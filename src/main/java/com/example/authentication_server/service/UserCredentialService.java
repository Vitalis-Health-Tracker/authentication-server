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

    @CircuitBreaker(name = "cirsaveUser",  fallbackMethod = "fallbackSaveUser")
    public String saveUser(UserCredential userCredential) {
        if(userCredentialRepo.findByEmail(userCredential.getEmail()).isPresent())
        {
            return "User already exists";
        }
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        RestClient restClient = RestClient.create();
        RoleDto roleDto = restClient.get()
                .uri("http://localhost:9088/roles/" + userCredential.getRole().getRoleId())
                .retrieve()
                .body(RoleDto.class);
        userCredential.setRole(roleDto);
        if(userCredentialRepo.save(userCredential) != null) {
            Boolean createUserCheck = restClient.post()
                    .uri("http://localhost:9091/user/" + userCredential.getEmail())
                    .retrieve().body(Boolean.class);
            if (createUserCheck == true) {
                return "User saved successfully";
            } else {
                return "User not saved, check your credentials again";
            }
        }
        else
        {
            return "User not saved, check your credentials again";
        }
    }

    public String fallbackSaveUser(UserCredential userCredential) {
        return "User not saved, check your credentials again";
    }

    public String generateToken(String email){
        String role = userCredentialRepo.findByEmail(email).get().getRole().getRoleName();
        return jwtService.generateToken(email, role);
    }

    public boolean validateToken(String token){
        jwtService.validateToken(token);
        return true;
    }

    public String extractRole(String token){
        return jwtService.getRoleFromToken(token);
    }

    public String getUserIdFromEmail(String email)
    {
        RestClient restClient = RestClient.create();
        return restClient.get()
                .uri("http://localhost:9091/user/retrieve-id/" + email)
                .retrieve()
                .body(String.class);
    }

    public boolean deleteUser(String email)
    {
        userCredentialRepo.deleteByEmail(email);
        if(userCredentialRepo.findByEmail(email).isPresent())
        {
            return false;
        }
        return true;
    }
}
