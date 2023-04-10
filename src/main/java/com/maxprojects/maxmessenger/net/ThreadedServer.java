package com.maxprojects.maxmessenger.net;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedServer {
    private static final int PORT = 19000;
    private volatile boolean isRunning;
    static Logger log = LoggerFactory.getLogger(ThreadedServer.class);
    private ServerSocket sSocket;
    private Protocol protocol;

    public ThreadedServer(Protocol protocol){
        try{
            this.protocol = protocol;
            sSocket = new ServerSocket(PORT);
            sSocket.setReuseAddress(true);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() throws Exception{
        ThreadedServer ts = new ThreadedServer(new CustomProtocol());
        ts.startServer();
    }

    private void startServer() throws Exception{
        log.info("Server started");

        isRunning = true;
        while(isRunning){
            Socket socket = sSocket.accept();
            log.info("Accepted " + socket.getInetAddress());
            ConnectionHandler handler = new SocketConnectionHandler(protocol, socket);
            Thread thread = new Thread(handler);
            thread.start();
        }
    }
}
