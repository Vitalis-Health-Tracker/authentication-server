package com.example.authentication_server.service;

import com.example.authentication_server.model.RoleDto;
import com.example.authentication_server.model.UserCredential;
import com.example.authentication_server.repository.UserCredentialRepo;
import com.example.authentication_server.service.JwtService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UserCredentialService {

    @Autowired
    private UserCredentialRepo userCredentialRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String saveUser(UserCredential userCredential) {
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        if(userCredentialRepo.save(userCredential) != null){
            return "User saved successfully";
        }
        else{
            return "User not saved";
        }
    }

    public String generateToken(String username){
        return jwtService.generateToken(username);
    }

    public boolean validateToken(String token){
        jwtService.validateToken(token);
        return true;
    }
}
