package com.example.aismarinetracker.decoder.reports;

import com.example.aismarinetracker.decoder.AisHandler;
import com.example.aismarinetracker.decoder.enums.ManeuverIndicator;
import com.example.aismarinetracker.decoder.enums.NavigationStatus;
import com.example.aismarinetracker.decoder.enums.SyncState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionReportClassATest {

    private AisMessage aisMessage;
    private AisHandler aisHandler;
    @BeforeEach
    void setUp() {
        aisHandler = new AisHandler();
        //aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,144ar<0wAK11`p8NjQALKqv00400,0*5D");
        aisMessage = aisHandler.handleAisMessage("!AIVDO,1,1,,,100000?P00Q1BLTNnNU0Wgv62000,0*48");

    }

    @Test
    @DisplayName("getNavigationStatus() returns the navigation status")
    void getNavigationStatus() {
        if (aisMessage instanceof PositionReport) {
            var expected = NavigationStatus.Undefined;
            assertEquals(expected, ((PositionReport) aisMessage).getNavigationStatus());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,B,13`woCS000Q2<hHNitSN48RrP`A?,0*2C");
        if (aisMessage instanceof PositionReport) {
            var expected = NavigationStatus.RestrictedManoeuvrability;
            assertEquals(expected, ((PositionReport) aisMessage).getNavigationStatus());
        }
    }

    @Test
    @DisplayName("getMMSI() returns the MMSI 9-digits number")
    void getRateOfTurn() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,144ar<0wAK11`p8NjQALKqv00400,0*5D");
        if (aisMessage instanceof PositionReport) {
            var expected = 0;
            assertEquals(expected, ((PositionReport) aisMessage).getRateOfTurn());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDO,1,1,,,100000?P00Q1BLTNnNU0Wgv62000,0*48");
        if (aisMessage instanceof PositionReport) {
            var expected = -128;
            assertEquals(expected, ((PositionReport) aisMessage).getRateOfTurn());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,13u?etPv2;0n:dDPwUM1U1Cb069D,0*24");
        if (aisMessage instanceof PositionReport) {
            var expected = -3;
            assertEquals(expected, ((PositionReport) aisMessage).getRateOfTurn());
        }
    }

    @Test
    @DisplayName("getSpeedOverGround() returns the speed over ground in knots")
    void getSpeedOverGround() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,13u?etPv2;0n:dDPwUM1U1Cb069D,0*24");
        if (aisMessage instanceof PositionReport) {
            var expected = 13.9f;
            assertEquals(expected, ((PositionReport) aisMessage).getSpeedOverGround());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDO,1,1,,,100000?P00Q1BLTNnNU0Wgv62000,0*48");
    }

    @Test
    @DisplayName("isPositionAccurate() returns true if the position is accurate")
    void isPositionAccurate() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,13u?etPv2;0n:dDPwUM1U1Cb069D,0*24");
        if (aisMessage instanceof PositionReport) {
            assertFalse(((PositionReport) aisMessage).isPositionAccurate());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDO,1,1,,,100000?P00Q1BLTNnNU0Wgv62000,0*48");
        if (aisMessage instanceof PositionReport) {
            assertTrue(((PositionReport) aisMessage).isPositionAccurate());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDO,1,1,,,100000?P00Q1BLVNnNThWgv:2000,0*1F");
        if (aisMessage instanceof PositionReport) {
            assertTrue(((PositionReport) aisMessage).isPositionAccurate());
        }
    }

    @Test
    @DisplayName("getLongitude() returns the longitude in degrees")
    void getLongitude() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,13u?etPv2;0n:dDPwUM1U1Cb069D,0*24");
        if (aisMessage instanceof PositionReport) {
            var expected = 11.832976f;
            assertEquals(expected, ((PositionReport) aisMessage).getLongitude());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDO,1,1,,,100000?P00Q1BLTNnNU0Wgv62000,0*48");
        if (aisMessage instanceof PositionReport) {
            var expected = 14.26243f;
            assertEquals(expected, ((PositionReport) aisMessage).getLongitude());
        }
    }

    @Test
    @DisplayName("getLatitude() returns the latitude in degrees")
    void getLatitude() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,13u?etPv2;0n:dDPwUM1U1Cb069D,0*24");
        if (aisMessage instanceof PositionReport) {
            var expected = 57.660355f;
            assertEquals(expected, ((PositionReport) aisMessage).getLatitude());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDO,1,1,,,100000?P00Q1BLTNnNU0Wgv62000,0*48");
        if (aisMessage instanceof PositionReport) {
            var expected = 53.91641f;
            assertEquals(expected, ((PositionReport) aisMessage).getLatitude());
        }
    }

    @Test
    @DisplayName("getCourseOverGround() returns the course over ground in degrees")
    void getCourseOverGround() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,13u?etPv2;0n:dDPwUM1U1Cb069D,0*24");
        if (aisMessage instanceof PositionReport) {
            var expected = 40.4f;
            assertEquals(expected, ((PositionReport) aisMessage).getCourseOverGround());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDO,1,1,,,100000?P00Q1BLTNnNU0Wgv62000,0*48");
        if (aisMessage instanceof PositionReport) {
            var expected = 15.8f;
            assertEquals(expected, ((PositionReport) aisMessage).getCourseOverGround());
        }
    }

    @Test
    @DisplayName("getTrueHeading() returns the true heading in degrees")
    void getTrueHeading() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,13u?etPv2;0n:dDPwUM1U1Cb069D,0*24");
        if (aisMessage instanceof PositionReport) {
            var expected = 41;
            assertEquals(expected, ((PositionReport) aisMessage).getTrueHeading());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDO,1,1,,,100000?P00Q1BLTNnNU0Wgv62000,0*48");
        if (aisMessage instanceof PositionReport) {
            var expected = 511;
            assertEquals(expected, ((PositionReport) aisMessage).getTrueHeading());
        }
    }

    @Test
    @DisplayName("getTimeStamp() returns the time stamp")
    void getTimeStamp() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,13u?etPv2;0n:dDPwUM1U1Cb069D,0*24");
        if (aisMessage instanceof PositionReport) {
            var expected = 53;
            assertEquals(expected, ((PositionReport) aisMessage).getTimeStamp());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDO,1,1,,,100000?P00Q1BLTNnNU0Wgv62000,0*48");
        if (aisMessage instanceof PositionReport) {
            var expected = 3;
            assertEquals(expected, ((PositionReport) aisMessage).getTimeStamp());
        }
    }

    @Test
    @DisplayName("getManeuverIndicator() returns the maneuver indicator")
    void getManeuverIndicator() {
        aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,13u?etPv2;0n:dDPwUM1U1Cb069D,0*24");
        if (aisMessage instanceof PositionReport) {
            var expected = ManeuverIndicator.NotAvailable;
            assertEquals(expected, ((PositionReport) aisMessage).getManeuverIndicator());
        }
        aisMessage = aisHandler.handleAisMessage("!AIVDO,1,1,,,100000?P00Q1BLTNnNU0Wgv62000,0*48");
        if (aisMessage instanceof PositionReport) {
            var expected = ManeuverIndicator.NotAvailable;
            assertEquals(expected, ((PositionReport) aisMessage).getManeuverIndicator());
        }
    }

    @Test
    void isRaimFlag() {
        var aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,13u?etPv2;0n:dDPwUM1U1Cb069D,0*24");
        if (aisMessage instanceof PositionReport) {
            assertFalse(((PositionReport) aisMessage).isRaimFlag());
        }
    }

    @Test
    void getRadioStatus() {
        var aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,13u?etPv2;0n:dDPwUM1U1Cb069D,0*24");
        System.out.println(aisMessage);
        assertTrue(aisMessage instanceof PositionReportClassAScheduled);
        var radioStatus = (SOTDMACommunicationState) ((PositionReport) aisMessage).getRadioStatus();
        var expectedSyncState = SyncState.UTCDirect;
        var expectedSlotTimeout = 1;
        var expectedUTCHour = 17;
        var expectedUTCMinute = 21;

        Integer expectedReceivedStations = null;
        Integer expectedSlotNumber = null;

        assertEquals(expectedSyncState, ((PositionReport) aisMessage).getRadioStatus().getSyncState());
        assertEquals(expectedSlotTimeout, radioStatus.getSlotTimeout());
        assertNull(radioStatus.getSlotNumber());
        assertEquals(expectedUTCHour, radioStatus.getUtcHour());
        assertEquals(expectedUTCMinute, radioStatus.getUtcMinute());
        assertEquals(expectedReceivedStations, radioStatus.getReceivedStations());
        assertEquals(expectedSlotNumber, radioStatus.getSlotNumber());

        aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,B,33ptLk001HQ1FttNoKo0Mc>L0000,0*30");
        assertTrue(aisMessage instanceof PositionReportClassAResponseToInterrogation);
        System.out.println(((PositionReport) aisMessage).getRadioStatus());
        var radioStatus1 = (ITDMACommunicationState) ((PositionReport) aisMessage).getRadioStatus();
        var expectedSyncState1 = SyncState.UTCDirect;
        var slotIncrement = 0;
        var numberOfSlots = 0;

        assertEquals(expectedSyncState1, radioStatus1.getSyncState());
        assertEquals(slotIncrement, radioStatus1.getSlotIncrement());
        assertEquals(numberOfSlots, radioStatus1.getNumberOfSlots());
        assertFalse(radioStatus1.isKeepFlag());
    }
}