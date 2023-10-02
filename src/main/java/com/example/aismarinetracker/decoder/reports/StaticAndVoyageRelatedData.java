package com.example.aismarinetracker.decoder.reports;


import com.example.aismarinetracker.decoder.Decoders;
import com.example.aismarinetracker.decoder.enums.EPFD;
import com.example.aismarinetracker.decoder.enums.ShipType;
import lombok.Getter;

@Getter
public class StaticAndVoyageRelatedData extends AisMessage {

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

    public int decodeAisVersion() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(38, 40));
    }

    public int decodeImoNumber() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(40, 70));
    }

    public String decodeCallSign() {
        return Decoders.toAsciiString(getBinaryMessagePayload().substring(70, 112));
    }

    public String decodeVesselName() {
        return Decoders.toAsciiString(getBinaryMessagePayload().substring(112, 232));
    }

    public ShipType decodeShipType() {
        return ShipType.from(Decoders.toInteger(getBinaryMessagePayload().substring(232, 240)));
    }

    public int decodeDimensionToBow() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(240, 249));
    }

    public int decodeDimensionToStern() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(249, 258));
    }

    public int decodeDimensionToPort() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(258, 264));
    }

    public int decodeDimensionToStarboard() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(264, 270));
    }

    public EPFD decodeTypeOfEPFD() {
        return EPFD.from(Decoders.toInteger(getBinaryMessagePayload().substring(270, 274)));
    }

    public int decodeMonth() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(274, 278));
    }

    public int decodeDay() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(278, 283));
    }

    public int decodeHour() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(283, 288));
    }

    public int decodeMinute() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(288, 294));
    }

    public float decodeDraught() {
        return Decoders.toUnsignedFloat(getBinaryMessagePayload().substring(294, 302)) / 10f;
    }

    public String decodeDestination() {
        return Decoders.toAsciiString(getBinaryMessagePayload().substring(302, 422));
    }

    public boolean decodeDte() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(422, 423));
    }
}
