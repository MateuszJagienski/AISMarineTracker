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

    public void decode() {
        this.positionAccuracy = decodePositionAccuracy();
        this.raimFlag = decodeRaimFlag();
        this.navigationStatus = decodeNavigationStatus();
        this.longitude = decodeLongitude();
        this.latitude = decodeLatitude();
        this.courseOverGround = decodeCourseOverGround();
        this.speedOverGround = decodeSpeedOverGround();
        this.GNSSPositionStatus = decodeGNSSPositionStatus();
    }

    public boolean decodePositionAccuracy() {
        return Decoders.toBoolean(getMessagePayload().substring(38, 39));
    }

    public boolean decodeRaimFlag() {
        return Decoders.toBoolean(getMessagePayload().substring(39, 40));
    }

    public NavigationStatus decodeNavigationStatus() {
        return NavigationStatus.from(Decoders.toInteger(getMessagePayload().substring(40, 44)));
    }
    // todo check longitude and latitude
    public float decodeLongitude() {
        return Decoders.toFloat(getMessagePayload().substring(44, 62));
    }

    public float decodeLatitude() {
        return Decoders.toFloat(getMessagePayload().substring(62, 79));
    }

    public int decodeCourseOverGround() {
        return Decoders.toUnsignedInteger(getMessagePayload().substring(85, 94));
    }

    public float decodeSpeedOverGround() {
        return Decoders.toUnsignedInteger(getMessagePayload().substring(79, 85));
    }

    public boolean decodeGNSSPositionStatus() {
        return Decoders.toBoolean(getMessagePayload().substring(94, 95));
    }
}
