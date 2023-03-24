package com.maxprojects.maxmessenger.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue
    private Long id;
    private String text;
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat_id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender_id;
    @CreationTimestamp
    private LocalDateTime created;
}
