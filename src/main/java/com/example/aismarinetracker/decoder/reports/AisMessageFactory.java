package com.example.aismarinetracker.decoder.reports;


import com.example.aismarinetracker.decoder.Decoders;
import com.example.aismarinetracker.decoder.enums.MessageType;
import com.example.aismarinetracker.decoder.exceptions.UnsupportedMessageType;

public class AisMessageFactory {

    public AisMessage createAisMessage(String messagePayload) {
        return switch (MessageType.from(Decoders.toUnsignedInteger(messagePayload.substring(0, 6)))) {
            case PositionReportClassA -> new PositionReportClassAScheduled(messagePayload); // type 1
            case PositionReportClassAAssignedSchedule -> new PositionReportClassA(messagePayload); // type 2
            case PositionReportClassAResponseToInterrogation -> new PositionReportClassAResponseToInterrogation(messagePayload); // type 3
            case BaseStationReport -> new BaseStationReport(messagePayload); // type 4
            case StaticAndVoyageRelatedData -> new StaticAndVoyageRelatedData(messagePayload); // type 5
            case StandardClassBCSPositionReport -> new StandardClassBCSPositionReport(messagePayload); // type 18
            case ExtendedClassBEquipmentPositionReport -> new ExtendedClassBEquipmentPositionReport(messagePayload); // type 19
            case PositionReportForLongRangeApplications -> new PositionReportForLongRangeApplications(messagePayload); // type 27
            default -> throw new UnsupportedMessageType();
        };
    }
}