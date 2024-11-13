package com.example.authentication_server.configuration;

import com.example.authentication_server.repository.UserCredentialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserCredentialUserService implements UserDetailsService {

    @Autowired
    private UserCredentialRepo userCredentialRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userCredentialRepo.findByEmail(email).map(UserCredentialUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
