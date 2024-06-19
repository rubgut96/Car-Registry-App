package com.rgutierrez.carregistry.filter;

import com.mysql.cj.util.StringUtils;
import com.rgutierrez.carregistry.jwtservice.JwtService;
import com.rgutierrez.carregistry.userservice.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userMail;

        if(StringUtils.isEmptyOrWhitespaceOnly(authHeader)){
            filterChain.doFilter(request,response);
            return;
        }
        jwt = authHeader.substring(7);//Remove 'Bearer ' prefix.
        log.info("JWT -> {}", jwt);
        userMail=jwtService.extractUserName(jwt);// Extract the user email from the JWT
        if(!StringUtils.isEmptyOrWhitespaceOnly(userMail) && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userService.loadUserByUsername(userMail);
            if(jwtService.isTokenValid(jwt, userDetails)){
                log.info("User - {}",userDetails);
                //Creates an authentication token using the provided user details.
                //This token will be used to verify the user's identity in subsequent requests.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                //Attaches details from the current web request to the authentication token.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //Update the SecurityContextHolder with the new authentication token.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

