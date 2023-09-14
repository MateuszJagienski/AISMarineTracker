package com.example.aismarinetracker.decoder.reports;

import com.example.aismarinetracker.decoder.Decoders;
import com.example.aismarinetracker.decoder.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.logging.Logger;


@Getter
public abstract class AisMessage {

    private int MMSI;
    private MessageType messageType;
    @JsonIgnore
    private final String messagePayload;

    private static final Logger logger = Logger.getLogger(AisMessage.class.getName());

    protected AisMessage(String messagePayload) {
        this.messagePayload = messagePayload;
        decode();
    }

    private void decode() {
        this.MMSI = decodeMMSI();
        this.messageType = decodeMessageType();
    }

    public int decodeMMSI() {
        return Decoders.toUnsignedInteger(messagePayload.substring(8, 38));
    }

    public MessageType decodeMessageType() {
        return MessageType.from(Integer.parseInt(messagePayload.substring(0, 6), 2));
    }
}