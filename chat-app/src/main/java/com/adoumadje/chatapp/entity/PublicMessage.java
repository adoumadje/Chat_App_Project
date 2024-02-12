package com.adoumadje.chatapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "public_messages")
public class PublicMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;
    @Column(name = "message")
    private String message;

    public PublicMessage() {
    }

    public PublicMessage(User sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
