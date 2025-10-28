package com.valencia.user.service;

import com.valencia.user.dto.AuthResponse;
import com.valencia.user.dto.LoginRequest;
import com.valencia.user.dto.RegisterRequest;
import com.valencia.user.dto.Role;
import com.valencia.user.entity.User;
import com.valencia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.el.stream.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private final UserRepository userRepository;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public AuthResponse login(LoginRequest request) {
        log.debug("Service login request: {}", request);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + request.getEmail()));

        String token = jwtService.generateToken(user.getEmail());
        return AuthResponse
                .builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        log.debug("register request: " + request);
        User user = User.builder()
                .id(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME))
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .lastname(request.getLastname())
                .email(request.getEmail())
                .role(Role.ADMIN)
                .build();

        log.debug("saved: " + userRepository.save(user));
        return AuthResponse.builder().token(jwtService.generateToken(user.getEmail())).build();
    }
}
