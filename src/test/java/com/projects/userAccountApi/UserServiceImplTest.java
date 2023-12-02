package com.projects.userAccountApi;

import com.projects.userAccountApi.model.User;
import com.projects.userAccountApi.controller.form.UserForm;
import com.projects.userAccountApi.repository.UserRepository;
import com.projects.userAccountApi.service.impl.UserServiceImpl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserServiceImplTest.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    // Ensure that Mockito is properly initialized before each test
    @Test
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllUsers() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify that the findAll method of userRepository is called once
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById() {
        // Given
        Long userId = 1L;
        User mockUser = new User("John Doe", "john@example.com", "password123", null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // When
        Optional<User> result = userService.getUserById(userId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(mockUser, result.get());

        // Verify that the findById method of userRepository is called once with the specified userId
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void createUser() {
        // Given
        UserForm userForm = new UserForm("John Doe", "john@example.com", "password123");
        User mockUser = new User("John Doe", "john@example.com", "password123", null);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // When
        User result = userService.createUser(userForm);

        // Then
        assertNotNull(result);
        assertEquals(mockUser, result);

        // Verify that the save method of userRepository is called once with any User object
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserById() {
        // Given
        Long userId = 1L;
        UserForm userForm = new UserForm("Updated Name", "updated@example.com", "updatedPassword");
        User mockUser = new User("John Doe", "john@example.com", "password123", null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // When
        Optional<User> result = userService.updateUserById(userId, userForm);

        // Then

        assertTrue(result.isPresent());
        assertEquals(mockUser, result.get());

        // Verify that the findById method of userRepository is called once with the specified userId
        verify(userRepository, times(1)).findById(userId);
        // Verify that the save method of userRepository is called once with any User object
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUserById() {
        // Given
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // When
        boolean result = userService.deleteUserById(userId);

        // Then
        assertTrue(result);

        // Verify that the existsById method of userRepository is called once with the specified userId
        verify(userRepository, times(1)).existsById(userId);
        // Verify that the deleteById method of userRepository is called once with the specified userId
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUserById_UserNotFound() {
        // Given
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // When
        boolean result = userService.deleteUserById(userId);

        // Then
        assertFalse(result);

        // Verify that the existsById method of userRepository is called once with the specified userId
        verify(userRepository, times(1)).existsById(userId);
        // Verify that the deleteById method of userRepository is not called
        verify(userRepository, never()).deleteById(userId);
    }
}
