package com.maxprojects.maxmessenger.net;

import com.maxprojects.maxmessenger.Entities.Message;
import com.maxprojects.maxmessenger.Entities.MessageDTO;

import java.io.IOException;

public interface ConnectionHandler extends Runnable{
    void send(MessageDTO msg) throws IOException;

    void addListener(MessageListener listener);

    void stop();
}
