package com.adoumadje.chatapp.controller;

import com.adoumadje.chatapp.model.MessageModel;
import com.adoumadje.chatapp.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatRestController {
    private final MessageService messageService;

    public ChatRestController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/get-all-chatroom-messages")
    public List<MessageModel> getAllChatRoomMessages() {
        return messageService.getAllChatRoomMessages();
    }

    @GetMapping("get-user-private-messages")
    public List<MessageModel> getUserPrivateMessages(@RequestParam("userId") Long userId) {
        return messageService.getUserPrivateMessages(userId);
    }
}
