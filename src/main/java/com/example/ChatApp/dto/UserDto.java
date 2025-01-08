package com.example.ChatApp.dto;


import com.example.ChatApp.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {

    private Long id;

    private String username;

    private String email;

    private String password;

    private List<Roles> role;


}
