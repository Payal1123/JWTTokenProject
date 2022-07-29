package com.example.JWTSpringBoot.Service;

import com.example.JWTSpringBoot.Model.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.Optional;

public interface UserService  {

    Integer saveUSer(User user);
    Optional<User> findbyUsername(String username);
}
