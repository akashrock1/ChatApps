package com.example.ChatApp.service;

import com.example.ChatApp.dto.JwtResponseToken;
import com.example.ChatApp.dto.LoginDto;
import com.example.ChatApp.entity.SessionEntity;
import com.example.ChatApp.entity.UserEntity;
import com.example.ChatApp.exception.ResourceNotFoundException;
import com.example.ChatApp.repository.AuthRepository;
import com.example.ChatApp.repository.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenService jwtTokenService;

    private final SessionRepository sessionRepository;

    private final AuthRepository authRepository;

   // private final JwtResponseToken jwtResponseToken;
   private static final Map<String,Long> map=Map.of(
           "Free",3L,
        "Basic",5L,
           "Premium",7L
   );
    @Value("${spring.subscription}")
    private String subscription;

    @Transactional
    public JwtResponseToken login(LoginDto user, HttpServletRequest request, HttpServletResponse response) {
        if(!authRepository.existsByEmail(user.getEmail())){
            throw new ResourceNotFoundException("Email Not Found");
        }
        Authentication authenticate=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword())
        );
        UserEntity userDetail=(UserEntity) authenticate.getPrincipal();

        String accessToken= jwtTokenService.generateAccessToken(userDetail);
        String refreshToken=jwtTokenService.generateRefreshToken(userDetail);

       List<SessionEntity> x= sessionRepository.findByUser(userDetail);
       // System.out.println(" here ,"+map.get(subscription));
// Based on subscription limiting no. of session
     if(x.size()>= map.get(subscription)){
        x.stream().sorted(Comparator.comparing(SessionEntity::getTime));
        String token=x.getFirst().getRefreshToken();
        sessionRepository.deleteByRefreshToken(token);
       }

        sessionRepository.save(
                SessionEntity.builder().refreshToken(refreshToken).user(userDetail).build()
        );

        Cookie cookie=new Cookie("refreshToken",refreshToken);
        cookie.setSecure(true);
        response.addCookie(cookie);


        return new JwtResponseToken(userDetail.getEmail(), accessToken,refreshToken);
    }

    public JwtResponseToken refresh(HttpServletRequest request) {

        String refreshToken= request.getHeader("Authorization").split("Bearer ")[1];

        Long Userid=jwtTokenService.getIdFromToken(refreshToken);
        UserEntity user=authRepository.findById(Userid).orElseThrow(()->new ResourceNotFoundException("Token is incorrect"));

        String acccessToken=jwtTokenService.generateAccessToken(user);

        return new JwtResponseToken(user.getEmail(), acccessToken,refreshToken);
    }
}
