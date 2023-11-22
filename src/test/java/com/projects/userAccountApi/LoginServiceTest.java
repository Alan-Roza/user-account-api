package com.projects.userAccountApi;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;

import com.projects.userAccountApi.repository.UserRepository;
import com.projects.userAccountApi.service.LoginService;
import com.projects.userAccountApi.controller.form.LoginForm;
import com.projects.userAccountApi.model.User;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = LoginServiceTest.class)
public class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

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
    public void userBlockedAfterSixUnsuccessfulLoginAttempts() {
        int attemptsLimit = 6;

        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setFailTries(0);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            for (int i = 0; i <= attemptsLimit; i++) {
                boolean isUserAuthenticated = loginService.authenticateUser(new LoginForm("test@example.com", "wrongpassword"));
                assertFalse(isUserAuthenticated);
            }
        });

        assertEquals("Credencial bloqueada temporariamente, tente novamente em 3 horas.", exception.getMessage());
    }

    @Test
    public void unsuccessfulLoginWithEmptyField() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setFailTries(0);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assertFalse(loginService.authenticateUser(new LoginForm("test@example.com", "")));
            assertFalse(loginService.authenticateUser(new LoginForm("", "password123")));
            assertFalse(loginService.authenticateUser(new LoginForm("", "")));
            assertFalse(loginService.authenticateUser(new LoginForm(null, "")));
            assertFalse(loginService.authenticateUser(new LoginForm("test@example.com", null)));
            assertFalse(loginService.authenticateUser(new LoginForm(null, null)));
        });

        assertEquals("E-mail e Senha são obrigatórios!", exception.getMessage());
    }
}
