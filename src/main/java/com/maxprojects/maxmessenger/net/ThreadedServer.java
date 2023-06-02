package com.maxprojects.maxmessenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

@Component
public class ThreadedServer implements InitializingBean, Runnable {
    private static final int PORT = 19000;
    private volatile boolean isRunning;
    static Logger log = LoggerFactory.getLogger(ThreadedServer.class);
    private ServerSocket sSocket;
    private Protocol protocol;
    private HashMap<Long, ConnectionHandler> chatSockets = new HashMap<>();

    public HashMap<Long, ConnectionHandler> getChatSockets() {
        return chatSockets;
    }

    @Bean
    @Scope("singleton")
    public ThreadedServer threadedServer(){
        return new ThreadedServer();
    }

    @Override
    public void afterPropertiesSet(){
        try{
            this.protocol = new CustomProtocol();
            sSocket = new ServerSocket(PORT);
            sSocket.setReuseAddress(true);
            Thread thread = new Thread(this);
            thread.start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        log.info("Server started");

        isRunning = true;
        while(isRunning){
            try {
                Socket socket = sSocket.accept();
                log.info("Accepted " + socket.getInetAddress());
                ConnectionHandler handler = new SocketConnectionHandler(protocol, socket);
                Thread thread = new Thread(handler);
                thread.start();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
