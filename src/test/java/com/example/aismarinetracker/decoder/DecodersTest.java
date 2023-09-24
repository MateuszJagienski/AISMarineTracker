package com.example.aismarinetracker.decoder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecodersTest {

    private String payload;

    private String bitString;
    private Decoders decoders;
    @BeforeEach
    void setUp() {
        payload = "144ar<0wAK11`p8NjQALKqv00400";
        bitString = Decoders.undoArmouringToBinaryString(payload);
    }

    @Test
    void undoArmouringToBinaryString() {
        var expected = "000001000100000100101001111010001100000000111111010001011011000001000001101000111000001000011110110010100001010001011100011011111001111110000000000000000100000000000000";
        assertEquals(expected, Decoders.undoArmouringToBinaryString(payload));
    }

    @Test
    void toInteger() {
        var expected = 1;
        assertEquals(expected, Decoders.toInteger(bitString.substring(0, 6)));
    }

    @Test
    void toFloat() {
        assertEquals(0, Decoders.toFloat("00000000000000000"));
        assertEquals(1, Decoders.toFloat("00000000000000001"));
        assertEquals(-1, Decoders.toFloat("11111111111111111"));
        assertEquals(-2.11666666667f, Decoders.toFloat("10000001") / 60f, 1e-16);
    }

    @Test
    void toBoolean() {
    }

    @Test
    void toUnsignedLong() {
    }

    @Test
    void toUnsignedFloat() {
    }

    @Test
    void toAsciiString() {
    }
}