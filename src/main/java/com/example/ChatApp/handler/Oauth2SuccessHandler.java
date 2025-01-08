package com.example.ChatApp.handler;


import com.example.ChatApp.entity.UserEntity;
import com.example.ChatApp.service.AuthService;
import com.example.ChatApp.service.JwtTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static com.example.ChatApp.enums.Roles.USER;


@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;
    private final JwtTokenService jwtTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauth2Token=(OAuth2AuthenticationToken) authentication;

         OAuth2User authuser= oauth2Token.getPrincipal();

         String email=authuser.getAttribute("email");
         UserEntity user=authService.findByEmail(email);
         if(user==null){
             UserEntity data= UserEntity.builder().email(email).username(authuser.getAttribute("name")).role(List.of(USER))
             .build();
             user=authService.createUser(data);
         }
        String accessToken=jwtTokenService.generateAccessToken(user);
        String refreshToken=jwtTokenService.generateRefreshToken(user);

        Cookie cookie=new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        String frontEndUrl="http://localhost:8080/home.html?token="+accessToken;
        getRedirectStrategy().sendRedirect(request,response,frontEndUrl);

        log.info("handler -? "+user.toString());
    }
}
