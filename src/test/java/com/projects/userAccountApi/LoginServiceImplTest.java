package com.projects.userAccountApi;

import com.projects.userAccountApi.controller.LoginController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;

import com.projects.userAccountApi.repository.UserRepository;
import com.projects.userAccountApi.service.impl.LoginServiceImpl;
import com.projects.userAccountApi.controller.form.LoginForm;
import com.projects.userAccountApi.model.User;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = LoginServiceImplTest.class)
public class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl loginServiceImpl;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginController loginController;

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

        boolean isUserAuthenticated = loginServiceImpl.authenticateUser(new LoginForm("test@example.com", "password123"));

        assertTrue(isUserAuthenticated);
    }

    @Test
    public void unsuccessfulUserLoginUsingInvalidCredentials() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setFailTries(0);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        boolean isUserAuthenticated = loginServiceImpl.authenticateUser(new LoginForm("test@example.com", "wrongpassword"));

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
                boolean isUserAuthenticated = loginServiceImpl.authenticateUser(new LoginForm("test@example.com", "wrongpassword"));
                assertFalse(isUserAuthenticated);
            }
        });

        assertEquals("Credencial bloqueada temporariamente, tente novamente em 3 horas.", exception.getMessage());
    }

    @Test
    public void testUnsuccessfulLoginWithEmptyPasswordField() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setFailTries(0);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assertFalse(loginServiceImpl.authenticateUser(new LoginForm("test@example.com", "")));
        });

        assertEquals("E-mail e Senha são obrigatórios!", exception.getMessage());
    }

    @Test
    public void testUnsuccessfulLoginWithEmptyEmailField() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setFailTries(0);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assertFalse(loginServiceImpl.authenticateUser(new LoginForm("", "password123")));
        });

        assertEquals("E-mail e Senha são obrigatórios!", exception.getMessage());
    }

    @Test
    public void testUnsuccessfulLoginWithEmptyEmailAndPasswordFields() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setFailTries(0);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assertFalse(loginServiceImpl.authenticateUser(new LoginForm("", "")));
        });

        assertEquals("E-mail e Senha são obrigatórios!", exception.getMessage());
    }

    @Test
    public void testUnsuccessfulLoginWithNullEmailField() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setFailTries(0);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assertFalse(loginServiceImpl.authenticateUser(new LoginForm(null, "")));
        });

        assertEquals("E-mail e Senha são obrigatórios!", exception.getMessage());
    }

    @Test
    public void testUnsuccessfulLoginWithNullPasswordField() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setFailTries(0);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assertFalse(loginServiceImpl.authenticateUser(new LoginForm("test@example.com", null)));
        });

        assertEquals("E-mail e Senha são obrigatórios!", exception.getMessage());
    }

    @Test
    public void testUnsuccessfulLoginWithNullEmailAndPasswordFields() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setFailTries(0);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assertFalse(loginServiceImpl.authenticateUser(new LoginForm(null, null)));
        });

        assertEquals("E-mail e Senha são obrigatórios!", exception.getMessage());
    }

    @Test
    public void testUnsuccessfulLoginWithoutFields() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setFailTries(0);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assertFalse(loginServiceImpl.authenticateUser(new LoginForm()));
        });

        assertEquals("E-mail e Senha são obrigatórios!", exception.getMessage());
    }
}
