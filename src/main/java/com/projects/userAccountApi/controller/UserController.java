package com.projects.userAccountApi.controller;

import com.projects.userAccountApi.controller.dto.UserDto;
import com.projects.userAccountApi.controller.form.UserForm;
import com.projects.userAccountApi.model.User;
import com.projects.userAccountApi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDto> listUsers() {
        List<UserDto> users = UserDto.fromPersistLayer(userService.getAllUsers());
        System.out.println(users);
        return users;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserForm form, UriComponentsBuilder uriBuilder) {
        User user = userService.createUser(form);
        URI uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<UserDto> userDto = userService.getUserById(id).map(UserDto::new);
        return userDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable Long id, @RequestBody UserForm form) {
        Optional<UserDto> updatedUserDto = userService.updateUserById(id, form).map(UserDto::new);
        return updatedUserDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        boolean deleted = userService.deleteUserById(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
