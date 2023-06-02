package com.maxprojects.maxmessenger.services;


import com.maxprojects.maxmessenger.Entities.Chat;
import com.maxprojects.maxmessenger.Entities.Message;
import com.maxprojects.maxmessenger.Entities.MessageDTO;
import com.maxprojects.maxmessenger.Entities.User;
import com.maxprojects.maxmessenger.net.CustomProtocol;
import com.maxprojects.maxmessenger.net.ThreadedClient;
import com.maxprojects.maxmessenger.net.ThreadedServer;
import com.maxprojects.maxmessenger.repository.ChatRepository;
import com.maxprojects.maxmessenger.repository.MessageRepository;
import com.maxprojects.maxmessenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final static Logger log = LoggerFactory.getLogger(ChatService.class);
    private final ApplicationContext context;
    private final ThreadedServer threadedServer;


    public Chat createChat(String admin, List<String> participants){
        User adminUser = userRepository.findByLogin(admin)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<User> partUsers;
        partUsers = participants.stream().map(login ->
                userRepository
                        .findByLogin(login)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")))
                .collect(Collectors.toList());
        partUsers.add(adminUser);

        var chat =  Chat.builder()
                .admin(adminUser)
                .lastMessage(null)
                .users(partUsers)
                .build();

        partUsers.forEach(user -> user.getChatList().add(chat));
        chatRepository.save(chat);
        new ThreadedClient(new CustomProtocol(), context);
        return chat;
    }

    public void sendToChat(Long chatId,String login,String text) {
        try {
            threadedServer.getChatSockets()
                    .get(chatId)
                    .send(MessageDTO.builder()
                            .text(text)
                            .chat(chatId)
                            .sender(userRepository.findByLogin(login)
                                    .orElseThrow(() -> new UsernameNotFoundException("not found user"))
                                    .getId())
                            .created(LocalDateTime.now())
                            .build());
        } catch (IOException e){
            log.info(e.getMessage());
        }
    }

    public void onMessage(MessageDTO messageDTO){
        try{
            Chat chat = chatRepository.findById(messageDTO.getChat())
                    .orElseThrow(() -> new SQLException("not found chat"));
            User user = userRepository.findById(messageDTO.getSender())
                    .orElseThrow(() -> new SQLException("not found user"));

            Message message = Message.builder()
                    .chat(chat)
                    .sender(user)
                    .created(messageDTO.getCreated())
                    .text(messageDTO.getText())
                    .build();
            chat.setLastMessage(message);
            messageRepository.save(message);
        } catch (SQLException e){
            log.info(e.getMessage());
        }
    }
}
