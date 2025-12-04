package com.example.savvyproject.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.savvyproject.entities.User;
import com.example.savvyproject.services.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {
 
	private UserService userservice;
	
	@Autowired
	public UserController (UserService userservice) {
		this.userservice = userservice;
	}
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user){
		
		
		try {
			User registeredUser = userservice.registerUser(user);
			return ResponseEntity.ok(Map.of("message", "User registered successfully","user", registeredUser));
			
		}
		catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error i got", e.getMessage()));
		}
	}
	
	
	
}

