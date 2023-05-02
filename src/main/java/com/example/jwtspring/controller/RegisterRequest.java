package com.example.jwtspring.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class RegisterRequest {
    private String firstname;
    private String lastname;

    private String email;

    private String password;


}
