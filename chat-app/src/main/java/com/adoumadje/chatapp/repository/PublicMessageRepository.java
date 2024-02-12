package com.adoumadje.chatapp.repository;

import com.adoumadje.chatapp.entity.PublicMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicMessageRepository extends JpaRepository<PublicMessage, Long> {
}
