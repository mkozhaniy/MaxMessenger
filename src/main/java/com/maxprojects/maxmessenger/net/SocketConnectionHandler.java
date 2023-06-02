package com.maxprojects.maxmessenger.net;

import com.maxprojects.maxmessenger.Entities.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SocketConnectionHandler implements ConnectionHandler{
    static Logger log = LoggerFactory.getLogger(SocketConnectionHandler.class);
    private final List<MessageListener> listeners = new ArrayList<>();
    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    private final Protocol protocol;

    public SocketConnectionHandler(Protocol protocol, Socket socket) throws IOException {
        this.protocol = protocol;
        this.socket = socket;
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    @Override
    public void send(MessageDTO msg) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug(msg.toString());
        }

        out.write(protocol.encode(msg));
        out.flush();
    }


    @Override
    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }



    public void notifyListeners(MessageDTO msg) {
        listeners.forEach(it -> it.onMessage(msg));
    }

    @Override
    public void run() {
        final byte[] buf = new byte[1024 * 64];
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int read = in.read(buf);
                if (read > 0) {
                    MessageDTO msg = protocol.decode(Arrays.copyOf(buf, read));
                    notifyListeners(msg);
                }
            } catch (Exception e) {
                log.error("Failed to handle connection: {}", e);
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void stop() {
        Thread.currentThread().interrupt();
    }
}
