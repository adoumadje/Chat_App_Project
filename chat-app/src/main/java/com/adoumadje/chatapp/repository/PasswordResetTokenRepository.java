package com.adoumadje.chatapp.repository;

import com.adoumadje.chatapp.entity.PasswordResetToken;
import com.adoumadje.chatapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findOneByUser(User user);
    PasswordResetToken findOneByToken(String token);
}
