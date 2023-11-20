package com.projects.userAccountApi.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.projects.userAccountApi.controller.dto.UserDto;
import com.projects.userAccountApi.controller.form.UserForm;
import com.projects.userAccountApi.model.User;
import com.projects.userAccountApi.repository.UserRepository;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping
	public List<UserDto> listUsers() {
		List<User> users = userRepository.findAll();
		System.out.println(users);
		return UserDto.fromPersistLayer(users);
	}
	
	@PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserForm form, UriComponentsBuilder uriBuilder) {
        User user = form.toPersistLayer(userRepository);
        userRepository.save(user);
        
        URI uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserDto(user));
    }
	
	// Mapping to get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.map(value -> ResponseEntity.ok(new UserDto(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Mapping to update a user by ID
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable Long id, @RequestBody UserForm form) {
        Optional<User> optionalUser = userRepository.findById(id);
        
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(form.getName());
            user.setEmail(form.getEmail());
            user.setPassword(form.getPassword());

            userRepository.save(user);

            return ResponseEntity.ok(new UserDto(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
