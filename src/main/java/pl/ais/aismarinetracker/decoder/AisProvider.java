package pl.ais.aismarinetracker.decoder;

import java.io.IOException;
import java.net.UnknownHostException;

public interface AisProvider {
    void open(String address, int port) throws IOException;
    void close() throws IOException;
    String read() throws IOException;
    boolean isClosed();
}
