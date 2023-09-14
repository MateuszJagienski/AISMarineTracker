package com.example.aismarinetracker.decoder.reports;

import com.example.aismarinetracker.decoder.enums.SyncState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ITDMACommunicationState implements ICommunicationState {
    private SyncState syncState;
    private int slotIncrement;
    private int numberOfSlots;
    private boolean keepFlag;

    @Override
    public SyncState getSyncState() {
        return syncState;
    }
}
