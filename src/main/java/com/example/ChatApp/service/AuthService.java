package com.example.ChatApp.service;


import com.example.ChatApp.dto.UserDto;
import com.example.ChatApp.entity.UserEntity;
import com.example.ChatApp.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final ModelMapper modelMapper;

    private final AuthRepository authRepository;

    private final PasswordEncoder passwordEncoder;


    public UserDto signUp(UserDto body) {
        UserEntity user=modelMapper.map(body, UserEntity.class);
       user.setPassword(passwordEncoder.encode(user.getPassword()));
        authRepository.save(user);
        return modelMapper.map(user,UserDto.class);

    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authRepository.findByEmail(username);
    }

    public UserEntity findByEmail(String email) {
        return (UserEntity) authRepository.findByEmail(email);
    }

    public UserEntity createUser(UserEntity data) {
        return authRepository.save(data);
    }
}
