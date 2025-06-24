package com.chat.chatapp.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private ActiveUsers activeUsers;

    public ChatController(ActiveUsers activeUsers){
        this.activeUsers = activeUsers;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){

        return chatMessage;
    }


    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor){

        String userName = chatMessage.getSender();
        //activeUsers.addUser(userName);
        //Add username to the WebSocket Session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        //return chatMessage;


        //adding user to the users map;
        activeUsers.addUser(userName);
        return ChatMessage.builder()
                        .type(MessageType.JOIN)
                        .sender(userName)
                        .userCount(activeUsers.getUsers().size())
                        .build();
        
    }




}
