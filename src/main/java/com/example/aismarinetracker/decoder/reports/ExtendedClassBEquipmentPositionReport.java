package com.example.aismarinetracker.decoder.reports;

import com.example.aismarinetracker.decoder.Decoders;
import com.example.aismarinetracker.decoder.enums.EPFD;
import com.example.aismarinetracker.decoder.enums.ShipType;
import lombok.Getter;

@Getter
public class ExtendedClassBEquipmentPositionReport extends AisMessage {

    private int regionalReserved;
    private int regionalReserved1;
    private float speedOverGround;
    private boolean positionAccurate;
    private float longitude;
    private float latitude;
    private float courseOverGround;
    private int trueHeading;
    private int timeStamp;
    private String name;
    private ShipType shipType;
    private int dimensionToBow;
    private int dimensionToStern;
    private int dimensionToPort;
    private int dimensionToStarboard;
    private EPFD typeOfEPFD;
    private boolean raimFlag;
    private boolean dataTerminalReady;
    private boolean assignedModeFlag;


    public ExtendedClassBEquipmentPositionReport(String messagePayload) {
        super(messagePayload);
        decode();
    }

    public void decode() {
        regionalReserved = decodeRegionalReserved();
        regionalReserved1 = decodeRegionalReserved1();
        speedOverGround = decodeSpeedOverGround();
        positionAccurate = isPositionAccurate();
        longitude = decodeLongitude();
        latitude = decodeLatitude();
        courseOverGround = decodeCourseOverGround();
        trueHeading = decodeTrueHeading();
        timeStamp = decodeTimeStamp();
        name = decodeName();
        shipType = decodeShipType();
        dimensionToBow = decodeDimensionToBow();
        dimensionToStern = decodeDimensionToStern();
        dimensionToPort = decodeDimensionToPort();
        dimensionToStarboard = decodeDimensionToStarboard();
        typeOfEPFD = decodeTypeOfEPFD();
        raimFlag = decodeRaimFlag();
        dataTerminalReady = decodeDataTerminalReady();
        assignedModeFlag = decodeAssignedModeFlag();
    }

    public int decodeRegionalReserved() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(38, 46));
    }

    public int decodeRegionalReserved1() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(139, 143));
    }

    public float decodeSpeedOverGround() {
        return Decoders.toUnsignedFloat(getBinaryMessagePayload().substring(46, 56)) / 10f;
    }

    public boolean isPositionAccurate() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(56, 57));
    }

    public float decodeLongitude() {
        var longitude = Decoders.toFloat(getBinaryMessagePayload().substring(57, 85));
        return longitude / 600000f;
    }

    public float decodeLatitude() {
        var latitude = Decoders.toFloat(getBinaryMessagePayload().substring(85, 112));
        return latitude / 600000f;
    }

    public float decodeCourseOverGround() {
        return Decoders.toUnsignedFloat(getBinaryMessagePayload().substring(112, 124)) / 10f;
    }

    public int decodeTrueHeading() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(124, 133));
    }
    public int decodeTimeStamp() {
        return Decoders.toInteger(getBinaryMessagePayload().substring(133, 139));
    }

    public String decodeName() {
        return Decoders.toAsciiString(getBinaryMessagePayload().substring(143, 263));
    }

    public ShipType decodeShipType() {
        return ShipType.from(Decoders.toInteger(getBinaryMessagePayload().substring(263, 271)));
    }

    public int decodeDimensionToBow() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(271, 280));
    }

    public int decodeDimensionToStern() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(280, 289));
    }

    public int decodeDimensionToPort() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(289, 295));
    }

    public int decodeDimensionToStarboard() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(295, 301));
    }

    public EPFD decodeTypeOfEPFD() {
        return EPFD.from(Decoders.toInteger(getBinaryMessagePayload().substring(301, 305)));
    }

    public boolean decodeRaimFlag() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(305, 306));
    }

    public boolean decodeDataTerminalReady() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(306, 307));
    }

    public boolean decodeAssignedModeFlag() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(307, 308));
    }
}
