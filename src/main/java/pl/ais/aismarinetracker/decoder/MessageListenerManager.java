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
    private AisProvider aisProvider;
    private static final Logger logger = Logger.getLogger(MessageListenerManager.class.getName());

    public enum Provider {
        UDP, TCP;
    }
    private Provider provider = Provider.UDP;
    private void startListening() {
        CompletableFuture.runAsync(this::run);
    }

    private void run() {
        logger.info("Listening on: " + address + ":" + port + " " + provider);
        try {
            aisProvider = provider.equals(Provider.TCP) ? new TcpClient() : new UdpClient();
            aisProvider.open(address, port);

            while (!aisProvider.isClosed()) {
                String receivedMessage = aisProvider.read();
                messageListeners.forEach(messageListener -> messageListener.onMessageReceived(receivedMessage));
            }
        } catch (IOException e) {
            logger.info("Inside: " + getClass() + "Exception in: " + e.getClass().getName());
        }
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
        if (messageListeners.isEmpty() && aisProvider != null && !aisProvider.isClosed()) {
            try {
                aisProvider.close();
            } catch (IOException e) {
                logger.info("Inside: " + getClass() + "Exception in: " + e.getClass().getName());
            }
        }
    }

    public void changeSource(String address, int port, Provider provider) {
        if (!checkConnectionDetails(address, port, provider)) {
            return;
        }
        if (provider.equals(Provider.UDP)) {
            this.provider = Provider.UDP;
            this.address = address;
            this.port = port;
            closeSocketIfNoListeners();
        }
        if (provider.equals(Provider.TCP)) {
            this.provider = Provider.TCP;
            this.address = address;
            this.port = port;
            closeSocketIfNoListeners();
        }
    }

    private boolean checkConnectionDetails(String address, int port, Provider provider) {
        if (address == null || address.isEmpty()) {
            logger.info("Invalid address");
            return false;
        }

        if (port <= 0 || port > 65535) {
            logger.info("Invalid port number");
            return false;
        }

        if (provider == null) {
            logger.info("Invalid provider. Should be TCP or UDP");
            return false;
        }
        return true;
    }

}