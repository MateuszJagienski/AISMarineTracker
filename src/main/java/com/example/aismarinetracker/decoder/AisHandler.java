package com.example.aismarinetracker.decoder;


import com.example.aismarinetracker.decoder.enums.MessageType;
import com.example.aismarinetracker.decoder.exceptions.UnsupportedMessageType;
import com.example.aismarinetracker.decoder.reports.AisMessage;
import com.example.aismarinetracker.decoder.reports.AisMessageFactory;
import com.sun.tools.javac.Main;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AisHandler {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private final AisMessageFactory aisMessageFactory;


    public AisHandler(AisMessageFactory aisMessageFactory) {
        logger.setLevel(Level.ALL);
        this.aisMessageFactory = aisMessageFactory;
    }

    public AisMessage handleAisMessage(String... nmeaMessage) throws UnsupportedMessageType {
        for (String s : nmeaMessage)
            if (!AisValidator.isMessageFormatValid(s)) {
                logger.warning("message: " + s + " is invalid");
            }

        RawAisMessage[] rawAisMessages = new RawAisMessage[nmeaMessage.length];
        if (nmeaMessage.length > 1) {
            for (int i = 0; i < nmeaMessage.length; i++) {
                rawAisMessages[i] = createRawAisMessage(nmeaMessage[i]);
            }
        } else {
            rawAisMessages[0] = createRawAisMessage(nmeaMessage[0]);
        }
        var messagePayload = Decoders.undoArmouringToBinaryString(Decoders.concatenateMessagesPayloads(rawAisMessages));
        try {
            var ais = aisMessageFactory.createAisMessage(messagePayload);
            ais.setMessageRaw(nmeaMessage);
            return ais;
        } catch (UnsupportedMessageType e) {
            logger.info("Unsupported message: " + MessageType.from(Decoders.toUnsignedInteger(messagePayload.substring(0, 6))) + " " + nmeaMessage[0]);
            throw e;
        }
    }

    private RawAisMessage createRawAisMessage(String nmeaMessage) {
        var messageParts = nmeaMessage.split("\\*");
        var checksum = messageParts[1];
        var messageWithoutChecksum = messageParts[0];
        String[] messageFields = messageWithoutChecksum.split(",");

        return new RawAisMessage(messageFields[0], Integer.parseInt(messageFields[1]),
                Integer.parseInt(messageFields[2]), messageFields[3], messageFields[4],
                messageFields[5], Integer.parseInt(messageFields[6]), checksum);
    }
}