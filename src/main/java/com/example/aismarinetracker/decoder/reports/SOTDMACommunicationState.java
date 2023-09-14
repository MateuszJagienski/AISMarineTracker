package com.example.aismarinetracker.decoder.reports;

import com.example.aismarinetracker.decoder.Decoders;
import com.example.aismarinetracker.decoder.enums.SyncState;
import lombok.Getter;

@Getter
public class SOTDMACommunicationState implements ICommunicationState {

    private SyncState syncState;
    private int slotTimeout;
    private String subMessage;
    private Integer receivedStations = null;
    private Integer slotNumber = null;
    private Integer utcHour = null;
    private Integer utcMinute = null;
    private Integer slotOffset = null;

    public SOTDMACommunicationState(SyncState syncState, int slotTimeout, String subMessage) {
        this.syncState = syncState;
        this.slotTimeout = slotTimeout;
        this.subMessage = subMessage;


        switch (slotTimeout) {
            case 3, 5, 7 -> receivedStations = Decoders.toUnsignedInteger(subMessage);
            case 2, 4, 6 -> slotNumber = Decoders.toUnsignedInteger(subMessage);
            case 1 -> {
                utcHour = Decoders.toUnsignedInteger(subMessage.substring(0, 5));
                utcMinute = Decoders.toUnsignedInteger(subMessage.substring(5, 12));
            }
            case 0 -> slotOffset = Decoders.toUnsignedInteger(subMessage);
        }
    }

    @Override
    public SyncState getSyncState() {
        return syncState;
    }
}
