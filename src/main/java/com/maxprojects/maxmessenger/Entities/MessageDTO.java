package com.maxprojects.maxmessenger.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class MessageDTO {
    private Long chat;
    private LocalDateTime created;
    private Long sender;
    private String text;
}
