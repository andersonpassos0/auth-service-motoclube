package com.motoclub.authservice.infrastructure.security;

import com.motoclub.authservice.infrastructure.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

        @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getRequestURI();

        boolean shouldSkip =  path.startsWith("/auth")
                                || path.startsWith("/v3/api-docs")
                                || path.startsWith("/swagger-ui")
                                || path.startsWith("/swagger-ui/index.html")
                                || path.startsWith("/swagger-resources")
                                || path.startsWith("/webjars")
                                || path.startsWith("/h2-console")
                                || path.equals("/")
                                || path.contains("favicon.ico");

        return shouldSkip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String path = request.getRequestURI();

        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String email;

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

//        if (authHeader.length() <= 7){
//            filterChain.doFilter(request, response);
//            return;
//        }

        token = authHeader.substring(7);
        email = jwtService.extractEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            userRepository.findByEmail(email).ifPresent(user -> {
                if (jwtService.isTokenValid(token, user)) {
                    UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                            user.getEmail(),
                            user.getPassword(),
                            new ArrayList<>());

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            });
        }

        filterChain.doFilter(request, response);
        }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }

}
