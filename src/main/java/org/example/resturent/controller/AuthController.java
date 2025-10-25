package org.example.resturent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.request.LoginRequest;
import org.example.resturent.dto.request.SignupRequest;
import org.example.resturent.dto.response.JwtResponse;
import org.example.resturent.dto.response.MessageResponse;
import org.example.resturent.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return ResponseEntity.ok(authService.registerUser(signUpRequest));
    }
}
