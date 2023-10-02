package com.example.aismarinetracker.decoder.reports;

import com.example.aismarinetracker.decoder.AisHandler;
import com.example.aismarinetracker.decoder.enums.MessageType;
import com.example.aismarinetracker.decoder.enums.NavigationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongRangeBroadcastMessageTest {

    private AisMessage aisMessage;
    private AisHandler aisHandler;
    @BeforeEach
    void setUp() {
        aisHandler = new AisHandler();
        aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,B,KC5E2b@U19PFdLbMuc5=ROv62<7m,0*16"); // message type 27
    }

    @Test
    @DisplayName("Correct message type")
    void getMessageType() {
        var expected = MessageType.PositionReportForLongRangeApplications;

        assertEquals(expected, aisMessage.getMessageType());
    }

    @Test
    void getPositionAccuracy() {
        var expected = false;
        assertEquals(expected, ((PositionReportForLongRangeApplications) aisMessage).isPositionAccuracy());
    }

    @Test
    void getRAIMFlag() {
        var expected = false;
        assertEquals(expected, ((PositionReportForLongRangeApplications) aisMessage).isRaimFlag());
    }

    @Test
    void getNavigationStatus() {
        var expected = NavigationStatus.NotUnderCommand;
        assertEquals(expected, ((PositionReportForLongRangeApplications) aisMessage).getNavigationStatus());
    }

    @Test
    void getLongitude() {
        var expected = 82214;
        assertEquals(expected, ((PositionReportForLongRangeApplications) aisMessage).getLongitude());
    }

    @Test
    void getLatitude() {
        var expected = 2904;
        assertEquals(expected, ((PositionReportForLongRangeApplications) aisMessage).getLatitude());
    }

    @Test
    void getCourseOverGround() {
        var expected = 167;
        assertEquals(expected, ((PositionReportForLongRangeApplications) aisMessage).getCourseOverGround());
    }

    @Test
    void getSpeedOverGround() {
        var expected = 57;
        assertEquals(expected, ((PositionReportForLongRangeApplications) aisMessage).getSpeedOverGround());
    }

    @Test
    void getGNSSPositionStatus() {
        var expected = false;
        assertEquals(expected, ((PositionReportForLongRangeApplications) aisMessage).isGNSSPositionStatus());
    }
}