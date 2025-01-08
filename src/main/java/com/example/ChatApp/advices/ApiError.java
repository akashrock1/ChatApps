package com.example.ChatApp.advices;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiError {

     LocalDateTime currentTime;

     String error;

    ApiError(){
        this.currentTime=LocalDateTime.now();
    }

    ApiError(String error){
        this();
        this.error=error;
    }

}
