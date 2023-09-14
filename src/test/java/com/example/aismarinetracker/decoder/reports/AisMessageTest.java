package com.example.aismarinetracker.decoder.reports;


import com.example.aismarinetracker.decoder.AisHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AisMessageTest {

    private String rawAisMessage = "!AIVDM,1,1,,A,144ar<0wAK11`p8NjQALKqv00400,0*5D";
    private AisMessage aisMessage;

    @BeforeEach
    void setUp() {
        AisHandler aisHandler = new AisHandler();
        aisMessage = aisHandler.handleAisMessage(rawAisMessage);
       // aisMessage = AisMessage.create(new RawAisMessage(rawAisMessage));
    }

    @Test
    @DisplayName("getMMSI() returns the MMSI 9-digits number")
    void getMMSI() {
        var expected = 273316400;
        assertEquals(expected, aisMessage.decodeMMSI());
    }
}