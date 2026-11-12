package com.agriculture.smartagriculture.controller;

import com.agriculture.smartagriculture.model.AppUser;
import com.agriculture.smartagriculture.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    public AuthController(UserRepository users) { this.users = users; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> body) {
        String name=body.getOrDefault("name","").trim();
        String email=body.getOrDefault("email","").trim().toLowerCase();
        String password=body.getOrDefault("password","");
        if(name.isBlank() || email.isBlank() || password.length()<6)
            return ResponseEntity.badRequest().body(Map.of("message","Name, valid email and password (6+ characters) are required."));
        if(users.existsByEmail(email)) return ResponseEntity.status(409).body(Map.of("message","Email already registered."));
        // Demo only: replace with BCrypt + Spring Security/JWT before deployment.
        users.save(new AppUser(name,email,password));
        return ResponseEntity.ok(Map.of("name",name,"email",email,"message","Registration successful."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        String email=body.getOrDefault("email","").trim().toLowerCase();
        String password=body.getOrDefault("password","");
        return users.findByEmail(email)
            .filter(u -> u.getPassword().equals(password))
            .<ResponseEntity<?>>map(u -> ResponseEntity.ok(Map.of("name",u.getName(),"email",u.getEmail())))
            .orElseGet(() -> ResponseEntity.status(401).body(Map.of("message","Invalid email or password.")));
    }
}
