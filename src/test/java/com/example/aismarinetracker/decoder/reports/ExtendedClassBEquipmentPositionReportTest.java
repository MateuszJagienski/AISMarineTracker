package com.example.aismarinetracker.decoder.reports;

import com.example.aismarinetracker.decoder.AisHandler;
import com.example.aismarinetracker.decoder.enums.EPFD;
import com.example.aismarinetracker.decoder.enums.MessageType;
import com.example.aismarinetracker.decoder.enums.ShipType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtendedClassBEquipmentPositionReportTest {

    private AisMessage aisMessage;
    private AisHandler aisHandler;
    @BeforeEach
    void setUp() {
        aisHandler = new AisHandler();
        //aisMessage = aisHandler.handleAisMessage("!AIVDM,1,1,,A,144ar<0wAK11`p8NjQALKqv00400,0*5D");
        aisMessage = aisHandler.handleAisMessage("!AIVDM,2,1,0,B,C8u:8C@t7@TnGCKfm6Po`e6N`:Va0L2J;06HV50JV?SjBPL3,0*28",
                "!AIVDM,2,2,0,B,11RP,0*17"); // message type 19

    }

    @Test
    @DisplayName(
            "Test message type 19, Extended Class B Equipment Position Report"
    )
    void getMessageType() {
        var expected = MessageType.ExtendedClassBEquipmentPositionReport;
        assertEquals(expected, aisMessage.decodeMessageType());
    }
    @Test
    void getRegionalReserved() {
        var expected = 15;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getRegionalReserved());
    }

    @Test
    void getRegionalReserved1() {
        var expected = 15;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getRegionalReserved());
    }

    @Test
    void getSpeedOverGround() {
        var expected = 2.9;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getSpeedOverGround(), 0.00001f);
    }

    @Test
    void isPositionAccurate() {
        var expected = false;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).isPositionAccurate());
    }

    @Test
    void getLongitude() {
        var expected = 32.19953;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getLongitude(), 0.00001f);
    }

    @Test
    void getLatitude() {
        var expected = -29.83748;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getLatitude(), 0.00001f);
    }

    @Test
    void getCourseOverGround() {
        var expected = 89;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getCourseOverGround());
    }

    @Test
    void getTrueHeading() {
        var expected = 90;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getTrueHeading());
    }

    @Test
    void getTimeStamp() {
        var expected = 12;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getTimeStamp());
    }

    @Test
    void getName() {
        var expected = "TEST NAME CLSB MSG19";
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getName());
    }

    @Test
    void getShipType() {
        var expected = ShipType.PleasureCraft;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getShipType());
    }

    @Test
    void getDimensionToBow() {
        var expected = 7;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getDimensionToBow());
    }

    @Test
    void getDimensionToStern() {
        var expected = 6;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getDimensionToStern());
    }

    @Test
    void getDimensionToPort() {
        var expected = 2;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getDimensionToPort());
    }

    @Test
    void getDimensionToStarboard() {
        var expected = 3;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getDimensionToStarboard());
    }

    @Test
    void getTypeOfEPFD() {
        var expected = EPFD.GPS;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).getTypeOfEPFD());
    }

    @Test
    void isRaimFlag() {
        var expected = false;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).isRaimFlag());
    }

    @Test
    void isDataTerminalReady() {
        var expected = true;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).isDataTerminalReady());
    }

    @Test
    void isAssignedModeFlag() {
        var expected = false;
        assertEquals(expected, ((ExtendedClassBEquipmentPositionReport) aisMessage).isAssignedModeFlag());
    }
}