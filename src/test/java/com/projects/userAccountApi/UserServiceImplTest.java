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
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById() {
        Long userId = 1L;
        User mockUser = new User("John Doe", "john@example.com", "password123", null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.getUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(mockUser, result.get());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void createUser() {
        UserForm userForm = new UserForm("John Doe", "john@example.com", "password123");
        User mockUser = new User("John Doe", "john@example.com", "password123", null);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = userService.createUser(userForm);

        assertNotNull(result);
        assertEquals(mockUser, result);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserById() {
        Long userId = 1L;
        UserForm userForm = new UserForm("Updated Name", "updated@example.com", "updatedPassword");
        User mockUser = new User("John Doe", "john@example.com", "password123", null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        Optional<User> result = userService.updateUserById(userId, userForm);


        assertTrue(result.isPresent());
        assertEquals(mockUser, result.get());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUserById() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        boolean result = userService.deleteUserById(userId);

        assertTrue(result);

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUserById_UserNotFound() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        boolean result = userService.deleteUserById(userId);

        assertFalse(result);

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(userId);
    }
}
