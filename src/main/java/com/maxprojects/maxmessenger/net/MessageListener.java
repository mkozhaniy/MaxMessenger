package com.maxprojects.maxmessenger.net;

import com.maxprojects.maxmessenger.Entities.MessageDTO;

public interface MessageListener {
    void onMessage(MessageDTO message);
}
