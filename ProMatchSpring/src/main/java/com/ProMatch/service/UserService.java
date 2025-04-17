package com.ProMatch.service;

import com.ProMatch.dto.request.SignUpRequest;
import com.ProMatch.dto.response.UserResponse;
import com.ProMatch.entity.User;
import com.ProMatch.exception.ResourceAlreadyExistsException;
import com.ProMatch.exception.ValidationException;
import com.ProMatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(SignUpRequest signUpRequest) {
        validateSignUpRequest(signUpRequest);

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        User user = mapSignUpRequestToUser(signUpRequest);
        User savedUser = userRepository.save(user);
        
        return mapUserToResponse(savedUser);
    }

    private void validateSignUpRequest(SignUpRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("Passwords do not match");
        }
    }

    private User mapSignUpRequestToUser(SignUpRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .company(request.getCompany())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .build();
    }

    private UserResponse mapUserToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .company(user.getCompany())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
} 