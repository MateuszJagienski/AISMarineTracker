package com.example.aismarinetracker.decoder.enums;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum MessageType {

    PositionReportClassA(1),
    PositionReportClassAAssignedSchedule(2),
    PositionReportClassAResponseToInterrogation(3),
    BaseStationReport(4),
    StaticAndVoyageRelatedData(5),
    BinaryAddressedMessage(6),
    BinaryAcknowledge(7),
    BinaryBroadcastMessage(8),
    StandardSARAircraftPositionReport(9),
    UTCAndDateInquiry(10),
    UTCAndDateResponse(11),
    AddressedSafetyRelatedMessage(12),
    SafetyRelatedAcknowledge(13),
    SafetyRelatedBroadcastMessage(14),
    Interrogation(15),
    AssignedModeCommand(16),
    DGNSSBinaryBroadcastMessage(17),
    StandardClassBCSPositionReport(18),
    ExtendedClassBEquipmentPositionReport(19),
    DataLinkManagement(20),
    AidToNavigationReport(21),
    ChannelManagement(22),
    GroupAssignmentCommand(23),
    StaticDataReport(24),
    SingleSlotBinaryMessage(25),
    MultipleSlotBinaryMessageWithCommunicationsState(26),
    PositionReportForLongRangeApplications(27),
    Error(-1);

    private final int code;
    public int getCode() {
        return code;
    }

    MessageType(int code) {
        this.code = code;
    }

    private static final Map<Integer, MessageType> map = Stream.of(values())
            .collect(toMap(MessageType::getCode, e -> e));

    public static MessageType from(int code) {
        return map.get(code) == null ? Error : map.get(code);
    }
}
