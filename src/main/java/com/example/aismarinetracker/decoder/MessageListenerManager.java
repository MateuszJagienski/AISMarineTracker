package com.example.aismarinetracker.decoder;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class MessageListenerManager {
    private final int port = 12346;
    private final List<MessageListener> messageListeners = new CopyOnWriteArrayList<>();
    private DatagramSocket socket;
    private Thread udpThread;
    private static final Logger logger = Logger.getLogger(MessageListenerManager.class.getName());


    public void startListening() {
        if (udpThread != null && udpThread.isAlive()) return;
       udpThread = new Thread(() -> {
           try {
               // Creating a UDP socket on the specified port
               socket = new DatagramSocket(port);

               logger.info("Listening on port: " + socket.getLocalPort());
               while (!socket.isClosed()) {
                   // Buffer to receive incoming data
                   byte[] receiveData = new byte[1024];

                   // Creating a packet to receive data
                   DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                   // Receiving data
                   socket.receive(receivePacket);

                   // Converting received data to a string
                   String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                   messageListeners.forEach(messageListener -> messageListener.onMessageReceived(receivedMessage));

                   logger.info("Received message: " + receivedMessage);
               }
           } catch (IOException e) {
               logger.info("Inside: " + getClass() + "Exception in: " + e.getClass().getName());
           }
       });
       udpThread.start();
    }

    public void addMessageListener(MessageListener messageListener) {
        synchronized (messageListeners) {
            messageListeners.add(messageListener);
            startListeningIfNeeded();
        }
    }

    public void removeMessageListener(MessageListener messageListener) {
        synchronized (messageListeners) {
            messageListeners.remove(messageListener);
            closeSocketIfNoListeners();
        }
    }

    private void startListeningIfNeeded() {
        if (messageListeners.isEmpty()) return;
        startListening();
    }

    private void closeSocketIfNoListeners() {
        if (messageListeners.isEmpty() && socket != null && !socket.isClosed())
            socket.close();
    }

}