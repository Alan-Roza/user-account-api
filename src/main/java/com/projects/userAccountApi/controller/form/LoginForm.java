package com.projects.userAccountApi.controller.form;

import com.projects.userAccountApi.contract.Login;

public class LoginForm {

	private String email;
	private String password;
	
	public LoginForm() {
    }
	
	public LoginForm(Login login) {
		this.email = login.getEmail();
		this.password = login.getPassword();
	}

	public LoginForm(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
	
}
