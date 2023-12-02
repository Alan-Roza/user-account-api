package com.projects.userAccountApi.controller.form;

import com.projects.userAccountApi.contract.Login;
import com.projects.userAccountApi.model.User;
import com.projects.userAccountApi.repository.UserRepository;

public class UserForm {
	
	private String name;
	private String email;
	private String password;

	public UserForm(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public User toPersistLayer(UserRepository usuarioRepository) {
		
		return new User(name, email, password);
	}
	
	public User update(UserRepository userRepository, Long id) {
		User existingUser = userRepository.findById(id).orElse(null);
		
		if (existingUser != null) {
			existingUser.setName(name);
			existingUser.setEmail(email);
			existingUser.setPassword(password);
		}
		
		return existingUser;
	}
}
