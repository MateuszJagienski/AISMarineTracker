package com.example.aismarinetracker.decoder.reports;

import com.example.aismarinetracker.decoder.AisHandler;
import com.example.aismarinetracker.decoder.enums.MessageType;
import com.example.aismarinetracker.decoder.enums.SyncState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardClassBCSPositionReportTest {

    private AisMessage aisMessage;
    private AisHandler aisHandler;
    @BeforeEach
    void setUp() {
        aisHandler = new AisHandler();
        aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,B6CdCm0t3`tba35f@V9faHi7kP06,0*58"); // message type 18
    }

    @Test
    @DisplayName("Correct message type")
    void getMessageType() {
        var expected = MessageType.StandardClassBCSPositionReport;
        assertEquals(expected, aisMessage.getMessageType());
    }

    @Test
    void getSpeedOverGround() {
        var expected = 1.4;
        assertEquals(expected, ((StandardClassBCSPositionReport) aisMessage).getSpeedOverGround(), 0.00001);
    }

    @Test
    void isPositionAccurate() {
        var expected = true;
        assertEquals(expected, ((StandardClassBCSPositionReport) aisMessage).isPositionAccurate());
    }

    @Test
    void getLongitude() {
        var expected = 53.010998;
        assertEquals(expected, ((StandardClassBCSPositionReport) aisMessage).getLongitude(), 0.00001);
    }

    @Test
    void getLatitude() {
        var expected = 40.005283;
        assertEquals(expected, ((StandardClassBCSPositionReport) aisMessage).getLatitude(), 0.00001);
    }

    @Test
    void getCourseOverGround() {
        var expected = 177;
        assertEquals(expected, ((StandardClassBCSPositionReport) aisMessage).getCourseOverGround(), 0.00001);
    }

    @Test
    void getTrueHeading() {
        var expected = 177;
        assertEquals(expected, ((StandardClassBCSPositionReport) aisMessage).getTrueHeading());
    }

    @Test
    void getTimeStamp() {
        var expected = 34;
        assertEquals(expected, ((StandardClassBCSPositionReport) aisMessage).getTimeStamp());
    }

    @Test
    void isCsUnit() {
        var expected = true;
        assertEquals(expected, ((StandardClassBCSPositionReport) aisMessage).isCsUnit());
    }

    @Test
    void isDisplay() {
        var expected = true;
        assertEquals(expected, ((StandardClassBCSPositionReport) aisMessage).isDisplay());
    }

    @Test
    void isDsc() {
        var expected = true;
        assertEquals(expected, ((StandardClassBCSPositionReport) aisMessage).isDsc());
    }

    @Test
    void isBand() {
        var expected = true;
        assertEquals(expected, ((StandardClassBCSPositionReport) aisMessage).isBand());
    }

    @Test
    void isMessage22() {
        var expected = true;
        assertEquals(expected, ((StandardClassBCSPositionReport) aisMessage).isMessage22());
    }

    @Test
    void isAssigned() {
        var expected = false;
        assertEquals(expected, ((StandardClassBCSPositionReport) aisMessage).isAssigned());
    }

    @Test
    void isRaimFlag() {
        var expected = false;
        assertEquals(expected, ((StandardClassBCSPositionReport) aisMessage).isRaimFlag());
    }

    @Test
    void getRadioStatus() {
        // Check the type of AIS message
        assertTrue(aisMessage instanceof StandardClassBCSPositionReport);

        // Extract radio status
        var radioStatus = ((StandardClassBCSPositionReport) aisMessage).getRadioStatus();
        assertTrue(radioStatus instanceof ITDMACommunicationState);
        var expectedSyncState = SyncState.BaseIndirect;
        var expectedSlotIncrement = 0;
        var expectedNumberOfSlots = 3;
        var expectedKeepFlag = false;
        var actualRadioStatus = (ITDMACommunicationState) ((StandardClassBCSPositionReport) aisMessage).getRadioStatus();

        assertEquals(expectedSyncState, actualRadioStatus.getSyncState());
        assertEquals(expectedSlotIncrement, actualRadioStatus.getSlotIncrement());
        assertEquals(expectedNumberOfSlots, actualRadioStatus.getNumberOfSlots());
        assertEquals(expectedKeepFlag, actualRadioStatus.isKeepFlag());
    }
}