package com.ProMatch.controller;

import com.ProMatch.dto.LoginRequest;
import com.ProMatch.dto.SignupRequest;
import com.ProMatch.dto.JwtResponse;
import com.ProMatch.entity.User;
import com.ProMatch.repository.UserRepository;
import com.ProMatch.security.jwt.JwtUtils;
import com.ProMatch.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            logger.info("Attempting to register user with email: {}", signUpRequest.getEmail());
            
            // Validate if email already exists
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                logger.warn("Email {} is already in use", signUpRequest.getEmail());
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Email is already in use!");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            User user = userService.registerUser(signUpRequest);
            logger.info("User registered successfully with ID: {}", user.getId());
            
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "User registered successfully!");
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Attempting to authenticate user with email: {}", loginRequest.getEmail());
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
    
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
    
            User user = userService.findByEmail(loginRequest.getEmail());
            logger.info("User authenticated successfully with ID: {}", user.getId());
            
            // Create response with explicitly set fields
            JwtResponse response = new JwtResponse();
            response.setToken(jwt);
            response.setType("Bearer");
            response.setId(user.getId());
            response.setEmail(user.getEmail());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setRole(user.getRole().toString());
            response.setCompany(user.getCompany());
            response.setPhoneNumber(user.getPhoneNumber());
            
            logger.info("Sending login response with role: {}", response.getRole());
    
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user {}: {}", loginRequest.getEmail(), e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            logger.error("Login error: {}", e.getMessage(), e);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
} 