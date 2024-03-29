package pl.ais.aismarinetracker.decoder.reports;


import pl.ais.aismarinetracker.decoder.AisHandler;
import pl.ais.aismarinetracker.decoder.exceptions.UnsupportedMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AisMessageTest {

    private String rawAisMessage = "!AIVDM,1,1,,A,144ar<0wAK11`p8NjQALKqv00400,0*5D";
    private AisMessage aisMessage;
    @Autowired
    private AisHandler aisHandler;


    @BeforeEach
    void setUp() {
        try {
        aisMessage = aisHandler.handleAisMessage(rawAisMessage);
        } catch (
        UnsupportedMessageType e) {
            System.out.println("Unsupported message");
        }
    }

    @Test
    @DisplayName("getMMSI() returns the MMSI 9-digits number")
    void getMMSI() {
        var expected = 273316400;
        assertEquals(expected, aisMessage.getMMSI());
    }
}