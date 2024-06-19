package com.rgutierrez.carregistry.controller;

import com.rgutierrez.carregistry.authservice.AuthenticationService;
import com.rgutierrez.carregistry.controller.dtos.JwtResponse;
import com.rgutierrez.carregistry.controller.dtos.LoginRequest;
import com.rgutierrez.carregistry.controller.dtos.SignUpRequest;
import com.rgutierrez.carregistry.controller.dtos.UserResponse;
import com.rgutierrez.carregistry.userservice.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/signUp")
    @Operation(description = "Call the authentication service to process the sign-up request.")
    @ApiResponse(responseCode = "200", description = "Successful sign up.")
    @ApiResponse(responseCode = "400", description = "Not valid request.", content = @Content())
    //Call the authentication service to process the sign-up request.
    public ResponseEntity<JwtResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {
            //Return an ok response with the created JWT token.
            return ResponseEntity.ok(authenticationService.signUp(signUpRequest));
        } catch (Exception e) {
            //Return a bad response if any error occurs.
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/login")
    @Operation(description = "Call the authentication service to process the login request")
    @ApiResponse(responseCode = "200", description = "Successful login.")
    @ApiResponse(responseCode = "400", description = "Incorrect mail or password.", content = @Content())
    //Call the authentication service to process the login request.
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            //Return an ok response with the created JWT token.
            return ResponseEntity.ok(authenticationService.login(loginRequest));
        } catch (Exception e) {
            //Return a bad response if any error occurs.
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping(value = "/uploadImageToProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_VENDOR')")
    @Operation(description = "Upload an image to your profile. Only PNG are allowed.  \n"
            +"NECESSARY ROLE: ROLE_CLIENT or ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "Image uploaded.")
    @ApiResponse(responseCode = "415", description = "Only PNG images are allowed.", content = @Content())
    @ApiResponse(responseCode = "500", description = "Failed to upload image.", content = @Content())
    //Upload an image to your profile. Only PNG are allowed.
    public ResponseEntity<String> uploadImageToProfile(@RequestParam(value = "image") MultipartFile image) {
        String contentType = image.getContentType();
        if (!"image/png".equals(contentType)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Only PNG images are allowed.");
        }
        try {
            userService.addImageToProfile(image);
            return ResponseEntity.ok("Image uploaded.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/downloadImage/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_VENDOR')")
    @Operation(description = "Download the user's image with the corresponding id.  \n"
            +"NECESSARY ROLE: ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "Image downloaded successfully.")
    @ApiResponse(responseCode = "404", description = "The user with the specified ID was not found.", content = @Content())
    //Download the user image of the corresponding id.
    public ResponseEntity<byte[]> downloadImage(@PathVariable Integer userId) {
        try {
            byte[] imageBytes = userService.getUserImage(userId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (NullPointerException e) {
            //Return a not found response if the id is not in use.
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/showUsers")
    @PreAuthorize("hasAnyRole('ROLE_VENDOR')")
    @Operation(description = "Shows all users stored in the database.  \n"
            +"NECESSARY ROLE: ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "Successful operation.")
    @ApiResponse(responseCode = "500", description = "Error during the process.", content = @Content())
    //Shows all users stored in the database.
    public CompletableFuture<ResponseEntity<List<UserResponse>>> showUsers(){
        try{
            return userService.showUsers().thenApply(ResponseEntity::ok);
        }
        catch (Exception e){
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }
    }
}

