package com.rgutierrez.carregistry.userservice.converters;

import com.rgutierrez.carregistry.controller.dtos.UserResponse;
import com.rgutierrez.carregistry.repository.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public UserResponse toResponse(UserEntity userEntity){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userEntity.getId());
        userResponse.setName(userEntity.getName());
        return userResponse;
    }
}
