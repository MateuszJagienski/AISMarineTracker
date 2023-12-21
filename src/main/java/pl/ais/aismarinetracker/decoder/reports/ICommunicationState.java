package pl.ais.aismarinetracker.decoder.reports;


import pl.ais.aismarinetracker.decoder.enums.SyncState;

public interface ICommunicationState {
    SyncState getSyncState();
}
