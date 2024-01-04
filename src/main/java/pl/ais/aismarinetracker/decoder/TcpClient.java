package pl.ais.aismarinetracker.decoder;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class TcpClient implements AisProvider {

    private boolean isClosed = true;
    private Socket socket;
    BufferedReader in;
    private static final Logger logger = Logger.getLogger(TcpClient.class.getName());


    @Override
    public void open(String address, int port) throws IOException {
        if (!isClosed) close();
        this.socket = new Socket(address, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        isClosed = false;
        logger.info("Listening on port: " + socket.getLocalPort() + " " + port);
    }
    @Override
    public void close() throws IOException {
        if (in != null) in.close();
        if (socket != null) socket.close();
        isClosed = true;
    }
    @Override
    public String read() throws IOException {
        return in.readLine();
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }
}
