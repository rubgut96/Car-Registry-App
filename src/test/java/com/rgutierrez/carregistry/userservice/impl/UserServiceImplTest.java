package com.rgutierrez.carregistry.userservice.impl;

import com.rgutierrez.carregistry.controller.dtos.UserResponse;
import com.rgutierrez.carregistry.repository.UserRepository;
import com.rgutierrez.carregistry.repository.entities.UserEntity;
import com.rgutierrez.carregistry.userservice.converters.UserConverter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;

    private UserResponse userResponse;
    private UserResponse createUserResponse(){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setName("name");
        return userResponse;
    }
    private UserEntity userEntity;
    private UserEntity createUserEntity(){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userResponse.getId());
        userEntity.setName(userResponse.getName());
        userEntity.setMail("mail");
        userEntity.setPassword("password");
        userEntity.setRole("ROLE_CLIENT");
        userEntity.setImage(null);
        return userEntity;
    }
    private byte[] mockImageBytes;
    private byte[] createMockImageBytes() {
        return new byte[10];
    }
    private MockMultipartFile createMockImage() {
        return new MockMultipartFile("image",mockImageBytes);
    }
    @BeforeEach
    void setUp() {
        userResponse = createUserResponse();
        userEntity = createUserEntity();
        mockImageBytes = createMockImageBytes();
    }
    @Test
    void getUserImage_Test_success() {

        int id = userEntity.getId();
        String encodedImage = "encodedImage";
        userEntity.setImage(encodedImage);

        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));

        byte[] decodedImage = userService.getUserImage(id);

        assertNotNull(decodedImage);
    }
    @Test
    void getUserImage_Test_failure() {

        int id = userEntity.getId();

        assertThrows(RuntimeException.class, () -> userService.getUserImage(id));
    }
    @Test
    void loadUserByUsername_test_success() {

        when(userRepository.findByMail(userEntity.getMail())).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = userService.loadUserByUsername(userEntity.getMail());

        assertNotNull(userDetails);
        assertEquals(userEntity.getUsername(), userDetails.getUsername());
        assertEquals(userEntity.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertEquals("["+userEntity.getRole()+"]", userDetails.getAuthorities().toString());
    }
    @Test
    void loadUserByUsername_test_failure() {

        String invalidMail = "";

        when(userRepository.findByMail(invalidMail)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(invalidMail));
    }
    @Test
    void showUsers_success() throws ExecutionException, InterruptedException {

        List<UserResponse> userResponses = new ArrayList<>();
        userResponses.add(userResponse);

        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(userEntity);

        when(userRepository.findAll()).thenReturn(userEntities);
        when(userConverter.toResponse(userEntity)).thenReturn(userResponse);

        CompletableFuture<List<UserResponse>> resultFuture = userService.showUsers();

        List<UserResponse> result = resultFuture.get();
        assertEquals(userResponses, result);
        assertEquals(userResponses.size(), result.size());
    }
}