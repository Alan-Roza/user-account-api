package com.projects.userAccountApi.service.impl;

import com.projects.userAccountApi.model.User;
import com.projects.userAccountApi.controller.form.UserForm;
import com.projects.userAccountApi.repository.UserRepository;

import com.projects.userAccountApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(UserForm form) {
        User user = form.toPersistLayer(userRepository);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> updateUserById(Long id, UserForm form) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(form.getName());
            user.setEmail(form.getEmail());
            user.setPassword(form.getPassword());
            return Optional.of(userRepository.save(user));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
