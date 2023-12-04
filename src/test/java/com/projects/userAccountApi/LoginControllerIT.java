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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginForm)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login realizado com sucesso!"));
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
    public void testLoginWithNullEmail() throws Exception {
        LoginForm login = new LoginForm(null, "password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("E-mail e Senha são obrigatórios!"));
    }

    @Test
    public void testLoginWithEmptyEmail() throws Exception {
        LoginForm login = new LoginForm("", "password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("E-mail e Senha são obrigatórios!"));
    }

    @Test
    public void testLoginWithNullPassword() throws Exception {
        LoginForm login = new LoginForm("test@example.com", null);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("E-mail e Senha são obrigatórios!"));
    }

    @Test
    public void testLoginWithEmptyPassword() throws Exception {
        LoginForm login = new LoginForm("test@example.com", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("E-mail e Senha são obrigatórios!"));
    }

}
