package com.rgutierrez.carregistry.authservice;

import com.rgutierrez.carregistry.controller.dtos.JwtResponse;
import com.rgutierrez.carregistry.controller.dtos.LoginRequest;
import com.rgutierrez.carregistry.controller.dtos.SignUpRequest;

public interface AuthenticationService {
    JwtResponse signUp(SignUpRequest signUpRequest);
    JwtResponse login(LoginRequest loginRequest);
}
