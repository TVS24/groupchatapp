package com.chat.chatapp.config;

import com.chat.chatapp.chat.ActiveUsers;
import com.chat.chatapp.chat.ChatMessage;
import com.chat.chatapp.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final ActiveUsers activeUsers;
    private final SimpMessageSendingOperations messageTemplate;


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if(username != null){
            log.info("User disconnected:{}", username);
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();

            messageTemplate.convertAndSend("/topic/public",chatMessage);
            //tracking of active users
            activeUsers.removeUser(username);
            System.out.println("USER DISCONNECTED : " + username);

            ChatMessage countUpdate = ChatMessage.builder()
                                    .type(MessageType.USER_COUNT_UPDATE)
                                    .userCount(activeUsers.getUsers().size())
                                    .build();
            messageTemplate.convertAndSend("/topic/public",countUpdate);

        }
    }
}
