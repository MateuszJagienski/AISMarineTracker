package com.example.aismarinetracker.decoder;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class AisSocket {
    private final int port = 12346;
    private final String host = "127.0.0.1";
    private final ArrayList<MessageListener> messageListeners = new ArrayList<>();

    private Thread udpThread;
    public void run() {
       udpThread = new Thread(() -> {
           try {
               // Creating a UDP socket on the specified port
               DatagramSocket socket = new DatagramSocket(port);

               System.out.println("Listening on port: " + socket.getLocalPort());
               while (true) {
                   // Buffer to receive incoming data
                   byte[] receiveData = new byte[1024];

                   // Creating a packet to receive data
                   DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                   // Receiving data
                   socket.receive(receivePacket);

                   // Converting received data to a string
                   String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                   messageListeners.forEach(messageListener -> messageListener.onMessageReceived(receivedMessage));

                   System.out.println("Received message: " + receivedMessage);
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
       });
       udpThread.start();
    }

    public void addMessageListener(MessageListener messageListener) {
        if (messageListeners.isEmpty())
            run();

        messageListeners.add(messageListener);
    }

    public void removeMessageListener(MessageListener messageListener) {
        messageListeners.remove(messageListener);
        if (messageListeners.isEmpty())
            udpThread.interrupt();
    }

}

