package com.adoumadje.chatapp.repository;

import com.adoumadje.chatapp.entity.PrivateMessage;
import com.adoumadje.chatapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    List<PrivateMessage> findBySenderOrReceiver(User sender, User receiver);
}
