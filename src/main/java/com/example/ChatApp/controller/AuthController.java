package com.example.ChatApp.controller;


import com.example.ChatApp.dto.JwtResponseToken;
import com.example.ChatApp.dto.LoginDto;
import com.example.ChatApp.dto.UserDto;
import com.example.ChatApp.service.AuthService;
import com.example.ChatApp.service.LoginService;
import com.example.ChatApp.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final LoginService loginService;

    private final LogoutService logoutService;

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody UserDto body){
        return authService.signUp(body);

    }

    @PostMapping("/login")
    public JwtResponseToken login(@RequestBody LoginDto login, HttpServletRequest request, HttpServletResponse response){
        return loginService.login(login,request,response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request,HttpServletResponse response){
        return logoutService.logout(request,response);
    }

    @PostMapping("/refresh")
    public JwtResponseToken refresh(HttpServletRequest request){
        return loginService.refresh(request);
    }

}
