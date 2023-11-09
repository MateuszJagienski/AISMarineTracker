package com.example.aismarinetracker.decoder.reports;


import com.example.aismarinetracker.decoder.Decoders;
import com.example.aismarinetracker.decoder.enums.SyncState;
import lombok.Getter;

@Getter
public class StandardClassBCSPositionReport extends AisMessage implements IDynamicPositionReport {

    private float speedOverGround;
    private boolean positionAccurate;
    private float longitude;
    private float latitude;
    private float courseOverGround;
    private int trueHeading;
    private int timeStamp;
    private boolean csUnit;
    private boolean display;
    private boolean dsc;
    private boolean band;
    private boolean message22;
    private boolean assigned;
    private boolean raimFlag;
    private ICommunicationState radioStatus;

    public StandardClassBCSPositionReport(String messagePayload) {
        super(messagePayload);
        decode();
    }

    public void decode() {
        this.speedOverGround = decodeSpeedOverGround();
        this.positionAccurate = decodePositionAccurate();
        this.longitude = decodeLongitude();
        this.latitude = decodeLatitude();
        this.courseOverGround = decodeCourseOverGround();
        this.trueHeading = decodeTrueHeading();
        this.timeStamp = decodeTimeStamp();
        this.csUnit = decodeCsUnit();
        this.display = decodeDisplay();
        this.dsc = decodeDsc();
        this.band = decodeBand();
        this.message22 = decodeMessage22();
        this.assigned = decodeAssigned();
        this.raimFlag = decodeRaimFlag();
        this.radioStatus = decodeRadioStatus();
    }

    public float decodeSpeedOverGround() {
        return Decoders.toUnsignedFloat(getBinaryMessagePayload().substring(46, 56)) / 10;
    }

    public boolean decodePositionAccurate() {
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
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(133, 139));
    }

    public boolean decodeCsUnit() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(141, 142));
    }

    public boolean decodeDisplay() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(142, 143));
    }

    public boolean decodeDsc() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(143, 144));
    }

    public boolean decodeBand() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(144, 145));
    }

    public boolean decodeMessage22() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(145, 146));
    }

    public boolean decodeAssigned() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(146, 147));
    }

    public boolean decodeRaimFlag() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(147, 148));
    }

    public ICommunicationState decodeRadioStatus() {
        var selectorFlag = Decoders.toBoolean(getBinaryMessagePayload().substring(148, 149));
        SyncState syncState = SyncState.from(Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(149, 151)));
        if (selectorFlag) {
            // ITDMA communication state
            return new ITDMACommunicationState(syncState, Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(151, 164)),
                    Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(164, 167)),
                    Decoders.toBoolean(getBinaryMessagePayload().substring(167, 168)));
        } else {
            // SOTDMA communication state
            return new SOTDMACommunicationState(syncState, Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(151, 154)),
                    getBinaryMessagePayload().substring(154, 168));
        }
    }
}
