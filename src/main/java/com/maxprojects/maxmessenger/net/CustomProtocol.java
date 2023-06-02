package com.maxprojects.maxmessenger.net;

import com.maxprojects.maxmessenger.Entities.MessageDTO;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@NoArgsConstructor
@Component
public class CustomProtocol implements Protocol{
    static Logger log = LoggerFactory.getLogger(CustomProtocol.class);
    private static final String DELIMITER = ";";

    @Override
    public MessageDTO decode(byte[] bytes){
        String str = new String(bytes);
        String[] tokens = str.split(DELIMITER);
        return MessageDTO.builder()
                .text(tokens[0])
                .chat(Long.parseLong(tokens[1]))
                .created(LocalDateTime.parse(tokens[2]))
                .sender(Long.parseLong(tokens[3]))
                .build();
    }

    @Override
    public byte[] encode(MessageDTO message){
        StringBuilder builder = new StringBuilder();
        return builder
                .append(message.getText().replaceAll(";", "\\"))
                .append(DELIMITER)
                .append(message.getChat())
                .append(DELIMITER)
                .append(message.getCreated())
                .append(DELIMITER)
                .append(message.getSender())
                .toString().getBytes(StandardCharsets.UTF_8);
    }
}
