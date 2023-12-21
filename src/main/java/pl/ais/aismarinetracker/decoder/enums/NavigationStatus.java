package pl.ais.aismarinetracker.decoder.enums;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum NavigationStatus {
    UnderwayUsingEngine(0),
    AtAnchor(1),
    NotUnderCommand(2),
    RestrictedManoeuvrability(3),
    ConstrainedByHerDraught(4),
    Moored(5),
    Aground(6),
    EngagedInFishing(7),
    UnderWaySailing(8),
    ReservedForFutureAmendmentHSC(9),
    ReservedForFutureAmendmentWIG(10),
    ReservedForFutureUse11(11),
    ReservedForFutureUse12(12),
    ReservedForFutureUse13(13),
    ReservedForFutureUse14(14),
    Undefined(15);
    private final int code;

    public int getCode() {
        return code;
    }

    NavigationStatus(int code) {
        this.code = code;
    }

    private static final Map<Integer, NavigationStatus> map = Stream.of(values())
            .collect(toMap(NavigationStatus::getCode, e -> e));

    public static NavigationStatus from(int code) {
        return map.get(code) == null ? Undefined : map.get(code);
    }

}
