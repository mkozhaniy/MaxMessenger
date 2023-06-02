package com.maxprojects.maxmessenger.net;

import com.maxprojects.maxmessenger.Entities.MessageDTO;

public interface Protocol {
    MessageDTO decode(byte[] bytes);

    byte[] encode(MessageDTO message);
}
