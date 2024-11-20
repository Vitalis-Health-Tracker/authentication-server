package com.example.authentication_server.controller;

import com.example.authentication_server.model.UserCredential;
import com.example.authentication_server.service.UserCredentialService;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserCredentialController {

    @Autowired
    private UserCredentialService userCredentialService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String addUser(@RequestBody UserCredential userCredential)
    {
        return userCredentialService.saveUser(userCredential);
    }

    @GetMapping("/validate/{token}")
    public boolean validateToken(@PathVariable String token){
        return userCredentialService.validateToken(token);
    }

    @GetMapping("/extract/{token}")
    public String extractRole(@PathVariable String token){
        return userCredentialService.extractRole(token);
    }

    @PostMapping("/validate/user")
    public String validateUser(@RequestBody UserCredential userCredential){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userCredential.getEmail(),userCredential.getPassword()));
        if(authentication.isAuthenticated())
        {
            return userCredentialService.generateToken(userCredential.getEmail());
        }
        else
        {
            return "User not authenticated!";
        }
    }

    @GetMapping("/{email}")
    public String getUserIdFromEmail(@PathVariable String email)
    {
        return userCredentialService.getUserIdFromEmail(email);
    }

    @DeleteMapping("/{email}")
    public boolean deleteUser(@PathVariable String email)
    {
        return userCredentialService.deleteUser(email);
    }
}
