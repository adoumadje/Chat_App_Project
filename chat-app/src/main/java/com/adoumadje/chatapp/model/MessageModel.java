package com.adoumadje.chatapp.model;

import java.util.Date;

public class MessageModel {
    private Long senderId;
    private String senderName;
    private String senderInitial;
    private Long receiverId;
    private String message;
    private MessageType messageType;

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderInitial() {
        return senderInitial;
    }

    public void setSenderInitial(String senderInitial) {
        this.senderInitial = senderInitial;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", message='" + message + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
