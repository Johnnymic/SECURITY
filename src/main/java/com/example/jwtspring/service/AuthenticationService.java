package com.example.jwtspring.service;

import com.example.jwtspring.config.JwtService;
import com.example.jwtspring.controller.AuthenticationRequest;
import com.example.jwtspring.controller.AuthenticationResponse;
import com.example.jwtspring.controller.RegisterRequest;
import com.example.jwtspring.enums.Role;
import com.example.jwtspring.enums.TokenType;
import com.example.jwtspring.token.Token;
import com.example.jwtspring.user.User;
import com.example.jwtspring.userRepository.TokenRepository;
import com.example.jwtspring.userRepository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private  final UserRepo userRepo;

    private  final JwtService jwtService;

    private  final PasswordEncoder passwordEncoder;

    private   final AuthenticationManager manager;



    private  final TokenRepository tokenRepository;



    public AuthenticationResponse register(RegisterRequest request){
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .role(Role.USER)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var saveUser =  userRepo.save(user);
         String  jwtToken = jwtService.generateToken(user);
         saveUserToken(saveUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }



    public   AuthenticationResponse authenticate(AuthenticationRequest request){
             manager.authenticate(new UsernamePasswordAuthenticationToken(
                      request.getEmail(),
                     request.getPassword()

             ));
          User user = userRepo.findByEmail(request.getEmail())
                  .orElseThrow(()-> new NullPointerException("USER NOT FOUND"));
          log.info("User information == {} and {}",user.getEmail(),user.getPassword());
        String  jwtToken = jwtService.generateToken(user);
        revokeAllUserToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }
    private void saveUserToken(User user, String jwtToken) {
        Token token =Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }
    private void revokeAllUserToken(User user) {
        var validToken = tokenRepository.findAllValidTokenByUser(user.getId());

        if (validToken.isEmpty())
            return;
        validToken.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
        tokenRepository.saveAll(validToken);
    }


    }
