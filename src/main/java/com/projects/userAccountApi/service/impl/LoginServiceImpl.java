package com.projects.userAccountApi.service.impl;
import java.time.LocalDateTime;

import com.projects.userAccountApi.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projects.userAccountApi.controller.form.LoginForm;
import com.projects.userAccountApi.model.User;
import com.projects.userAccountApi.repository.UserRepository;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
    private UserRepository userRepository;

	@Override
    public boolean authenticateUser(LoginForm login) {
        String email = login.getEmail();
        String password = login.getPassword();
        
        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
   	     	throw new RuntimeException("E-mail e Senha são obrigatórios!");
        }

        User user = userRepository.findByEmail(email);
        
        boolean IsBlockedUser = validateIsBlockUser(user);
        if (IsBlockedUser) {
            throw new RuntimeException("Credencial bloqueada temporariamente, tente novamente em 3 horas.");
        }
        
        boolean isMatchPassword = validatePasswordMatch(user.getPassword(), password);
        if (!isMatchPassword) {
        	incrementFailTriesCounter(user);
        } else {
        	resetFailTriesCounter(user);
        }

        return isMatchPassword;
    }
    
    private boolean validateIsBlockUser(User user) {
    	int limitTriesToBlockUser = 6;

    	if (user.getFailTries() >= limitTriesToBlockUser) {
    		if (user.getBlockingDatetime() != null && LocalDateTime.now().isAfter(user.getBlockingDatetime())) {
				user.setBlockingDatetime(null);
				user.setFailTries(0);

				userRepository.save(user);

				return false;
    		}

			if (user.getBlockingDatetime() == null) {
				user.setBlockingDatetime(LocalDateTime.now().plusHours(3));
			}

			userRepository.save(user);

    		return true;
    	}
    	
    	return false;
    }
    
    private boolean validatePasswordMatch(String userPassword, String password) {
    	if (userPassword.equals(password)) {
    		return true;
    	}
    	
    	return false;
    }
    
    private void incrementFailTriesCounter(User user) {  	
    	user.incrementFailTriesCount();
    	
    	userRepository.save(user);
    }
    
    private void resetFailTriesCounter(User user) {  	
    	user.setFailTries(0);
    	
    	userRepository.save(user);
    }
}
