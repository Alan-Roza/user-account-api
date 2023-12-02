package com.projects.userAccountApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.userAccountApi.controller.UserController;
import com.projects.userAccountApi.controller.form.UserForm;
import com.projects.userAccountApi.model.User;
import com.projects.userAccountApi.repository.UserRepository;
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

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private UserForm validUserForm;
    private User mockUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListUsers() throws Exception {
        mockUser = new User("John Doe", "test@example.com", "password123", null);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(mockUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateUser() throws Exception {
        mockUser = new User("John Doe", "test@example.com", "password123", null);
        validUserForm = new UserForm("John Doe", "test@example.com", "password123");

        when(userRepository.save(any())).thenReturn(mockUser);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserForm)))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void testGetUserById() throws Exception {
        mockUser = new User("John Doe", "test@example.com", "password123", null);
        mockUser = new User("John Doe two", "test2@example.com", "password123", null);
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(mockUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/2"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateUserById() throws Exception {
        validUserForm = new UserForm("Updated Name", "updated@example.com", "updatedPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(mockUser));
        when(userRepository.save(any())).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserForm)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUserById() throws Exception {
        when(userRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserByIdNotExistingUser() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUserByIdNonExistingUser() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserForm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteUserByIdNonExistingUser() throws Exception {
        when(userRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/-1"))
                .andExpect(status().isNotFound());
    }
}
