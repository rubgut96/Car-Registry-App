package com.rgutierrez.carregistry.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rgutierrez.carregistry.authservice.impl.AuthenticationServiceImpl;
import com.rgutierrez.carregistry.controller.dtos.JwtResponse;
import com.rgutierrez.carregistry.controller.dtos.LoginRequest;
import com.rgutierrez.carregistry.controller.dtos.SignUpRequest;
import com.rgutierrez.carregistry.controller.dtos.UserResponse;
import com.rgutierrez.carregistry.filter.JwtAuthenticationFilter;
import com.rgutierrez.carregistry.jwtservice.impl.JwtServiceImpl;
import com.rgutierrez.carregistry.userservice.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationServiceImpl authenticationServiceImpl;
    @MockBean
    private JwtServiceImpl jwtService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private SignUpRequest signUpRequest;
    private SignUpRequest createSignUpRequest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName("name");
        signUpRequest.setMail("male");
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

    private ObjectMapper mapper;
    private ObjectMapper createMapper(){
        return new ObjectMapper();
    }

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        signUpRequest = createSignUpRequest();
        loginRequest = createLoginRequest();
        mapper = createMapper();
    }

    @Test
    void signUp_test_success() throws Exception {

        JwtResponse jwtResponse = JwtResponse.builder().token("expectedToken").build();

        when(authenticationServiceImpl.signUp(any(SignUpRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signUpRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(jwtResponse)));
    }
    @Test
    void signUp_test_failure() throws Exception {

        when(authenticationServiceImpl.signUp(any(SignUpRequest.class))).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signUpRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void login_test_success() throws Exception {

        JwtResponse jwtResponse = JwtResponse.builder().token("expectedToken").build();

        when(authenticationServiceImpl.login(any(LoginRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(jwtResponse)));
    }
    @Test
    void login_test_failure() throws Exception {

        when(authenticationServiceImpl.login(any(LoginRequest.class))).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void uploadImageToProfile_test_success() throws Exception {

        byte[] mockImageBytes = new byte[20];
        MockMultipartFile mockImage = new MockMultipartFile("image","test.png","image/png",mockImageBytes);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/uploadImageToProfile")
                        .file(mockImage))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Image uploaded."));
    }
    @Test
    void uploadImageToProfile_test_failure() throws Exception {

        byte[] mockImageBytes = new byte[20];
        MockMultipartFile mockImage = new MockMultipartFile("image","test.png","",mockImageBytes);

        doThrow(new RuntimeException()).when(userService).addImageToProfile(any(MultipartFile.class));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/uploadImageToProfile")
                        .file(mockImage))
                .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType());
    }
    @Test
    void uploadImageToProfile_test_failure2() throws Exception {

        byte[] mockImageBytes = new byte[20];
        MockMultipartFile mockImage = new MockMultipartFile("image","test.png","image/png",mockImageBytes);

        doThrow(new RuntimeException()).when(userService).addImageToProfile(any(MultipartFile.class));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/uploadImageToProfile")
                        .file(mockImage))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
    @Test
    void downloadImage_test_success() throws Exception {

        Integer id = 1;
        byte[] mockImageBytes = new byte[20];

        when(userService.getUserImage(any(Integer.class))).thenReturn(mockImageBytes);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/downloadImage/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().bytes(mockImageBytes));
    }

    @Test
    void downloadImage_test_failure() throws Exception {

        Integer id = 1;

        when(userService.getUserImage(any(Integer.class))).thenThrow(new NullPointerException());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/downloadImage/{id}",id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    void showUsers_test_success() throws Exception {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setName(signUpRequest.getName());

        List<UserResponse> userResponses = new ArrayList<>();
        userResponses.add(userResponse);

        when(userService.showUsers()).thenReturn(CompletableFuture.completedFuture(userResponses));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/showUsers"));
        verify(userService).showUsers();
    }
}

