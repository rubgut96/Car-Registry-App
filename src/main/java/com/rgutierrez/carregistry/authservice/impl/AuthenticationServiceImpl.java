package com.rgutierrez.carregistry.authservice.impl;

import com.rgutierrez.carregistry.authservice.AuthenticationService;
import com.rgutierrez.carregistry.controller.dtos.JwtResponse;
import com.rgutierrez.carregistry.controller.dtos.LoginRequest;
import com.rgutierrez.carregistry.controller.dtos.SignUpRequest;
import com.rgutierrez.carregistry.jwtservice.JwtService;
import com.rgutierrez.carregistry.repository.UserRepository;
import com.rgutierrez.carregistry.repository.entities.UserEntity;
import com.rgutierrez.carregistry.userservice.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public JwtResponse signUp(SignUpRequest signUpRequest) {
        var user = UserEntity
                .builder()
                .name(signUpRequest.getName())
                .mail(signUpRequest.getMail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role("ROLE_CLIENT")
                .build();
        user = userService.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtResponse.builder().token(jwt).build();
    }
    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getMail(), loginRequest.getPassword()));
        var user = userRepository.findByMail(loginRequest.getMail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid mail or password"));
        var jwt = jwtService.generateToken(user);
        return  JwtResponse.builder().token(jwt).build();
    }
}
