package com.rgutierrez.carregistry.authservice.impl;

import com.rgutierrez.carregistry.controller.dtos.JwtResponse;
import com.rgutierrez.carregistry.controller.dtos.LoginRequest;
import com.rgutierrez.carregistry.controller.dtos.SignUpRequest;
import com.rgutierrez.carregistry.jwtservice.impl.JwtServiceImpl;
import com.rgutierrez.carregistry.repository.UserRepository;
import com.rgutierrez.carregistry.repository.entities.UserEntity;
import com.rgutierrez.carregistry.userservice.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationServiceImpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtServiceImpl jwtServiceImpl;
    @Mock
    private AuthenticationManager authenticationManager;
    private SignUpRequest signUpRequest;
    private SignUpRequest createSignUpRequest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName("name");
        signUpRequest.setMail("mail");
        signUpRequest.setPassword("password");
        return signUpRequest;
    }
    private LoginRequest loginRequest;
    private LoginRequest createLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMail(signUpRequest.getMail());
        loginRequest.setPassword(signUpRequest.getPassword());
        return loginRequest;
    }
    private UserEntity userEntity;
    private UserEntity createUserEntity(){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(null);
        userEntity.setName(signUpRequest.getName());
        userEntity.setMail(signUpRequest.getMail());
        userEntity.setPassword(signUpRequest.getPassword());
        userEntity.setRole("ROLE_CLIENT");
        userEntity.setImage(null);
        return userEntity;
    }
    @BeforeEach
    public void setUp() {
        signUpRequest = createSignUpRequest();
        loginRequest = createLoginRequest();
        userEntity = createUserEntity();
    }
    @Test
    void signup_test_success() {

        when(userService.save(userEntity)).thenReturn(userEntity);
        when(passwordEncoder.encode(anyString())).thenReturn(signUpRequest.getPassword());
        when(jwtServiceImpl.generateToken(userEntity)).thenReturn("token");

        var result = authenticationServiceImpl.signUp(signUpRequest);

        assertEquals("token", result.getToken());
    }
    @Test
    void login_test_success() {

        when(userRepository.findByMail(loginRequest.getMail())).thenReturn(Optional.of(userEntity));
        when(jwtServiceImpl.generateToken(userEntity)).thenReturn("token");

        JwtResponse jwtResponse = authenticationServiceImpl.login(loginRequest);

        assertEquals("token", jwtResponse.getToken());
    }
    @Test
    void login_test_failure() {

        when(userRepository.findByMail(loginRequest.getMail())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authenticationServiceImpl.login(loginRequest));
    }
}