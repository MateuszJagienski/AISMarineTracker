package pl.ais.aismarinetracker.decoder.reports;

import pl.ais.aismarinetracker.decoder.AisHandler;
import pl.ais.aismarinetracker.decoder.enums.EPFD;
import pl.ais.aismarinetracker.decoder.enums.ShipType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ShipAndVoyageRelatedDataTest {

    private AisMessage aisMessage;
    @Autowired
    private AisHandler aisHandler;
    @BeforeEach
    @SneakyThrows
	void setUp() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,1,A,54``>?42?ED8?ITkJ21LT<dv2222222222222216DhH;=4L?0>D3;DmjH888,0*71",
                "!AIVDM,2,2,1,A,88888888880,2*25"); // message type 5

    }

    @Test
    @DisplayName("Test message length")
    @SneakyThrows
	void getMessageLength() {
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = 426;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getBinaryMessagePayload().length());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = 426;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getBinaryMessagePayload().length());
        }
    }
    @Test
    @SneakyThrows
	void getAisVersion() {
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = 1;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getAisVersion());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = 1;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getAisVersion());
        }
    }

    @Test
    @SneakyThrows
	void getImoNumber() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = 0;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getImoNumber());
        }
    }

    @Test
    @SneakyThrows
	void getCallSign() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = "J050A";
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getCallSign());
        }
    }

    @Test
    @SneakyThrows
	void getVesselName() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = "HHLA 3 B";
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getVesselName());
        }
    }

    @Test
    @SneakyThrows
	void getShipType() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = ShipType.LawEnforcement;
            Assertions.assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getShipType());
        }
    }

    @Test
    @SneakyThrows
	void getDimensionToBow() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = 12;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getDimensionToBow());
        }
    }

    @Test
    @SneakyThrows
	void getDimensionToStern() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = 38;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getDimensionToStern());
        }
    }

    @Test
    @SneakyThrows
	void getDimensionToPort() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = 23;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getDimensionToPort());
        }
    }

    @Test
    @SneakyThrows
	void getDimensionToStarboard() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = 2;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getDimensionToStarboard());
        }
    }

    @Test
    @SneakyThrows
	void getTypeOfEPFD() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = EPFD.Undefined;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getTypeOfEPFD());
        }
    }

    @Test
    @SneakyThrows
	void getMonth() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = 5;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getMonth());
        }
    }

    @Test
    @SneakyThrows
	void getDay() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = 14;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getDay());
        }
    }

    @Test
    @SneakyThrows
	void getHour() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = 20;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getHour());
        }
    }

    @Test
    @SneakyThrows
	void getMinute() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = 10;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getMinute());
        }
    }

    @Test
    @SneakyThrows
	void getDraught() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = 0;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getDraught());
        }
    }

    @Test
    @SneakyThrows
	void getDestination() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = "";
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).getDestination());
        }
    }

    @Test
    @SneakyThrows
	void isDTE() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,539S:k40000000c3G04PPh63<00000000080000o1PVG2uGD:00000000000,0*34",
                "!AIVDM,2,2,0,B,00000000000,2*27");
        if (aisMessage instanceof StaticAndVoyageRelatedData) {
            var expected = false;
            assertEquals(expected, ((StaticAndVoyageRelatedData) aisMessage).isDte());
        }
    }
}