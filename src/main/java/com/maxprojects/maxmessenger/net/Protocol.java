package com.maxprojects.maxmessenger.net;

import com.maxprojects.maxmessenger.Entities.MessageDTO;

import java.sql.SQLException;

public interface Protocol {
    MessageDTO decode(byte[] bytes);

    byte[] encode(MessageDTO message);
}
