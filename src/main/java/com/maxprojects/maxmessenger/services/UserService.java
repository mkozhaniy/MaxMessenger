package com.maxprojects.maxmessenger.services;

import com.maxprojects.maxmessenger.Entities.Chat;
import com.maxprojects.maxmessenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<Chat> getChats(String login){
        var user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getChatList();
    }
}
