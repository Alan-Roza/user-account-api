package com.projects.userAccountApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.projects.userAccountApi.controller.form.LoginForm;
import com.projects.userAccountApi.service.impl.LoginServiceImpl;


@RestController
public class LoginController {

    @Autowired
    private LoginServiceImpl loginServiceImpl;
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginForm login) {
    	try {
    		validateFields(login);
    		
    		if (loginServiceImpl.authenticateUser(login)) {
    			return ResponseEntity.ok("Login realizado com sucesso!");
    		} else {
    			return ResponseEntity.status(401).body("Credenciais inválidas.");
    		}
    	} catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
    private void validateFields(LoginForm user) {
    	if (user.getEmail() == null || user.getPassword() == null || user.getEmail().trim().isEmpty() || user.getPassword().trim().isEmpty()) {
    	     throw new RuntimeException("E-mail e Senha são obrigatórios!");
    	}
    }

}