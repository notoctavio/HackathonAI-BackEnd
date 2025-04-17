package com.ProMatch.controller;

import com.ProMatch.dto.request.SignUpRequest;
import com.ProMatch.dto.response.UserResponse;
import com.ProMatch.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        UserResponse response = userService.registerUser(signUpRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
} 