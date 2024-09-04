package ru.vladislav117.simplercon.kr5chrkon.exceptions;

import java.io.IOException;

public class Kr5chMalformedPacketException extends IOException {
    public Kr5chMalformedPacketException(String message) {
        super(message);
    }
}
