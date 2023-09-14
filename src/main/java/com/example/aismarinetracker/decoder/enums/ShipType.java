package com.example.aismarinetracker.decoder.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ShipType {
    NotAvailable(0),
    // 1-19 Reserved for future use
    WingInGround(20),
    WingInGroundHazardousCategoryA(21),
    WingInGroundHazardousCategoryB(22),
    WingInGroundHazardousCategoryC(23),
    WingInGroundHazardousCategoryD(24),
    // 25-29 WIG Reserved for future use
    Fishing(30),
    Towing(31),
    TowingLarge(32),
    DredgingOrUnderwaterOps(33),
    DivingOps(34),
    MilitaryOps(35),
    Sailing(36),
    PleasureCraft(37),
    // 38-39 Reserved for future use
    HighSpeedCraft(40),
    HighSpeedCraftHazardousCategoryA(41),
    HighSpeedCraftHazardousCategoryB(42),
    HighSpeedCraftHazardousCategoryC(43),
    HighSpeedCraftHazardousCategoryD(44),
    // 45-48 HSC Reserved for future use
    HighSpeedCraftNoAdditionalInformation(49),
    PilotVessel(50),
    SearchAndRescueVessel(51),
    Tug(52),
    PortTender(53),
    AntiPollutionEquipment(54),
    LawEnforcement(55),
    SpareLocalVessel1(56),
    SpareLocalVessel2(57),
    MedicalTransport(58),
    NoncombatantShip(59),
    Passenger(60), //60-69
    PassengerHazardousCategoryA(61),
    PassengerHazardousCategoryB(62),
    PassengerHazardousCategoryC(63),
    PassengerHazardousCategoryD(64),
    // 65-69 Passenger ships Reserved for future use
    Cargo(70), // 70-79
    CargoHazardousCategoryA(71),
    CargoHazardousCategoryB(72),
    CargoHazardousCategoryC(73),
    CargoHazardousCategoryD(74),
    // 75-78 Reserved for future use
    CargoNoAdditionalInformation(79),
    Tanker(80), // 80-89
    TankerHazardousCategoryA(81),
    TankerHazardousCategoryB(82),
    TankerHazardousCategoryC(83),
    TankerHazardousCategoryD(84),
    // 85-88 Tanker Reserved for future use
    TankerNoAdditionalInformation(89),
    OtherType(90), // 90-99
    OtherTypeHazardousCategoryA(91),
    OtherTypeHazardousCategoryB(92),
    OtherTypeHazardousCategoryC(93),
    OtherTypeHazardousCategoryD(94),
    // 95-98 Other type Reserved for future use
    OtherTypeNoAdditionalInformation(99),
    // 100-199 Reserved for future use treat as 0
    ;
    private final int code;
    private static final Map<Integer, ShipType> map = Stream.of(values()).
            collect(Collectors.toMap(ShipType::getCode, e -> e));
    public int getCode() {
        return code;
    }
    ShipType(int code) {
        this.code = code;
    }
    public static ShipType from(Integer integer) {
        if (integer > 99) integer = 0;  //Decoders should treat these like value 0 rather than throwing an exception until and
                                        // unless the controlled vocabulary is extended to include the unknown values.
        return map.get(integer) == null ? NotAvailable : map.get(integer);
    }
}
