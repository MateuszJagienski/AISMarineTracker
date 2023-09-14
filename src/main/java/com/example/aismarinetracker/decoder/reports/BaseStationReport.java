package com.example.aismarinetracker.decoder.reports;


import com.example.aismarinetracker.decoder.Decoders;
import com.example.aismarinetracker.decoder.enums.EPFD;
import com.example.aismarinetracker.decoder.enums.SyncState;
import lombok.Getter;

@Getter
public class BaseStationReport extends AisMessage {

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

    public int decodeYear() {
        return Decoders.toUnsignedInteger(getMessagePayload().substring(38, 52)); // todo dlaczego tak duzo bitow?
    }

    public int decodeMonth() {
        return Decoders.toUnsignedInteger(getMessagePayload().substring(52, 56));
    }

    public int decodeDay() {
        return Decoders.toUnsignedInteger(getMessagePayload().substring(56, 61));
    }

    public int decodeHour() {
        return Decoders.toUnsignedInteger(getMessagePayload().substring(61, 66));
    }

    public int decodeMinute() {
        return Decoders.toUnsignedInteger(getMessagePayload().substring(66, 72));
    }

    public int decodeSecond() {
        return Decoders.toUnsignedInteger(getMessagePayload().substring(72, 78));
    }

    public boolean decodeFixQuality() {
        return Decoders.toBoolean(getMessagePayload().substring(78, 79));
    }

    public float decodeLongitude() {
        var longitude = Decoders.toFloat(getMessagePayload().substring(79, 107));
        return longitude / 600000f;
    }

    public float decodeLatitude() {
        var latitude = Decoders.toFloat(getMessagePayload().substring(107, 134));
        return latitude / 600000f;
    }

    public EPFD decodeTypeOfEPFD() {
        return EPFD.from(Decoders.toInteger(getMessagePayload().substring(134, 138)));
    }

    public boolean decodeRaimFlag() {
        return Decoders.toBoolean(getMessagePayload().substring(148, 149));
    }

    public ICommunicationState decodeSOTDMAState() {
        SyncState syncState = SyncState.from(Decoders.toUnsignedInteger(getMessagePayload().substring(149, 151)));
        return new SOTDMACommunicationState(syncState, Decoders.toUnsignedInteger(getMessagePayload().substring(151, 154)),
                getMessagePayload().substring(154, 168));
    }
}
