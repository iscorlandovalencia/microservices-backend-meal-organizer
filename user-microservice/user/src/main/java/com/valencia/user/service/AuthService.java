package com.valencia.user.service;

import com.valencia.user.dto.AuthResponse;
import com.valencia.user.dto.LoginRequest;
import com.valencia.user.dto.RegisterRequest;
import com.valencia.user.dto.Role;
import com.valencia.user.entity.User;
import com.valencia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user.getUsername());
        return AuthResponse
                .builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .id(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME))
                .name(request.getName())
                .password(request.getPassword())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .role(Role.valueOf(request.getRole()))
                .build();

        userRepository.save(user);
        return AuthResponse.builder().token(jwtService.generateToken(user.getUsername())).build();
    }
}
