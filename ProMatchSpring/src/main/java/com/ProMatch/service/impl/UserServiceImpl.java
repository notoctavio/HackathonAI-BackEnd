package com.ProMatch.service.impl;

import com.ProMatch.dto.SignupRequest;
import com.ProMatch.entity.User;
import com.ProMatch.exception.ResourceAlreadyExistsException;
import com.ProMatch.repository.UserRepository;
import com.ProMatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(SignupRequest signUpRequest) {
        logger.info("Checking if user with email {} already exists", signUpRequest.getEmail());
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            logger.warn("User with email {} already exists", signUpRequest.getEmail());
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        logger.info("Creating new user with email {}", signUpRequest.getEmail());
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setCompany(signUpRequest.getCompany());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setRole("USER");
        
        logger.info("Saving user to database");
        User savedUser = userRepository.save(user);
        logger.info("User saved successfully with ID: {}", savedUser.getId());
        
        return savedUser;
    }
    
    @Override
    public User findByEmail(String email) {
        logger.info("Finding user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new RuntimeException("User not found with email: " + email);
                });
    }
} 