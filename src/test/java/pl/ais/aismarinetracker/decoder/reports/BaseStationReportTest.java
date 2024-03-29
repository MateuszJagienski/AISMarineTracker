package pl.ais.aismarinetracker.decoder.reports;

import pl.ais.aismarinetracker.decoder.AisHandler;
import pl.ais.aismarinetracker.decoder.enums.EPFD;
import pl.ais.aismarinetracker.decoder.enums.SyncState;
import pl.ais.aismarinetracker.decoder.exceptions.UnsupportedMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BaseStationReportTest {

    private AisMessage aisMessage;
    @Autowired
    private AisHandler aisHandler;
    @BeforeEach
    void setUp() {
        try {
            aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,402OFL1vHPP03Q1IP8NnVFo00D1P,0*62"); // message type 4
        } catch (UnsupportedMessageType e) {
            System.out.println("Unsupported message");
        }

    }

    @Test
    void getMMSI() {
        if (aisMessage instanceof BaseStationReport) {
            var expected = 2610800;
            assertEquals(expected, (aisMessage).getMMSI());
        }
    }

    @Test
    void getYear() {
        if (aisMessage instanceof BaseStationReport) {
            var expected = 2022;
            assertEquals(expected, ((BaseStationReport) aisMessage).getYear());
        }
        try {
            aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,B,402OFL1vHPP03Q1IP8NnVFo00D1P,0*61");
        } catch (UnsupportedMessageType e) {
            System.out.println("Unsupported message!");
        }
        if (aisMessage instanceof BaseStationReport) {
            var expected = 2022;
            assertEquals(expected, ((BaseStationReport) aisMessage).getYear());
        }
    }

    @Test
    void getMonth() {
        if (aisMessage instanceof BaseStationReport) {
            var expected = 2;
            assertEquals(expected, ((BaseStationReport) aisMessage).getMonth());
        }
    }

    @Test
    void getDay() {
        if (aisMessage instanceof BaseStationReport) {
            var expected = 1;
            assertEquals(expected, ((BaseStationReport) aisMessage).getDay());
        }
    }

    @Test
    void getHour() {
        if (aisMessage instanceof BaseStationReport) {
            var expected = 0;
            assertEquals(expected, ((BaseStationReport) aisMessage).getHour());
        }
    }

    @Test
    void getMinute() {
        if (aisMessage instanceof BaseStationReport) {
            var expected = 0;
            assertEquals(expected, ((BaseStationReport) aisMessage).getMinute());
        }
    }

    @Test
    void getSecond() {
        if (aisMessage instanceof BaseStationReport) {
            var expected = 3;
            assertEquals(expected, ((BaseStationReport) aisMessage).getSecond());
        }
    }

    @Test
    void isFixQuality() {
        if (aisMessage instanceof BaseStationReport) {
            var expected = true;
            assertEquals(expected, ((BaseStationReport) aisMessage).isFixQuality());
        }
    }

    @Test
    void getLongitude() {
        if (aisMessage instanceof BaseStationReport) {
            var expected = 14.286513f;
            assertEquals(expected, ((BaseStationReport) aisMessage).getLongitude(), 0.00001f);
        }
    }

    @Test
    void getLatitude() {
        if (aisMessage instanceof BaseStationReport) {
            var expected = 53.919727f;
            assertEquals(expected, ((BaseStationReport) aisMessage).getLatitude(), 0.000001f);
        }
    }

    @Test
    void getTypeOfEPFD() {
        if (aisMessage instanceof BaseStationReport) {
            var expected = EPFD.Surveyed;
            assertEquals(expected, ((BaseStationReport) aisMessage).getTypeOfEPFD());
        }
    }

    @Test
    void isRaimFlag() {
        if (aisMessage instanceof BaseStationReport) {
            var expected = false;
            assertEquals(expected, ((BaseStationReport) aisMessage).isRaimFlag());
        }
    }

    @Test
    void getSOTDMAState() {
        if (aisMessage instanceof BaseStationReport) {

            var radioStatus = (SOTDMACommunicationState) ((BaseStationReport) aisMessage).getSOTDMAState();

            // Define the expected values
            var expectedSyncState = SyncState.UTCDirect;
            var expectedSlotTimeout = 5;
            var expectedNumberOfReceivedStations = 96;
            Integer expectedSlotNumber = null;
            Integer expectedUtcHour = null;
            Integer expectedUtcMinute = null;
            Integer expectedSlotOffset = null;

            // Perform assertions
            assertEquals(expectedSyncState, radioStatus.getSyncState());
            assertEquals(expectedSlotTimeout, radioStatus.getSlotTimeout());
            assertEquals(expectedNumberOfReceivedStations, radioStatus.getReceivedStations());
            assertEquals(expectedSlotNumber, radioStatus.getSlotNumber());
            assertEquals(expectedUtcHour, radioStatus.getUtcHour());
            assertEquals(expectedUtcMinute, radioStatus.getUtcMinute());
            assertEquals(expectedSlotOffset, radioStatus.getSlotOffset());
        }
    }
}