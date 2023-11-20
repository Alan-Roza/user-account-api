package com.projects.userAccountApi;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.projects.userAccountApi.repository.UserRepository;
import com.projects.userAccountApi.service.LoginService;

import com.projects.userAccountApi.controller.form.LoginForm;
import com.projects.userAccountApi.model.User;

//@SpringBootTest
@RunWith(SpringRunner.class)
public class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void successfullyUserLoginUsingValidCredentials() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setFailTries(0);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        boolean isUserAuthenticated = loginService.authenticateUser(new LoginForm("test@example.com", "password123"));

        assertTrue(isUserAuthenticated);
    }

    @Test
    public void unsuccessfulUserLoginUsingInvalidCredentials() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setFailTries(0);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        boolean isUserAuthenticated = loginService.authenticateUser(new LoginForm("test@example.com", "wrongpassword"));

        assertFalse(isUserAuthenticated);
    }

    @Test
    public void userBlockedAfterSixUnsuccessfulLoginAttempts() throws InterruptedException {
        int attemptsLimit = 6;

        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setFailTries(0);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        try {
            for (int i = 0; i <= attemptsLimit; i++) {
                boolean isUserAuthenticated = loginService.authenticateUser(new LoginForm("test@example.com", "wrongpassword"));
                assertFalse(isUserAuthenticated);
            }
            fail("Expected RuntimeException, but it was not thrown");
        } catch (RuntimeException e) {
            assertEquals("Credencial bloqueada temporariamente, tente novamente em 3 horas.", e.getMessage());
        }
    }

    @Test
    public void unsuccessfulLoginWithEmptyField() throws InterruptedException {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setFailTries(0);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        try {
        	assertFalse(loginService.authenticateUser(new LoginForm("test@example.com", "")));
        	assertFalse(loginService.authenticateUser(new LoginForm("", "password123")));
        	assertFalse(loginService.authenticateUser(new LoginForm("", "")));
        	assertFalse(loginService.authenticateUser(new LoginForm(null, "")));
        	assertFalse(loginService.authenticateUser(new LoginForm("test@example.com", null)));
        	assertFalse(loginService.authenticateUser(new LoginForm(null, null)));

            fail("Expected RuntimeException, but it was not thrown");
        } catch (RuntimeException e) {
            assertEquals("E-mail e Senha são obrigatórios!", e.getMessage());
        }
    }
}
