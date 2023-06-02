package com.maxprojects.maxmessenger.controllers;

import com.maxprojects.maxmessenger.Entities.Chat;
import com.maxprojects.maxmessenger.services.ChatService;
import com.maxprojects.maxmessenger.services.JwtService;
import com.maxprojects.maxmessenger.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {
    private final ChatService chatService;
    private final JwtService jwtService;
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<Chat>> getChats(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        token = token.substring(7);
        return ResponseEntity.ok(userService.getChats(jwtService.extractLogin(token)));
    }

    @PostMapping(value = "/CreateChat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createChat(
            @RequestParam String login,
            @RequestBody List<String> participants
            ){
        chatService.createChat(login, participants);
        return ResponseEntity.ok("Created");
    }

    @PostMapping("/chat")
    public void send(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                     @RequestParam(name = "sel") String chatId,
                     @RequestBody String text){
        token = token.substring(7);
        chatService.sendToChat(Long.parseLong(chatId), jwtService.extractLogin(token), text);

    }
}
