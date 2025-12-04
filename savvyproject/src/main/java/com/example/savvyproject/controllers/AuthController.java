package com.example.savvyproject.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.savvyproject.entities.LoginRequest;
import com.example.savvyproject.entities.User;
import com.example.savvyproject.services.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        try {
            User user = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            String token = authService.generateToken(user);

            // create cookie object (without domain)
            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);        // Secure must be true when SameSite=None
            cookie.setPath("/");
            cookie.setMaxAge(3600);        // 1 hour
            
            response.addCookie(cookie);

            String cookieHeader = String.format(
            	    "authToken=%s; HttpOnly; SameSite=None; Path=/; Max-Age=3600",
            	    token
            	);

            response.addHeader("Set-Cookie", cookieHeader);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Login successful");
            responseBody.put("role", user.getRole().name());
            responseBody.put("username", user.getUsername());
            return ResponseEntity.ok(responseBody);

        } catch (RuntimeException e) {
        	
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String,String>> logout(HttpServletRequest request,HttpServletResponse response){
    	try {
    		User user = (User) request.getAttribute("authenticatedUser");
        	
        	authService.logout(user);
        	
        	Cookie cookie = new Cookie("authToken",null);
        	  cookie.setHttpOnly(true);
              cookie.setPath("/");
              cookie.setMaxAge(0);
              response.addCookie(cookie);
        	
              Map<String,String> responseBody=new HashMap<>();
              
              responseBody.put("message", "Logout successful");
              return ResponseEntity.ok(responseBody);
    	}
    	catch (Exception e) {
			Map<String,String> errorResponse = new HashMap<>();
			errorResponse.put("message", "logout failed");
			return ResponseEntity.status(500).body(errorResponse);
		}
    	
    	
    }
    
    
    
    
    
    
    
}
