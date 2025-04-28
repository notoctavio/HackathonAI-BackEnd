package com.ProMatch.dto;

import com.ProMatch.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String company;
    private String phoneNumber;

    public JwtResponse(String token, Long id, String email, String firstName, String lastName, UserRole role, String company, String phoneNumber) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role.name(); // Convert enum to string
        this.company = company;
        this.phoneNumber = phoneNumber;
    }
    
    public JwtResponse(String token, Long id, String email, String firstName, String lastName, String role, String company, String phoneNumber) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.company = company;
        this.phoneNumber = phoneNumber;
    }
    
    // Keep legacy constructors for backward compatibility
    public JwtResponse(String token, Long id, String email, String firstName, String lastName, UserRole role) {
        this(token, id, email, firstName, lastName, role.name(), "", "");
    }
    
    public JwtResponse(String token, Long id, String email, String firstName, String lastName, String role) {
        this(token, id, email, firstName, lastName, role, "", "");
    }
} 