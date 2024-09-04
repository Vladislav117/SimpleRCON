package ru.vladislav117.simplercon.kr5chrkon;

import ru.vladislav117.simplercon.kr5chrkon.exceptions.Kr5chMalformedPacketException;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Kr5chRconPacket {
    public static final int SERVERDATA_EXECCOMMAND = 2;
    public static final int SERVERDATA_AUTH = 3;
    private int requestId;
    private int type;
    private byte[] payload;

    private Kr5chRconPacket(int requestId, int type, byte[] payload) {
        this.requestId = requestId;
        this.type = type;
        this.payload = payload;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getType() {
        return type;
    }

    public byte[] getPayload() {
        return payload;
    }

    protected static Kr5chRconPacket send(Kr5chRcon rcon, int type, byte[] payload) throws IOException {
        try {
            Kr5chRconPacket.write(rcon.getSocket().getOutputStream(), rcon.getRequestId(), type, payload);
        } catch (SocketException se) {
            rcon.getSocket().close();
            throw se;
        }
        return Kr5chRconPacket.read(rcon.getSocket().getInputStream());
    }

    private static void write(OutputStream out, int requestId, int type, byte[] payload) throws IOException {
        int bodyLength = Kr5chRconPacket.getBodyLength(payload.length);
        int packetLength = Kr5chRconPacket.getPacketLength(bodyLength);
        ByteBuffer buffer = ByteBuffer.allocate(packetLength);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(bodyLength);
        buffer.putInt(requestId);
        buffer.putInt(type);
        buffer.put(payload);
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        out.write(buffer.array());
        out.flush();
    }

    private static Kr5chRconPacket read(InputStream in) throws IOException {
        byte[] header = new byte[4 * 3];
        in.read(header);
        try {
            ByteBuffer buffer = ByteBuffer.wrap(header);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            int length = buffer.getInt();
            int requestId = buffer.getInt();
            int type = buffer.getInt();
            byte[] payload = new byte[length - 4 - 4 - 2];
            DataInputStream dis = new DataInputStream(in);
            dis.readFully(payload);
            dis.read(new byte[2]);
            return new Kr5chRconPacket(requestId, type, payload);
        } catch (BufferUnderflowException | EOFException e) {
            throw new Kr5chMalformedPacketException("Cannot read the whole packet");
        }
    }

    private static int getPacketLength(int bodyLength) {
        return 4 + bodyLength;
    }

    private static int getBodyLength(int payloadLength) {
        return 4 + 4 + payloadLength + 2;
    }
}
