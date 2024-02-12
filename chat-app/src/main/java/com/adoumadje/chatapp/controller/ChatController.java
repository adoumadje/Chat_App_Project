package com.adoumadje.chatapp.controller;

import com.adoumadje.chatapp.model.MessageModel;
import com.adoumadje.chatapp.model.MessageType;
import com.adoumadje.chatapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;

    @Autowired
    public ChatController(SimpMessagingTemplate simpMessagingTemplate,
                          MessageService messageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/public-message")
    @SendTo("/chatroom/public")
    public MessageModel receivePublicMessage(@Payload MessageModel message) {
        if(message.getMessageType() == MessageType.MESSAGE) {
            messageService.savePublicMessage(message);
        }
        return message;
    }

    @MessageMapping("/private-message")
    public MessageModel receivePrivateMessage(@Payload MessageModel message) {
        if(message.getMessageType() == MessageType.MESSAGE) {
            messageService.savePrivateMessage(message);
        }
        simpMessagingTemplate.convertAndSendToUser(
                message.getReceiverId().toString(), "/private", message
        );
        return message;
    }
}
