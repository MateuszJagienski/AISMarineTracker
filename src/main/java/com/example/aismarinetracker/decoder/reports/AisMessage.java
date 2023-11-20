package com.example.aismarinetracker.decoder.reports;

import com.example.aismarinetracker.decoder.Decoders;
import com.example.aismarinetracker.decoder.RawAisMessage;
import com.example.aismarinetracker.decoder.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.logging.Logger;


@Getter
public abstract class AisMessage {

    private int MMSI;
    private MessageType messageType;
    private final String binaryMessagePayload;
    private String[] messageRaw;

    private static final Logger logger = Logger.getLogger(AisMessage.class.getName());

    protected AisMessage(String binaryMessagePayload) {
        this.binaryMessagePayload = binaryMessagePayload;
        decode();
    }

    private void decode() {
        this.MMSI = decodeMMSI();
        this.messageType = decodeMessageType();
    }

    private int decodeMMSI() {
        return Decoders.toUnsignedInteger(binaryMessagePayload.substring(8, 38));
    }

    private MessageType decodeMessageType() {
        return MessageType.from(Integer.parseInt(binaryMessagePayload.substring(0, 6), 2));
    }

    public void setMessageRaw(String... messageRaw) {
        this.messageRaw = messageRaw;
    }
}