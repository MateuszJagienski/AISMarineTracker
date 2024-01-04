package pl.ais.aismarinetracker.decoder.reports;


import pl.ais.aismarinetracker.decoder.Decoders;
import pl.ais.aismarinetracker.decoder.enums.SyncState;
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

    private void decode() {
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

    private float decodeSpeedOverGround() {
        return Decoders.toUnsignedFloat(getBinaryMessagePayload().substring(46, 56)) / 10;
    }

    private boolean decodePositionAccurate() {
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
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(133, 139));
    }

    private boolean decodeCsUnit() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(141, 142));
    }

    private boolean decodeDisplay() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(142, 143));
    }

    private boolean decodeDsc() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(143, 144));
    }

    private boolean decodeBand() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(144, 145));
    }

    private boolean decodeMessage22() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(145, 146));
    }

    private boolean decodeAssigned() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(146, 147));
    }

    private boolean decodeRaimFlag() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(147, 148));
    }

    private ICommunicationState decodeRadioStatus() {
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