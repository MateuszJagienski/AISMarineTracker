package pl.ais.aismarinetracker.decoder.reports;

import pl.ais.aismarinetracker.decoder.Decoders;
import pl.ais.aismarinetracker.decoder.enums.SyncState;
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

    @Override
    public String toString() {
        return """
                SOTDMACommunicationState{
                    syncState=%s,
                    slotTimeout=%d,
                    subMessage='%s',
                    receivedStations=%d,
                    slotNumber=%d,
                    utcHour=%d,
                    utcMinute=%d,
                    slotOffset=%d
                }
                """.formatted(syncState, slotTimeout, subMessage, receivedStations, slotNumber, utcHour, utcMinute, slotOffset);
    }
}
