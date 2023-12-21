package pl.ais.aismarinetracker.decoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

@Component
public class MessageListenerManager {

    @Value("${port}")
    private int port;
    @Value("${address}")
    private String address;
    private final List<MessageListener> messageListeners = new CopyOnWriteArrayList<>();
    private DatagramSocket socket;
    private Thread udpThread;
    private static final Logger logger = Logger.getLogger(MessageListenerManager.class.getName());


    public void startListening() {
        logger.info("Start listening on address: " + address + " port: " + port);
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            logger.warning(e.getClass().getName());
        }
        InetAddress finalInetAddress = inetAddress;
        logger.info("InetAddress: " + (finalInetAddress != null ? finalInetAddress.getHostAddress() : null));
        CompletableFuture.runAsync(() -> {
           try {
               // Creating a UDP socket on the specified port
               socket = new DatagramSocket(port);

               logger.info("Listening on port: " + socket.getLocalPort());
               while (!socket.isClosed()) {
                   // Buffer to receive incoming data
                   byte[] receiveData = new byte[1024];

                   // Creating a packet to receive data
                   DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length, finalInetAddress, port);

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