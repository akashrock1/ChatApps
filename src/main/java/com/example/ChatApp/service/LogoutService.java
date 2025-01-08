package com.example.ChatApp.service;

import com.example.ChatApp.repository.SessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {

@Autowired
private final SessionRepository sessionRepository;

public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){

    String Token=request.getHeader("Authorization").split("Bearer ")[1];

    sessionRepository.deleteByRefreshToken(Token);

    return new ResponseEntity<>("Successfully Logout", HttpStatus.OK);

}


}
