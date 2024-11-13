package com.example.authentication_server.repository;

import com.example.authentication_server.model.UserCredential;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepo extends MongoRepository<UserCredential, String> {
   public Optional<UserCredential> findByEmail(String email);
}
