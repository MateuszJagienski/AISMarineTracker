package com.example.aismarinetracker.decoder.reports;


import com.example.aismarinetracker.decoder.Decoders;
import com.example.aismarinetracker.decoder.enums.EPFD;
import com.example.aismarinetracker.decoder.enums.ShipType;
import lombok.Getter;

@Getter
public class StaticAndVoyageRelatedData extends AisMessage implements IHasDimensions {

    private int aisVersion;
    private int imoNumber;
    private String callSign;
    private String vesselName;
    private ShipType shipType;
    private int dimensionToBow;
    private int dimensionToStern;
    private int dimensionToPort;
    private int dimensionToStarboard;
    private EPFD typeOfEPFD;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private float draught;
    private String destination;
    private boolean dte;


    public StaticAndVoyageRelatedData(String messagePayload) {
        super(messagePayload);
        decode();
    }

    private void decode() {
        this.aisVersion = decodeAisVersion();
        this.imoNumber = decodeImoNumber();
        this.callSign = decodeCallSign();
        this.vesselName = decodeVesselName();
        this.shipType = decodeShipType();
        this.dimensionToBow = decodeDimensionToBow();
        this.dimensionToStern = decodeDimensionToStern();
        this.dimensionToPort = decodeDimensionToPort();
        this.dimensionToStarboard = decodeDimensionToStarboard();
        this.typeOfEPFD = decodeTypeOfEPFD();
        this.month = decodeMonth();
        this.day = decodeDay();
        this.hour = decodeHour();
        this.minute = decodeMinute();
        this.draught = decodeDraught();
        this.destination = decodeDestination();
        this.dte = decodeDte();
    }

    private int decodeAisVersion() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(38, 40));
    }

    private int decodeImoNumber() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(40, 70));
    }

    private String decodeCallSign() {
        return Decoders.toAsciiString(getBinaryMessagePayload().substring(70, 112));
    }

    private String decodeVesselName() {
        return Decoders.toAsciiString(getBinaryMessagePayload().substring(112, 232));
    }

    private ShipType decodeShipType() {
        return ShipType.from(Decoders.toInteger(getBinaryMessagePayload().substring(232, 240)));
    }

    private int decodeDimensionToBow() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(240, 249));
    }

    private int decodeDimensionToStern() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(249, 258));
    }

    private int decodeDimensionToPort() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(258, 264));
    }

    private int decodeDimensionToStarboard() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(264, 270));
    }

    private EPFD decodeTypeOfEPFD() {
        return EPFD.from(Decoders.toInteger(getBinaryMessagePayload().substring(270, 274)));
    }

    private int decodeMonth() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(274, 278));
    }

    private int decodeDay() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(278, 283));
    }

    private int decodeHour() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(283, 288));
    }

    private int decodeMinute() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(288, 294));
    }

    private float decodeDraught() {
        return Decoders.toUnsignedFloat(getBinaryMessagePayload().substring(294, 302)) / 10f;
    }

    private String decodeDestination() {
        return Decoders.toAsciiString(getBinaryMessagePayload().substring(302, 422));
    }

    private boolean decodeDte() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(422, 423));
    }
}
