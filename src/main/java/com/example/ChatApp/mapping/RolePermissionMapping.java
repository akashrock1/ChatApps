package com.example.ChatApp.mapping;

import com.example.ChatApp.enums.Permisssions;
import com.example.ChatApp.enums.Roles;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.ChatApp.enums.Permisssions.CREATE_POST;
import static com.example.ChatApp.enums.Permisssions.VIEW_POST;
import static com.example.ChatApp.enums.Roles.ADMIN;
import static com.example.ChatApp.enums.Roles.USER;

public class RolePermissionMapping {

    private static final Map<Roles, Set<Permisssions>> mapping= Map.of(
      ADMIN,Set.of(CREATE_POST,VIEW_POST),
      USER,Set.of(VIEW_POST)
    );

    public static Set<SimpleGrantedAuthority> getRolesMapping(List<Roles> roles){
        Set<SimpleGrantedAuthority> lis=new HashSet<>();
        roles.forEach(role->
                {
                    lis.addAll(mapping.get(role).stream().map(permission ->
                                    new SimpleGrantedAuthority(permission.toString()))
                            .collect(Collectors.toSet()));
                    lis.add(new SimpleGrantedAuthority("ROLE_"+role));

                }


                );
        return lis;
    }

}
