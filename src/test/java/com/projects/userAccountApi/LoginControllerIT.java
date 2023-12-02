package com.projects.userAccountApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.userAccountApi.controller.LoginController;
import com.projects.userAccountApi.controller.form.LoginForm;
import com.projects.userAccountApi.model.User;
import com.projects.userAccountApi.repository.UserRepository;
import com.projects.userAccountApi.service.impl.LoginServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoginServiceImpl loginServiceImpl;

    @InjectMocks
    private LoginController loginController;

    private LoginForm validLoginForm;
    private User mockUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidLogin() throws Exception {
        validLoginForm = new LoginForm("test@example.com", "password123");

        mockUser = new User("John Doe", "test@example.com", "password123", null);
        when(userRepository.findByEmail(validLoginForm.getEmail())).thenReturn(mockUser);
        when(loginServiceImpl.authenticateUser(validLoginForm)).thenReturn(true);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginForm)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login realizado com sucesso!"))
                .andReturn();

    }

    @Test
    public void testInvalidLogin() throws Exception {
        validLoginForm = new LoginForm("test@example.com", "password1234");
        mockUser = new User("John Doe", "test@example.com", "password1234", null);
        when(userRepository.findByEmail(validLoginForm.getEmail())).thenReturn(mockUser);
        when(loginServiceImpl.authenticateUser(validLoginForm)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginForm)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciais inválidas."));
    }

    @Test
    public void testLoginWithNullEmail() {
        LoginForm login = new LoginForm(null, "password123");

        ResponseEntity<String> response = loginController.login(login);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("E-mail e Senha são obrigatórios!", response.getBody());
    }

    @Test
    public void testLoginWithEmptyEmail() {
        LoginForm login = new LoginForm("", "password123");

        ResponseEntity<String> response = loginController.login(login);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("E-mail e Senha são obrigatórios!", response.getBody());
    }

    @Test
    public void testLoginWithNullPassword() {
        LoginForm login = new LoginForm("test@example.com", null);

        ResponseEntity<String> response = loginController.login(login);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("E-mail e Senha são obrigatórios!", response.getBody());
    }

    @Test
    public void testLoginWithEmptyPassword() {
        LoginForm login = new LoginForm("test@example.com", "");

        ResponseEntity<String> response = loginController.login(login);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("E-mail e Senha são obrigatórios!", response.getBody());
    }
}