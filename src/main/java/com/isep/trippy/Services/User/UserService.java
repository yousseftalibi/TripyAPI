package com.isep.trippy.Services.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.isep.trippy.Models.ChatMessage;
import com.isep.trippy.Models.User;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.isep.trippy.Repositories.UserRepository;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService extends TextWebSocketHandler {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    Connection connection;
    /*@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
*/
    private final List<WebSocketSession> sessions = new ArrayList<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        sessions.add(session);
    }
   @Override
    public void handleTextMessage(WebSocketSession session, @NotNull TextMessage message) {
        String payload = message.getPayload();
        var chatMessage = ChatMessage.builder().id(session.getId()).message(payload).build();
       ObjectMapper myob = new JsonMapper();
       var openedSessions = sessions.stream().filter(s -> s.isOpen()).collect(Collectors.toList());
       openedSessions.forEach(s -> {
           try {
               s.sendMessage(new TextMessage(myob.writeValueAsString(chatMessage)));
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       });


        /*ChatMessage mymessage = new ObjectMapper().readValue(message.getPayload(), ChatMessage.class);
       Map<String, String> chatMessage = new HashMap<>();
       chatMessage.put("sender", mymessage.getId());
       chatMessage.put("message", mymessage.getMessage());
       ObjectMapper myob = new JsonMapper();
       sessions.forEach(sess -> {
           try {
               sess.sendMessage(new TextMessage(myob.writeValueAsString(chatMessage)));
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       });*/
        //kafkaTemplate.send("chat-messages", mymessage.getId()+": "+mymessage.getMessage());
    }

    public Boolean registerUser(User user) throws SQLException {
        userRepository.registerUser(user);
        return userRepository.getUserById(user.getId()).isPresent();
    }

    public Boolean loginUser(User user) throws SQLException {
            var existingUser = userRepository.getUserById(user.getId());
            return existingUser.isPresent() && (passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword()));
    }

    public boolean usernameTaken(String username) throws SQLException {
        return userRepository.usernameAlreadyTaken(username) ;
    }

    public User getUser(int userId) throws SQLException {
        User currentUser = new User();

        String getUserQuery = "SELECT * FROM users WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(getUserQuery);
        statement.setInt(1, userId);
        ResultSet result =  statement.executeQuery();
        while (result.next()) {
            currentUser.setId(result.getInt("id"));
            currentUser.setUsername(result.getString("username"));
            currentUser.setPassword(result.getString("password"));
            currentUser.setPlaces(String.valueOf(result.getArray("places")));
        }
        return currentUser;
    }
/*
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
*/
    //@Scheduled(cron = "*/10 * * * * *")
     /*
    public void helloWorld(){
        //when travel date - current date == 2 (2 days before the travel day), send a notification using firebase cloud messaging
        //send e-mail to user
        System.out.println("Hello world");
    }
    */

}
