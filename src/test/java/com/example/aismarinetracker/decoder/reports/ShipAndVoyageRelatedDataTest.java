package com.example.aismarinetracker.decoder.reports;

import com.example.aismarinetracker.decoder.AisHandler;
import com.example.aismarinetracker.decoder.enums.EPFD;
import com.example.aismarinetracker.decoder.enums.ShipType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShipAndVoyageRelatedDataTest {

    private AisMessage aisMessage;
    private AisHandler aisHandler;
    @BeforeEach
    void setUp() {
        aisHandler = new AisHandler();
        //aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,144ar<0wAK11`p8NjQALKqv00400,0*5D");
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,1,A,54``>?42?ED8?ITkJ21LT<dv2222222222222216DhH;=4L?0>D3;DmjH888,0*71",
                "!AIVDM,2,2,1,A,88888888880,2*25"); // message type 5

    }

    @Test
    @DisplayName("Test message length")
    void getMessageLength() {
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = 426;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getMessagePayload().length());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = 426;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getMessagePayload().length());
        }
    }
    @Test
    void getAisVersion() {
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = 1;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getAisVersion());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = 1;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getAisVersion());
        }
    }

    @Test
    void getImoNumber() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = 0;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getImoNumber());
        }
    }

    @Test
    void getCallSign() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = "J050A";
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getCallSign());
        }
    }

    @Test
    void getVesselName() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = "HHLA 3 B";
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getVesselName());
        }
    }

    @Test
    void getShipType() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = ShipType.LawEnforcement;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getShipType());
        }
    }

    @Test
    void getDimensionToBow() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = 12;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getDimensionToBow());
        }
    }

    @Test
    void getDimensionToStern() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = 38;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getDimensionToStern());
        }
    }

    @Test
    void getDimensionToPort() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = 23;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getDimensionToPort());
        }
    }

    @Test
    void getDimensionToStarboard() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = 2;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getDimensionToStarboard());
        }
    }

    @Test
    void getTypeOfEPFD() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = EPFD.Undefined;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getTypeOfEPFD());
        }
    }

    @Test
    void getMonth() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = 5;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getMonth());
        }
    }

    @Test
    void getDay() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = 14;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getDay());
        }
    }

    @Test
    void getHour() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = 20;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getHour());
        }
    }

    @Test
    void getMinute() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = 10;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getMinute());
        }
    }

    @Test
    void getDraught() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = 0;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getDraught());
        }
    }

    @Test
    void getDestination() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = "";
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).getDestination());
        }
    }

    @Test
    void isDTE() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof ShipAndVoyageRelatedData) {
            var expected = false;
            assertEquals(expected, ((ShipAndVoyageRelatedData) aisMessage).isDte());
        }
    }
}