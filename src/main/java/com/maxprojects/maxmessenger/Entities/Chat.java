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

    @Id
    @GeneratedValue
    private Long id;
    @OneToMany
    @JoinColumn(name = "message_id")
    private List<Message> messages;
    @ManyToMany(mappedBy = "chats")
    private List<User> users;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User admin;

}
