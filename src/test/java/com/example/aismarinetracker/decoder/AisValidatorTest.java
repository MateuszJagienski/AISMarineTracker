package com.example.aismarinetracker.decoder;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AisValidatorTest {

    @Test
    void isMessageFormatValid_WithValidMessage_ReturnsTrue() {
        // Given
        String validMessage = "!AIVDM,1,1,,A,144ar<0wAK11`p8NjQALKqv00400,0*5D";

        // When
        boolean isValid = AisValidator.isMessageFormatValid(validMessage);

        // Then
        assertTrue(isValid);
    }

    @Test
    void isMessageFormatValid_WithInvalidMessage_ReturnsFalse() {
        // Given
        String invalidMessage = "InvalidMessage";

        // When
        boolean isValid = AisValidator.isMessageFormatValid(invalidMessage);

        // Then
        assertFalse(isValid);
    }

    @Test
    void isCheckSumValid_WithValidChecksum_ReturnsTrue() {
        // Given
        String validMessage = "!AIVDM,1,1,,A,144ar<0wAK11`p8NjQALKqv00400,0*5D";

        // When
        boolean isValid = AisValidator.isCheckSumValid(validMessage);

        // Then
        assertTrue(isValid);
    }

    @Test
    void isCheckSumValid_WithInvalidChecksum_ReturnsFalse() {
        // Given
        String invalidChecksumMessage = "!AIVDM,1,1,,A,144ar<0wAK11`p8NjQALKqv00400,0*00";

        // When
        boolean isValid = AisValidator.isCheckSumValid(invalidChecksumMessage);

        // Then
        assertFalse(isValid);
    }

}
