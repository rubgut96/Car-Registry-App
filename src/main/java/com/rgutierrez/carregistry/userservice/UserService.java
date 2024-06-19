package com.rgutierrez.carregistry.userservice;

import com.rgutierrez.carregistry.controller.dtos.UserResponse;
import com.rgutierrez.carregistry.repository.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService extends UserDetailsService{
     UserEntity save(UserEntity user);
     void addImageToProfile (MultipartFile image) throws IOException;
     byte[] getUserImage(Integer id);
     UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException;
     CompletableFuture<List<UserResponse>> showUsers();
}
