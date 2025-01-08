package com.example.ChatApp.repository;

import com.example.ChatApp.entity.SessionEntity;
import com.example.ChatApp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity,Long> {


    List<SessionEntity> findByUser(UserEntity userDetail);


    void deleteByRefreshToken(String token);
}
