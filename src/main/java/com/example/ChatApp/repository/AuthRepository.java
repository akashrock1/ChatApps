package com.example.ChatApp.repository;


import com.example.ChatApp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;


@Repository
public interface AuthRepository extends JpaRepository<UserEntity,Long> {


    UserDetails findByEmail(String username);

    boolean existsByEmail(String email);
}
