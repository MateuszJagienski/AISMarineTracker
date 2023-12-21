package pl.ais.aismarinetracker.decoder.enums;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum SyncState {
    UTCDirect(0),
    UTCIndirect(1),
    BaseDirect(2),
    BaseIndirect(3);

    private final int code;
    public int getCode() {
        return code;
    }
    SyncState(int code) {
        this.code = code;
    }

    public static final Map<Integer, SyncState> map = Stream.of(values())
            .collect(toMap(SyncState::getCode, e -> e));

    public static SyncState from(int code) {
        return map.get(code);
    }
}
