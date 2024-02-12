package com.adoumadje.chatapp.service;

import com.adoumadje.chatapp.model.MessageModel;

import java.util.List;

public interface MessageService {
    void savePublicMessage(MessageModel message);

    List<MessageModel> getAllChatRoomMessages();

    void savePrivateMessage(MessageModel message);

    List<MessageModel> getUserPrivateMessages(Long userId);
}
