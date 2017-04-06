package com.usermodule.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.usermodule.entities.User;

public class ChangePasswordForm {

	@NotNull
	@Size(min=1, max=User.PASSWORD_MAX, message="{passwordSizeError}")
	private String oldPassword = "";
	
	@NotNull
	@Size(min=1, max=User.PASSWORD_MAX, message="{passwordSizeError}")
	private String newPassword = "";
	
	@NotNull
	@Size(min=1, max=User.PASSWORD_MAX, message="{passwordSizeError}")
	private String retypePassword = "";
	
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRetypePassword() {
		return retypePassword;
	}

	public void setRetypePassword(String retypePassword) {
		this.retypePassword = retypePassword;
	}		
}
