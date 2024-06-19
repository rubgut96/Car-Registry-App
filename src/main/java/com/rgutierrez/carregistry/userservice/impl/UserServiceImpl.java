package com.rgutierrez.carregistry.userservice.impl;

import com.rgutierrez.carregistry.controller.dtos.UserResponse;
import com.rgutierrez.carregistry.repository.UserRepository;
import com.rgutierrez.carregistry.repository.entities.UserEntity;
import com.rgutierrez.carregistry.userservice.UserService;
import com.rgutierrez.carregistry.userservice.converters.UserConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    @Override
    public UserEntity save(UserEntity user){
        return userRepository.save(user);
    }
    @Override
    public void addImageToProfile(MultipartFile image) throws IOException {
        //Retrieve UserDetails from the Security Context.
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity userEntity = userRepository.findByMail(userDetails.getUsername())
                .orElseThrow(() -> new NullPointerException("User not found."));
        //Encode and upload the image.
        userEntity.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
        userRepository.save(userEntity);
    }
    @Override
    public byte[] getUserImage(Integer userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NullPointerException("User not found with userId: " + userId));

        return Base64.getDecoder().decode(user.getImage());
    }
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByMail(mail);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + mail);
        }
        return new org.springframework.security.core.userdetails.User(
                user.get().getUsername(),
                user.get().getPassword(),
                user.get().isEnabled(),
                user.get().isAccountNonExpired(),
                user.get().isCredentialsNonExpired(),
                user.get().isAccountNonLocked(),
                user.get().getAuthorities()
        );
    }
    @Override
    @Async
    public CompletableFuture<List<UserResponse>> showUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return CompletableFuture.completedFuture(userEntities.stream()
                .map(userConverter::toResponse)
                .toList());
    }
}
