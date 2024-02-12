package com.adoumadje.chatapp.repository;

import com.adoumadje.chatapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByEmail(String email);
    User findOneByToken(String token);
    List<User> findByIdNot(Long id);
}
