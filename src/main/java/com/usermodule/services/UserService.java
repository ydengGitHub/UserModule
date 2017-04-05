package com.usermodule.services;

import org.springframework.validation.BindingResult;

import com.usermodule.dto.ForgotPasswordForm;
import com.usermodule.dto.ResetPasswordForm;
import com.usermodule.dto.SignupForm;

public interface UserService {
	public void signup(SignupForm signupForm);

	public void verify(String verificationCode);

	public void forgotPassword(ForgotPasswordForm forgotPasswordForm);

	public void resetPassword(String forgotPasswordCode, ResetPasswordForm resetPasswordForm, BindingResult result);
}
