package com.usermodule.services;

import javax.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.BindingResult;

import com.usermodule.dto.ChangeEmailForm;
import com.usermodule.dto.ChangePasswordForm;
import com.usermodule.dto.ForgotPasswordForm;
import com.usermodule.dto.ResetPasswordForm;
import com.usermodule.dto.SignupForm;
import com.usermodule.dto.UserDetailsImpl;
import com.usermodule.dto.UserEditForm;
import com.usermodule.entities.User;
import com.usermodule.entities.User.Role;
import com.usermodule.mail.MailSender;
import com.usermodule.repositories.UserRepository;
import com.usermodule.util.MyUtil;

/*Implementation of UserService interface, Service annotation is like @Component*/
/*Add data entry to database*/
@Service
@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private static final Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private MailSender mailSender;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
			MailSender mailSender){
		this.userRepository=userRepository;
		this.passwordEncoder=passwordEncoder;
		this.mailSender=mailSender;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void signup(SignupForm signupForm) {
		final User user=new User();//need to define as final to make TransactionSynchronization works
		user.setEmail(signupForm.getEmail());
		user.setName(signupForm.getName());
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
		user.getRoles().add(Role.UNVERIFIED);
		//Apache RandomStringUtils class, need to include by adding to pom.xml
		user.setVerificationCode(RandomStringUtils.randomAlphanumeric(User.RANDOM_CODE_LENGTH));
		userRepository.save(user);
		//int j=20/0; //if met error, the user will not be added to database
		/*Deal with the situation: user get email, but error occur and roll back.
		 * Tell Spring, Only run these codes when the transaction succeed.*/
		TransactionSynchronizationManager.registerSynchronization(
			    new TransactionSynchronizationAdapter() {
			        @Override
			        public void afterCommit() {
			    		try {
			    			String verifyLink = MyUtil.hostUrl() + "/users/" + user.getVerificationCode() + "/verify";
			    			//verifyLink will be inserted to {0} of "verifyEmail" message
			    			mailSender.send(user.getEmail(), MyUtil.getMessage("verifySubject"), MyUtil.getMessage("verifyEmail", verifyLink));
			    			logger.info("Verification mail to " + user.getEmail() + " queued.");
						} catch (MessagingException e) {
							logger.error(ExceptionUtils.getStackTrace(e));
						}
			        }
		    });
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepository.findByEmail(username);
		if(user==null) throw new UsernameNotFoundException(username);
		return new UserDetailsImpl(user);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void verify(String verificationCode) {
		/*
		long loggedInUserId=MyUtil.getSessionUser().getId();
		User user=userRepository.findOne(loggedInUserId);*/
		User user=userRepository.findByVerificationCode(verificationCode);
		/*if false, displayed the message associate with second arg, third argument will be inserted to the message*/
		MyUtil.validate(user.getRoles().contains(Role.UNVERIFIED), "alreadyVerified");
		//MyUtil.validate(user.getVerificationCode().equals(verificationCode), "incorrect", "verification code");
		user.getRoles().remove(Role.UNVERIFIED);
		user.setVerificationCode(null);
		userRepository.save(user);//update the database
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void forgotPassword(ForgotPasswordForm form) {
		final User user=userRepository.findByEmail(form.getEmail());
		String forgotPasswordCode=RandomStringUtils.randomAlphanumeric(User.RANDOM_CODE_LENGTH);
		User existUser=userRepository.findByForgotPasswordCode(forgotPasswordCode);
		while(existUser!=null){
			forgotPasswordCode=RandomStringUtils.randomAlphanumeric(User.RANDOM_CODE_LENGTH);
			existUser=userRepository.findByForgotPasswordCode(forgotPasswordCode);
		}
		user.setForgotPasswordCode(forgotPasswordCode);
		final User savedUser=userRepository.save(user);
		
		TransactionSynchronizationManager.registerSynchronization(
				new TransactionSynchronizationAdapter(){
					@Override
					public void afterCommit(){
						try{
							mailForgotPasswordLink(savedUser);
						}catch (MessagingException e){
							logger.error(ExceptionUtils.getStackTrace(e));
						}
					}
				});	
	}
	
	private void mailForgotPasswordLink(User user) throws MessagingException{
		String forgotPasswordLink = 
				MyUtil.hostUrl() + "/reset-password/" +
				user.getForgotPasswordCode();
		mailSender.send(user.getEmail(),
				MyUtil.getMessage("forgotPasswordSubject"),
				MyUtil.getMessage("forgotPasswordEmail", forgotPasswordLink));
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void resetPassword(String forgotPasswordCode,
			ResetPasswordForm resetPasswordForm,
			BindingResult result) {
		
		User user = userRepository.findByForgotPasswordCode(forgotPasswordCode);
		if (user == null) result.reject("invalidForgotPassword");
		
		if (result.hasErrors()) return;
		
		user.setForgotPasswordCode(null);
		user.setPassword(passwordEncoder.encode(resetPasswordForm.getPassword().trim()));
		userRepository.save(user);
	}

	@Override
	public User findOne(long userId) {
		//show different sets of information based on login user roles
		User loggedIn=MyUtil.getSessionUser();
		User user=userRepository.findOne(userId);
		if(loggedIn ==null ||
				loggedIn.getId()!=user.getId() && !loggedIn.isAdmin()){
			//hide the email id
			user.setEmail("Confidential");
		}
		return user;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void update(long userId, UserEditForm userEditForm) {
		User loggedIn=MyUtil.getSessionUser();
		MyUtil.validate(loggedIn.isAdmin() ||
				loggedIn.getId()==userId, "noPermission");
		User user=userRepository.findOne(userId);
		user.setName(userEditForm.getName());
		if(loggedIn.isAdmin()) user.setRoles(userEditForm.getRoles());
		userRepository.save(user);
	}
	
	@Override
	public boolean checkPassword(long userId, String password){
		User user=userRepository.findById(userId);
		return passwordEncoder.matches(password.trim(), user.getPassword());
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void changePassword(long userId,
			ChangePasswordForm changePasswordForm,
			BindingResult result) {
		
		User user = userRepository.findById(userId);
		if (user == null) result.reject("userNotFound");
		User loggedIn=MyUtil.getSessionUser();
		MyUtil.validate(loggedIn.isAdmin() ||
				loggedIn.getId()==userId, "noPermission");
		if(!loggedIn.isAdmin() && !checkPassword(userId, changePasswordForm.getOldPassword()))
			result.reject("incorrectPassword");
		if (result.hasErrors()) return;
		
		user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword().trim()));
		userRepository.save(user);	
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void changeEmail(long userId, ChangeEmailForm changeEmailForm, BindingResult result) {
		final User user = userRepository.findById(userId);
		if (user == null) result.reject("userNotFound");
		if (result.hasErrors()) return;
		User loggedIn=MyUtil.getSessionUser();
		MyUtil.validate(loggedIn.isAdmin() ||
				loggedIn.getId()==userId, "noPermission");
		
		String changeEmailCode=RandomStringUtils.randomAlphanumeric(User.RANDOM_CODE_LENGTH);
		User existUser=userRepository.findByChangeEmailCode(changeEmailCode);
		while(existUser!=null){
			changeEmailCode=RandomStringUtils.randomAlphanumeric(User.RANDOM_CODE_LENGTH);
			existUser=userRepository.findByChangeEmailCode(changeEmailCode);
		}
		user.setChangeEmailCode(changeEmailCode);
		user.setNewEmail(changeEmailForm.getEmail());
		user.getRoles().add(User.Role.CHANGINGEMAIL);
		final User savedUser=userRepository.save(user);
		
		TransactionSynchronizationManager.registerSynchronization(
				new TransactionSynchronizationAdapter(){
					@Override
					public void afterCommit(){
						try{
							mailChangeEmailLink(savedUser);
						}catch (MessagingException e){
							logger.error(ExceptionUtils.getStackTrace(e));
						}
					}
				});	
	}
	
	private void mailChangeEmailLink(User user) throws MessagingException{
		String changeEmailLink = 
				MyUtil.hostUrl() + "/users/" +
				user.getChangeEmailCode()+"/verifyEmail";
		mailSender.send(user.getNewEmail(),
				MyUtil.getMessage("changeEmailSubject"),
				MyUtil.getMessage("changeEmailEmail", changeEmailLink));
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void verifyEmail(String changeEmailCode) {
		User user=userRepository.findByChangeEmailCode(changeEmailCode);
		/*if false, 显示第二个参数key对应的 信息, third argument will be inserted to the message*/
		MyUtil.validate(user!=null, "userNotFound");
		MyUtil.validate(user.getRoles().contains(Role.CHANGINGEMAIL), "alreadyVerified");
		user.getRoles().remove(Role.CHANGINGEMAIL);
		user.setChangeEmailCode(null);
		user.setEmail(user.getNewEmail());
		user.setNewEmail(null);
		userRepository.save(user);//update the database
	}
}
