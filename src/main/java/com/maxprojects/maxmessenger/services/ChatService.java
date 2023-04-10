package com.maxprojects.maxmessenger.services;


import com.maxprojects.maxmessenger.Entities.Chat;
import com.maxprojects.maxmessenger.Entities.Message;
import com.maxprojects.maxmessenger.Entities.MessageDTO;
import com.maxprojects.maxmessenger.Entities.User;
import com.maxprojects.maxmessenger.net.Protocol;
import com.maxprojects.maxmessenger.net.ThreadedClient;
import com.maxprojects.maxmessenger.repository.ChatRepository;
import com.maxprojects.maxmessenger.repository.MessageRepository;
import com.maxprojects.maxmessenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private static Map<Long, ThreadedClient> chatHandlers= new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(ChatService.class);
    private final ApplicationContext context;

    public Chat createChat(List<String> participants){
        var chat =  Chat.builder()
                .admin(userRepository
                        .findByLogin(participants.get(0))
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")))
                .lastMessage(null)
                .users(userRepository.
                        findByLogins(participants.subList(1, participants.size())))
                .build();
        chatRepository.save(chat);
        chatHandlers.put(chat.getId(),
                new ThreadedClient((Protocol) context.getBean("protocol"), context));
        return chat;
    }

    public boolean sendToChat(Message message, Chat chat){
        return chatHandlers.get(chat.getId()).send(MessageDTO.builder()
                        .chat(chat.getId())
                        .created(message.getCreated())
                        .sender(message.getSender().getId())
                        .text(message.getText())
                .build());
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
            chatRepository.save(chat);
            messageRepository.save(message);

        } catch (SQLException e){
            log.info(e.getMessage());
        }
    }
}
