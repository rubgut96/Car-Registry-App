package com.rgutierrez.carregistry.userservice.converters;

import com.rgutierrez.carregistry.controller.dtos.UserResponse;
import com.rgutierrez.carregistry.repository.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserConverterTest {

    @InjectMocks
    private UserConverter userConverter;

    @Test
    void toResponse_test() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setName("a");
        userEntity.setMail("b");
        userEntity.setPassword("password");
        userEntity.setRole("ROLE_VENDOR");
        userEntity.setImage(null);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setName("a");

        UserResponse result = userConverter.toResponse(userEntity);

        assertEquals(userResponse.getId(),result.getId());
        assertEquals(userResponse.getName(),result.getName());
    }
}