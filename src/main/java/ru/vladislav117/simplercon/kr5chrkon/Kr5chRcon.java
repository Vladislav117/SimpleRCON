package ru.vladislav117.simplercon.kr5chrkon;

import ru.vladislav117.simplercon.kr5chrkon.exceptions.Kr5chAuthenticationException;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Random;

public class Kr5chRcon {
    private final Object sync = new Object();
    private final Random rand = new Random();
    private int requestId;
    private Socket socket;
    private Charset charset;

    public Kr5chRcon(String host, int port, byte[] password) throws IOException, Kr5chAuthenticationException {
        this.charset = Charset.forName("UTF-8");
        this.connect(host, port, password);
    }

    public void connect(String host, int port, byte[] password) throws IOException, Kr5chAuthenticationException {
        if (host == null || host.trim().isEmpty()) {
            throw new IllegalArgumentException("Host can't be null or empty");
        }
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port is out of range");
        }
        synchronized (sync) {
            this.requestId = rand.nextInt();
            this.socket = new Socket(host, port);
        }
        Kr5chRconPacket res = this.send(Kr5chRconPacket.SERVERDATA_AUTH, password);
        if (res.getRequestId() == -1) {
            throw new Kr5chAuthenticationException("Password rejected by server");
        }
    }

    public void disconnect() throws IOException {
        synchronized (sync) {
            this.socket.close();
        }
    }

    public String command(String payload) throws IOException {
        if (payload == null || payload.trim().isEmpty()) {
            throw new IllegalArgumentException("Payload can't be null or empty");
        }
        Kr5chRconPacket response = this.send(Kr5chRconPacket.SERVERDATA_EXECCOMMAND, payload.getBytes());
        return new String(response.getPayload(), this.getCharset());
    }

    private Kr5chRconPacket send(int type, byte[] payload) throws IOException {
        synchronized (sync) {
            return Kr5chRconPacket.send(this, type, payload);
        }
    }

    public int getRequestId() {
        return requestId;
    }

    public Socket getSocket() {
        return socket;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
