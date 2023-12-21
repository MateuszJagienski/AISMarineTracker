package pl.ais.aismarinetracker.decoder.reports;


import pl.ais.aismarinetracker.decoder.Decoders;
import pl.ais.aismarinetracker.decoder.enums.EPFD;
import pl.ais.aismarinetracker.decoder.enums.SyncState;
import lombok.Getter;

@Getter
public class BaseStationReport extends AisMessage implements IPositionReport {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private boolean fixQuality;
    private float longitude;
    private float latitude;
    private EPFD typeOfEPFD;
    private boolean raimFlag;
    private ICommunicationState SOTDMAState;
    public BaseStationReport(String messagePayload) {
        super(messagePayload);
        decode();
    }

    private void decode() {
        this.raimFlag = decodeRaimFlag();
        this.fixQuality = decodeFixQuality();
        this.longitude = decodeLongitude();
        this.latitude = decodeLatitude();
        this.typeOfEPFD = decodeTypeOfEPFD();
        this.year = decodeYear();
        this.month = decodeMonth();
        this.day = decodeDay();
        this.hour = decodeHour();
        this.minute = decodeMinute();
        this.second = decodeSecond();
        this.SOTDMAState = decodeSOTDMAState();
    }

    private int decodeYear() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(38, 52)); // todo dlaczego tak duzo bitow?
    }

    private int decodeMonth() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(52, 56));
    }

    private int decodeDay() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(56, 61));
    }

    private int decodeHour() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(61, 66));
    }

    private int decodeMinute() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(66, 72));
    }

    private int decodeSecond() {
        return Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(72, 78));
    }

    private boolean decodeFixQuality() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(78, 79));
    }

    private float decodeLongitude() {
        var longitude = Decoders.toFloat(getBinaryMessagePayload().substring(79, 107));
        return longitude / 600000f;
    }

    private float decodeLatitude() {
        var latitude = Decoders.toFloat(getBinaryMessagePayload().substring(107, 134));
        return latitude / 600000f;
    }

    private EPFD decodeTypeOfEPFD() {
        return EPFD.from(Decoders.toInteger(getBinaryMessagePayload().substring(134, 138)));
    }

    private boolean decodeRaimFlag() {
        return Decoders.toBoolean(getBinaryMessagePayload().substring(148, 149));
    }

    private ICommunicationState decodeSOTDMAState() {
        SyncState syncState = SyncState.from(Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(149, 151)));
        return new SOTDMACommunicationState(syncState, Decoders.toUnsignedInteger(getBinaryMessagePayload().substring(151, 154)),
                getBinaryMessagePayload().substring(154, 168));
    }
}
