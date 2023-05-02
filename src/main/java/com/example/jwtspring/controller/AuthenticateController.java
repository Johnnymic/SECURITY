package com.example.jwtspring.controller;

import com.example.jwtspring.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticateController {

     private final AuthenticationService authService;

     @PostMapping("/register")
     private ResponseEntity<AuthenticationResponse> register
             (@RequestBody RegisterRequest request ){

          return new ResponseEntity<>(  authService.register(request), HttpStatus.OK);
     }
     @PostMapping("/authenticate")
     private ResponseEntity<AuthenticationResponse> authenticateRequest
             (@RequestBody AuthenticationRequest request ){


          return new ResponseEntity<>(  authService.authenticate(request), HttpStatus.OK);
     }

}
