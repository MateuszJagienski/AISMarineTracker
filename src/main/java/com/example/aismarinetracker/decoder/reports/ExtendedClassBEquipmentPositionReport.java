package com.example.aismarinetracker.decoder.reports;

import com.example.aismarinetracker.decoder.Decoders;
import com.example.aismarinetracker.decoder.enums.EPFD;
import com.example.aismarinetracker.decoder.enums.ShipType;
import lombok.Getter;

@Getter
public class ExtendedClassBEquipmentPositionReport extends AisMessage implements IDynamicPositionReport {

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

    private void decode() {
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

    private int decodeRegionalReserved() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(38, 46));
    }

    private int decodeRegionalReserved1() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(139, 143));
    }

    private float decodeSpeedOverGround() {
        return Decoders.toUnsignedFloat(getBinaryMessagePayload().substring(46, 56)) / 10f;
    }

    public boolean isPositionAccurate() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(56, 57));
    }

    private float decodeLongitude() {
        var longitude = Decoders.toFloat(getBinaryMessagePayload().substring(57, 85));
        return longitude / 600000f;
    }

    private float decodeLatitude() {
        var latitude = Decoders.toFloat(getBinaryMessagePayload().substring(85, 112));
        return latitude / 600000f;
    }

    private float decodeCourseOverGround() {
        return Decoders.toUnsignedFloat(getBinaryMessagePayload().substring(112, 124)) / 10f;
    }

    private int decodeTrueHeading() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(124, 133));
    }
    private int decodeTimeStamp() {
        return Decoders.toInteger(getBinaryMessagePayload().substring(133, 139));
    }

    private String decodeName() {
        return Decoders.toAsciiString(getBinaryMessagePayload().substring(143, 263));
    }

    private ShipType decodeShipType() {
        return ShipType.from(Decoders.toInteger(getBinaryMessagePayload().substring(263, 271)));
    }

    private int decodeDimensionToBow() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(271, 280));
    }

    private int decodeDimensionToStern() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(280, 289));
    }

    private int decodeDimensionToPort() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(289, 295));
    }

    private int decodeDimensionToStarboard() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(295, 301));
    }

    private EPFD decodeTypeOfEPFD() {
        return EPFD.from(Decoders.toInteger(getBinaryMessagePayload().substring(301, 305)));
    }

    private boolean decodeRaimFlag() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(305, 306));
    }

    private boolean decodeDataTerminalReady() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(306, 307));
    }

    private boolean decodeAssignedModeFlag() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(307, 308));
    }
}
