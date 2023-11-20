package com.projects.userAccountApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.userAccountApi.controller.LoginController;
import com.projects.userAccountApi.controller.form.LoginForm;
import com.projects.userAccountApi.model.User;
import com.projects.userAccountApi.repository.UserRepository;
import com.projects.userAccountApi.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    private LoginForm validLoginForm;
    private User mockUser;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidLogin() throws Exception {
        // Set up a valid LoginForm for testing
        validLoginForm = new LoginForm("test@example.com", "password123");

        // Set up a valid User for testing
        mockUser = new User("John Doe", "test@example.com", "password123", null);
        // Mock the userRepository to return a user
        when(userRepository.findByEmail(validLoginForm.getEmail())).thenReturn(mockUser);
        // Mock the authentication service to return true
        when(loginService.authenticateUser(validLoginForm)).thenReturn(true);

        // Perform the POST request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginForm)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login realizado com sucesso!"))
                .andReturn();

    }

    @Test
    public void testInvalidLogin() throws Exception {
        // Set up an invalid LoginForm for testing
        validLoginForm = new LoginForm("test@example.com", "password1234");
        // Set up an invalid User for testing
        mockUser = new User("John Doe", "test@example.com", "password1234", null);
        // Mock the userRepository to return a user
        when(userRepository.findByEmail(validLoginForm.getEmail())).thenReturn(mockUser);
        // Mock the authentication service to return false
        when(loginService.authenticateUser(validLoginForm)).thenReturn(false);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginForm)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciais inválidas."));
    }
}