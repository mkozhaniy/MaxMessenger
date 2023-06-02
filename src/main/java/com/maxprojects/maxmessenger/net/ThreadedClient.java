package com.maxprojects.maxmessenger.net;

import com.maxprojects.maxmessenger.Entities.MessageDTO;
import com.maxprojects.maxmessenger.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

@RequiredArgsConstructor
public class ThreadedClient implements MessageListener{
    public static final int PORT = 19000;
    public static final String HOST = "localhost";
    static Logger log = LoggerFactory.getLogger(ThreadedClient.class);
    ConnectionHandler handler;
    Protocol protocol;
    private final ApplicationContext context;

    public ThreadedClient(Protocol protocol, ApplicationContext context){
        this.protocol = protocol;
        this.context = context;
        try {
            Socket socket = new Socket(HOST, PORT);
            handler = new SocketConnectionHandler(protocol, socket);
            handler.addListener(this);
            Thread socketThread = new Thread(handler);
            socketThread.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean send(MessageDTO message){
        try {
            handler.send(message);
            return true;
        } catch (IOException e){
            log.info("failed sending message: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void onMessage(MessageDTO message){
        context.getBean(ChatService.class).onMessage(message);
    }
}
