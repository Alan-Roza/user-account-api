package com.projects.userAccountApi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
	
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    @Column(name = "name")
	private String name;
    
    @Column(name = "email")
	private String email;
    
    @Column(name = "password")
	private String password;
    
    @Column(name = "fail_tries_counter")
	private int failTriesCounter;
    
    @Column(name = "blocking_datetime")
    private LocalDateTime blockingDatetime;
	
	public User() {
	}

	public User(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.setFailTries(0);
	}

	public User(String name, String email, String password, LocalDateTime blockingDatetime) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.blockingDatetime = blockingDatetime;
		this.setFailTries(0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public int getFailTries() {
		return failTriesCounter;
	}
	
	public LocalDateTime getBlockingDatetime() {
		return blockingDatetime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	public void setFailTries(int failTriesCounter) {
		this.failTriesCounter = failTriesCounter;
	}	
	
	public void setBlockingDatetime(LocalDateTime datetime) {
		this.blockingDatetime = datetime;
	}	
	
	public void incrementFailTriesCount() {
		this.failTriesCounter += 1;
	}

}
