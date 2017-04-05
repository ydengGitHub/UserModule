package com.usermodule.services;

import com.usermodule.dto.SignupForm;

public interface UserService {
	public void signup(SignupForm signupForm);

	public void verify(String verificationCode);
}
