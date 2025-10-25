package org.example.resturent.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.request.LoginRequest;
import org.example.resturent.dto.request.SignupRequest;
import org.example.resturent.dto.response.JwtResponse;
import org.example.resturent.dto.response.MessageResponse;
import org.example.resturent.exeptions.custom.EmailAlreadyExistsException;
import org.example.resturent.model.User;
import org.example.resturent.repository.UserRepository;
import org.example.resturent.security.jwt.JwtUtils;
import org.example.resturent.security.services.UserDetailsImpl;
import org.example.resturent.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();        
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        );
    }

    @Override
    public MessageResponse registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // Create new user's account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(signUpRequest.getRole() != null ? signUpRequest.getRole() : "USER");
        
        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }
}

