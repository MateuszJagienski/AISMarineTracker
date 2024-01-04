package pl.ais.aismarinetracker.decoder.reports;


import pl.ais.aismarinetracker.decoder.Decoders;
import pl.ais.aismarinetracker.decoder.enums.MessageType;
import pl.ais.aismarinetracker.decoder.exceptions.UnsupportedMessageType;
import org.springframework.stereotype.Service;

public class AisMessageFactory {

    public static AisMessage createAisMessage(String messagePayload) throws UnsupportedMessageType {
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