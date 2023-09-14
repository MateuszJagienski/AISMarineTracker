package com.example.aismarinetracker.decoder.enums;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum ManeuverIndicator {
    NotAvailable(0),
    NoSpecialManeuver(1),
    SpecialManeuver(2);

    private final Integer code;
    ManeuverIndicator(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    private static final Map<Integer, ManeuverIndicator> enumMap = Stream.of(values())
            .collect(toMap(ManeuverIndicator::getCode, e -> e));

    public static ManeuverIndicator from(int code) {
        return enumMap.get(code);
    }
}
