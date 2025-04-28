package com.ProMatch.service;

import com.ProMatch.dto.SignupRequest;
import com.ProMatch.entity.User;

public interface UserService {
    User registerUser(SignupRequest signUpRequest);
    User findByEmail(String email);
} 