package com.usermodule.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usermodule.dto.SignupForm;
import com.usermodule.entities.User;
import com.usermodule.repositories.UserRepository;

/*Implementation of UserService interface, Service annotation is like @Component*/
/*Add data entry to database*/
@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository){
		this.userRepository=userRepository;
	}
	
	@Override
	public void signup(SignupForm signupForm) {
		User user=new User();
		user.setEmail(signupForm.getEmail());
		user.setName(signupForm.getName());
		user.setPassword(signupForm.getPassword());
		userRepository.save(user);
	}
}
