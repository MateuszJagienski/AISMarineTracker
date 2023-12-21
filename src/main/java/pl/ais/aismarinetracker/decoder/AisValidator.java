package pl.ais.aismarinetracker.decoder;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AisValidator {

    private static final Logger logger = Logger.getLogger(AisValidator.class.getName());


    public AisValidator() {
        logger.setLevel(Level.OFF);
    }

    public static boolean isMessageFormatValid(String message) {
        if (!validateMessageFieldCount(message)) return false;
        if (!message.startsWith("!AIVDM") && !message.startsWith("!BSVDM") && !message.startsWith("!AIVDO") && !message.startsWith("!BSVDO")) {
            logger.info("Invalid message prefix expected: !AIVDM or !BSVDM but was: " + message.substring(0, 6));
            return false;
        }

        return isCheckSumValid(message);
    }

    public static boolean validateMessageFieldCount(String message) {
        if (message.split(",").length != 7) {
            logger.info("Invalid number of fields");
            return false;
        }
        return true;
    }

    public static boolean isCheckSumValid(String message) {
        var messageParts = message.split("\\*");
        if (messageParts.length != 2) {
            logger.warning("message doesnt contain checksum");
            return false;
        }
        var checksum = messageParts[1];
        var messageWithoutChecksum = messageParts[0];

        if (messageWithoutChecksum.startsWith("!") || messageWithoutChecksum.startsWith("$")) {
            messageWithoutChecksum = messageWithoutChecksum.substring(1);
        }

        var calculatedChecksum = Decoders.calculateChecksum(messageWithoutChecksum);
        calculatedChecksum = calculatedChecksum.trim();
        checksum = checksum.trim();

        logger.info("cal check : " + calculatedChecksum + " org check: " + checksum);
        if (!calculatedChecksum.equalsIgnoreCase(checksum)) {
            logger.warning("Invalid checksum expected: " + calculatedChecksum + " but was: " + checksum);
            return false;
        }
        return calculatedChecksum.equalsIgnoreCase(checksum);
    }
}
