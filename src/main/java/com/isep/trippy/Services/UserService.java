package com.isep.trippy.Services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.isep.trippy.Models.ChatMessage;
import com.isep.trippy.Models.User;
import jakarta.transaction.Transactional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.isep.trippy.Repositories.UserRepository;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Transactional
public class UserService extends TextWebSocketHandler {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final List<WebSocketSession> sessions = new ArrayList<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session){

        sessions.add(session);
    }
    @Override
    public void handleTextMessage(WebSocketSession session, @NotNull TextMessage message) throws JsonProcessingException {
        ChatMessage mymessage = new ObjectMapper().readValue(message.getPayload(), ChatMessage.class);
        kafkaTemplate.send("chat-messages", mymessage.getId()+": "+mymessage.getMessage());
    }

    public void registerUser(User user)  {
        if(user !=null){
            userRepository.registerUser(user);
        }
    }

    public Boolean loginUser(User user) {
            User existingUser = userRepository.loginUser(user);
            return existingUser!=null && (passwordEncoder.matches(user.getPassword(), existingUser.getPassword()));
    }

    public boolean usernameTaken(String username){
        return userRepository.usernameAlreadyTaken(username) >= 1;
    }

    @KafkaListener(topics= "chat-messages", groupId = "chat-group")
    public void listenChatMessage(@NotNull ConsumerRecord<String, String> record) throws IOException {
        Map<String, String> chatMessage = new HashMap<>();
        chatMessage.put("sender", String.valueOf(record.offset()));
        chatMessage.put("message", record.value());

        ObjectMapper myob = new JsonMapper();

        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage(myob.writeValueAsString(chatMessage)));
        }
    }

    //@Scheduled(cron = "*/10 * * * * *")
     /*
    public void helloWorld(){
        //when travel date - current date == 2 (2 days before the travel day), send a notification using firebase cloud messaging
        //send e-mail to user
        System.out.println("Hello world");
    }
    */

}
