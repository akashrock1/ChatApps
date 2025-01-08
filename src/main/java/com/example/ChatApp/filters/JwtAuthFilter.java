package com.example.ChatApp.filters;


import com.example.ChatApp.entity.UserEntity;
import com.example.ChatApp.repository.AuthRepository;
import com.example.ChatApp.repository.SessionRepository;
import com.example.ChatApp.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private SessionRepository sessionRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization=request.getHeader("Authorization");
        if(authorization==null){
            filterChain.doFilter(request,response);
            return;
        }

        String authorizationToken=authorization.split("Bearer ")[1];

        Long userId=jwtTokenService.getIdFromToken(authorizationToken);

        if(userId!=null || SecurityContextHolder.getContext().getAuthentication()==null){
            UserEntity user=authRepository.findById(userId).orElse(null);

            UsernamePasswordAuthenticationToken authenticationToken=
                    new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());

          //  sessionRepository.existsBy()

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);



        }
        filterChain.doFilter(request,response);

    }
}
