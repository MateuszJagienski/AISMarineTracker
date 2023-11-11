package com.example.aismarinetracker.decoder.reports;


import com.example.aismarinetracker.decoder.Decoders;
import com.example.aismarinetracker.decoder.enums.NavigationStatus;
import lombok.Getter;

@Getter
public class PositionReportForLongRangeApplications extends AisMessage {

    private boolean positionAccuracy;
    private boolean raimFlag;
    private NavigationStatus navigationStatus;
    private float longitude;
    private float latitude;
    private int courseOverGround;
    private float speedOverGround;
    private boolean GNSSPositionStatus;

    public PositionReportForLongRangeApplications(String messagePayload) {
        super(messagePayload);
        decode();
    }

    private void decode() {
        this.positionAccuracy = decodePositionAccuracy();
        this.raimFlag = decodeRaimFlag();
        this.navigationStatus = decodeNavigationStatus();
        this.longitude = decodeLongitude();
        this.latitude = decodeLatitude();
        this.courseOverGround = decodeCourseOverGround();
        this.speedOverGround = decodeSpeedOverGround();
        this.GNSSPositionStatus = decodeGNSSPositionStatus();
    }

    private boolean decodePositionAccuracy() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(38, 39));
    }

    private boolean decodeRaimFlag() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(39, 40));
    }

    private NavigationStatus decodeNavigationStatus() {
        return NavigationStatus.from(Decoders.toInteger(getBinaryMessagePayload().substring(40, 44)));
    }
    // todo check longitude and latitude
    private float decodeLongitude() {
        return Decoders.toFloat(getBinaryMessagePayload().substring(44, 62));
    }

    private float decodeLatitude() {
        return Decoders.toFloat(getBinaryMessagePayload().substring(62, 79));
    }

    private int decodeCourseOverGround() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(85, 94));
    }

    private float decodeSpeedOverGround() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(79, 85));
    }

    private boolean decodeGNSSPositionStatus() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(94, 95));
    }
}
