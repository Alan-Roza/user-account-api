package com.projects.userAccountApi.service;

import com.projects.userAccountApi.model.User;
import com.projects.userAccountApi.controller.form.UserForm;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createUser(UserForm form);
    Optional<User> updateUserById(Long id, UserForm form);
    boolean deleteUserById(Long id);
}

