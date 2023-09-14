package com.example.aismarinetracker.decoder.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EPFD {
    Undefined(0),
    GPS(1),
    GLONASS(2),
    CombinedGPSGLONASS(3),
    LoranC(4),
    Chayka(5),
    IntegratedNavigationSystem(6),
    Surveyed(7),
    Galileo(8);

    private final int code;

    public int getCode() { return code; }

    private static final Map<Integer, EPFD> map = Stream.of(values()).
            collect(Collectors.toMap(EPFD::getCode, e -> e));

    EPFD(int code) {
        this.code = code;
    }

    public static EPFD from(Integer code) {
        return map.get(code) == null ? EPFD.Undefined : map.get(code);
    }

}
