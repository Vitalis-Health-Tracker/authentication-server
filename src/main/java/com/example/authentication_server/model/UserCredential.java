package com.example.authentication_server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Auth")
public class UserCredential {
    @Id
    private String authId;
    private String email;
    private String password;
    private RoleDto role;
}
