package com.example.aismarinetracker.decoder;


import com.example.aismarinetracker.decoder.reports.AisMessage;
import com.example.aismarinetracker.decoder.reports.AisMessageFactory;
import com.sun.tools.javac.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AisHandler {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private final AisMessageFactory aisMessageFactory;


    public AisHandler() {
        logger.setLevel(Level.OFF);
        this.aisMessageFactory = new AisMessageFactory();
    }

    public AisMessage handleAisMessage(String... nmeaMessage) {
        for (String s : nmeaMessage)
            if (!isMessageFormatValid(s)) {
                throw new RuntimeException("Invalid message format");
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
        return aisMessageFactory.createAisMessage(messagePayload);
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

    private boolean isMessageFormatValid(String message) {
        if (message.split(",").length != 7) {
            logger.info("Invalid number of fields");
            return false;
        }
        if (!message.startsWith("!AIVDM") && !message.startsWith("!BSVDM") && !message.startsWith("!AIVDO") && !message.startsWith("!BSVDO")) {
            logger.info("Invalid message prefix expected: !AIVDM or !BSVDM but was: " + message.substring(0, 6));
            return false;
        }

        return isCheckSumValid(message);
    }

    private boolean isCheckSumValid(String message) {
        var messageParts = message.split("\\*");
        if (messageParts.length != 2) {
            logger.info("message doesnt contain checksum");
            return false;
        }
        var checksum = messageParts[1];
        var messageWithoutChecksum = messageParts[0];

        if (messageWithoutChecksum.startsWith("!") || messageWithoutChecksum.startsWith("$")) {
            messageWithoutChecksum = messageWithoutChecksum.substring(1);
        }

        var calculatedChecksum = Decoders.calculateChecksum(messageWithoutChecksum);
        if (!calculatedChecksum.equalsIgnoreCase(checksum)) {
            logger.info("Invalid checksum expected: " + calculatedChecksum + " but was: " + checksum);
            return false;
        }
        return calculatedChecksum.equalsIgnoreCase(checksum);
    }
}