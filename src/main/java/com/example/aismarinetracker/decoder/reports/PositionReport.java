package com.example.aismarinetracker.decoder.reports;

import com.example.aismarinetracker.decoder.Decoders;
import com.example.aismarinetracker.decoder.enums.ManeuverIndicator;
import com.example.aismarinetracker.decoder.enums.MessageType;
import com.example.aismarinetracker.decoder.enums.NavigationStatus;
import com.example.aismarinetracker.decoder.enums.SyncState;
import lombok.Getter;

@Getter
public class PositionReport extends AisMessage {

    private NavigationStatus navigationStatus;
    private int rateOfTurn;
    private float speedOverGround;
    private boolean positionAccurate;
    private float longitude;
    private float latitude;
    private float courseOverGround;
    private int trueHeading;
    private int timeStamp;
    private ManeuverIndicator maneuverIndicator;
    private boolean raimFlag;
    private ICommunicationState radioStatus;

    public PositionReport(String messagePayload) {
        super(messagePayload);
        decode();
    }

    public void decode() {
        rateOfTurn = decodeRateOfTurn();
        speedOverGround = decodeSpeedOverGround();
        positionAccurate = decodePositionAccurate();
        longitude = decodeLongitude();
        latitude = decodeLatitude();
        courseOverGround = decodeCourseOverGround();
        trueHeading = decodeTrueHeading();
        timeStamp = decodeTimeStamp();
        maneuverIndicator = decodeManeuverIndicator();
        raimFlag = decodeRaimFlag();
        radioStatus = decodeRadioStatus();
        navigationStatus = decodeNavigationStatus();
    }

    public NavigationStatus decodeNavigationStatus() {
        return NavigationStatus.from(Decoders.toInteger(getMessagePayload().substring(38, 42)));
    }

    /**
     https://intapi.sciendo.com/pdf/10.5604/01.3001.0010.6747#:~:text=Rate%20of%20turn%200%20to,of%20Turn%20Indicator%20(TI)
     * */
    public int decodeRateOfTurn() {
        float rotSensor = Decoders.toFloat(getMessagePayload().substring(42, 50));
        if (-126f <= rotSensor && rotSensor <= 126f) {
            var sign = getMessagePayload().charAt(42) == '1' ? -1 : 1;
            var rotAis = Math.pow(rotSensor / 4.733, 2);
            return (int) Math.round(rotAis * sign);
        }
        if (rotSensor == -128) {
            return -128; // no turn rate information
        } else {
            return (int) rotSensor; // turning left/right at more than 5 degrees per 30 seconds (No TI available)
        }
        // todo rozne decodery wzkasuja rozne wartosci, nie wiem ktora jest poprawna, czy sa jakies specjalne wartosci?
        // wzor z dokumentacji: https://gpsd.gitlab.io/gpsd/AIVDM.html#_types_1_2_and_3_position_report_class_a
    }

    public float decodeSpeedOverGround() {
        return Decoders.toUnsignedFloat(getMessagePayload().substring(50, 60)) / 10f;
    }

    /**
     *
     * @return The position accuracy flag indicates the accuracy of the fix.
     * True indicates a DGPS-quality fix with an accuracy of < 10ms. False, the default,
     * indicates an unaugmented GNSS fix with accuracy > 10m.
     */
    public boolean decodePositionAccurate() {
        return Decoders.toBoolean(getMessagePayload().substring(60, 61));
    }

    /**
     *
     * @return Values up to +- 180 degrees, East is positive, West is negative. A value of 181 degrees (0x6791AC0 hex) indicates
     * that longitude is not available and is the default.
     */
    public float decodeLongitude() {
        var longitude = Decoders.toFloat(getMessagePayload().substring(61, 89));
        return longitude / 600000f;
    }

    /**
     *
     * @return Values up to +- 90 degrees, North is positive, South is negative. A value of 91 degrees (0x3412140 hex) indicates
     * that latitude is not available and is the default.
     */
    public float decodeLatitude() {
        var latitude = Decoders.toFloat(getMessagePayload().substring(89, 116));
        return latitude / 600000f;
    }

    /**
     *
     * @return Values up to 359.9 degrees. A value of 360.0 indicates that
     * COG is not available and is the default.
     */
    public float decodeCourseOverGround() {
        return Decoders.toUnsignedFloat(getMessagePayload().substring(116, 128)) / 10f;
    }

    /**
     *
     * @return 0 to 359 degrees, 511 = not available = default
     */
    public int decodeTrueHeading() {
        return Decoders.toUnsignedInteger(getMessagePayload().substring(128, 137));
    }

    /**
     *
     * @return Seconds in UTC timestamp should be 0-59, except for these special values:
     * <br>60 if time stamp is not available (default)
     * <br>61 if positioning system is in manual input mode
     * <br>62 if Electronic Position Fixing System operates in estimated (dead reckoning) mode,
     * <br>63 if the positioning system is inoperative.
     */
    public int decodeTimeStamp() {
        return Decoders.toUnsignedInteger(getMessagePayload().substring(137, 143));
    }

    /**
     *
     * @return 0 - No available <br> 1 - No special maneuver <br> 2 - Special maneuver (such as regional passing arrangement)
     */
    public ManeuverIndicator decodeManeuverIndicator() {
        return ManeuverIndicator.from((Decoders.toUnsignedInteger(getMessagePayload().substring(143, 145))));
    }

    /**
     *
     * @return False not in use (default) <br> True in use
     */
    public boolean decodeRaimFlag() {
        return Decoders.toBoolean(getMessagePayload().substring(148, 149));
    }

    /**
     *
     * @return Diagnostic information for the radio system.
     */
    public ICommunicationState decodeRadioStatus() {
        SyncState syncState = SyncState.from(Decoders.toUnsignedInteger(getMessagePayload().substring(149, 151)));
        if (this.decodeMessageType() == MessageType.PositionReportClassA || this.decodeMessageType() == MessageType.PositionReportClassAAssignedSchedule) {
            // SOTDMA communication state 1 SOTDMA communication state as described in ß 3.3.7.2.2
            return new SOTDMACommunicationState(syncState, Decoders.toUnsignedInteger(getMessagePayload().substring(151, 154)),
                    getMessagePayload().substring(154, 168));
        } else {
            // ITDMA communication state
            return new ITDMACommunicationState(syncState, Decoders.toUnsignedInteger(getMessagePayload().substring(151, 164)),
                    Decoders.toUnsignedInteger(getMessagePayload().substring(164, 167)),
                    Decoders.toBoolean(getMessagePayload().substring(167, 168)));
        }
    }
}
