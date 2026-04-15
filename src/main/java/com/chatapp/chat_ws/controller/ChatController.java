package com.chatapp.chat_ws.controller;

import com.chatapp.chat_ws.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Save username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        
        chatMessage.setTimestamp(LocalDateTime.now().format(formatter));
        logger.info("[{}] Message received from {}: {}", chatMessage.getTimestamp(), chatMessage.getSender(), chatMessage.getContent());
        return chatMessage;
    }
}
