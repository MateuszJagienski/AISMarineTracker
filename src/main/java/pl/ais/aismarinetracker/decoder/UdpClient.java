package pl.ais.aismarinetracker.decoder;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class UdpClient implements AisProvider {

    private boolean isClosed = true;
    private DatagramSocket socket;
    private InetAddress finalInetAddress;
    private int port;
    private static final Logger logger = Logger.getLogger(UdpClient.class.getName());

    public void open(String address, int port) throws IOException {
        // if open is called multiple times, we need to close the socket first
        if (!isClosed) {
            close();
        }
        logger.info("Start listening on address: " + address + " port: " + port);
        finalInetAddress = InetAddress.getByName(address);
        logger.info("InetAddress: " + (finalInetAddress != null ? finalInetAddress.getHostAddress() : null));

        // Creating a UDP socket on the specified port
        socket = new DatagramSocket(port);
        this.port = socket.getLocalPort();
        isClosed = false;
        logger.info("Listening on port: " + socket.getLocalPort());
    }

    @Override
    public void close() {
        if (socket != null) socket.close();
        isClosed = true;
    }

    @Override
    public String read() throws IOException {
        if (isClosed) {
            throw new IOException("Socket is closed!");
        }
        // Buffer to receive incoming data
        byte[] receiveData = new byte[1024];

        // Creating a packet to receive data
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length, finalInetAddress, port);

        // Receiving data
        socket.receive(receivePacket);

        // Converting received data to a string
        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        logger.info("Received message: " + receivedMessage);
        return receivedMessage;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }
}

