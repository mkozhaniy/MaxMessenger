package com.maxprojects.maxmessenger.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "chats")
public class Chat {
    private static final String SEQ_NAME = "chat_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;
    @OneToMany(mappedBy = "chat")
    private List<Message> messages;
    @ManyToMany(mappedBy = "chatList")
    private List<User> users;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;
    @OneToOne
    @JoinColumn(name = "message_id")
    private Message lastMessage;

}
