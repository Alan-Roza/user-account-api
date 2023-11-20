package com.projects.userAccountApi.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.projects.userAccountApi.model.User;

public class UserDto {
	
	private String name;
	private String email;
	private int failTriesCounter;
	private LocalDateTime blockingDatetime;
	
	
	public UserDto(User user) {
		super();
		this.name = user.getName();
		this.email = user.getEmail();
		this.failTriesCounter = user.getFailTries();
		this.blockingDatetime = user.getBlockingDatetime();
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}
	
	public int getFailTriesCounter() {
		return failTriesCounter;
	}
	
	public void setFailTriesCounter(int newCount) {
		this.failTriesCounter = newCount;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getBlockingDatetime() {
		return blockingDatetime;
	}

	public void setBlockingDatetime(LocalDateTime blockingDatetime) {
		this.blockingDatetime = blockingDatetime;
	}

	public static List<UserDto> fromPersistLayer(List<User> users) {
		return users.stream().map(UserDto::new).collect(Collectors.toList());
	}
}
