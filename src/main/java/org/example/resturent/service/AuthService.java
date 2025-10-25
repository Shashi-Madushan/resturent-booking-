package org.example.resturent.service;

import org.example.resturent.dto.request.LoginRequest;
import org.example.resturent.dto.request.SignupRequest;
import org.example.resturent.dto.response.JwtResponse;
import org.example.resturent.dto.response.MessageResponse;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    MessageResponse registerUser(SignupRequest signUpRequest);
}
