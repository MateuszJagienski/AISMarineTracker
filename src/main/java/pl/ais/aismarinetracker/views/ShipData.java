package pl.ais.aismarinetracker.views;

import lombok.Getter;
import lombok.Setter;
import pl.ais.aismarinetracker.decoder.enums.NavigationStatus;
import pl.ais.aismarinetracker.decoder.enums.ShipType;
import pl.ais.aismarinetracker.decoder.exceptions.InvalidCoordinatesException;
import pl.ais.aismarinetracker.decoder.reports.*;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Getter
@Setter
public class ShipData {
    private NavigationStatus navigationStatus;
    private float latitude;
    private float longitude;
    private float speedOverGround;
    private float courseOverGround;
    private String trueHeading;
    private float draught;
    private String rateOfTurn;
    private String vesselName;
    private ShipType shipType;
    private String destination;
    private int mmsi;
    private static final Logger logger = Logger.getLogger(ShipData.class.getName());
    public ShipData(List<AisMessage> messages) throws InvalidCoordinatesException {
        messages.forEach(e -> {
            mmsi = e.getMMSI();
            if (e instanceof IDynamicPositionReport report) {
                latitude = report.getLatitude();
                longitude = report.getLongitude();
                speedOverGround = report.getSpeedOverGround();
                courseOverGround = report.getCourseOverGround();
            }
            if (e instanceof StaticAndVoyageRelatedData report) {
                vesselName = report.getVesselName();
                shipType = report.getShipType();
                draught = report.getDraught();
                destination = report.getDestination();

            }
            if (e instanceof PositionReport report) {
                navigationStatus = report.getNavigationStatus();
                rateOfTurn = report.getRateOfTurn() + "";
                trueHeading = report.getTrueHeading() + "";
            }
            if (e instanceof BaseStationReport report) {
                latitude = report.getLatitude();
                longitude = report.getLongitude();
            }
        });
        try {
            validate();
        } catch (Exception e) {
            var message= messages.stream().map(s -> Arrays.toString(s.getMessageRaw())).collect(Collectors.joining(", "));
            logger.info("Invalid message: " + message);
            logger.info("Validating message failed! " + e.getClass().getName());
            throw e;
        }

    }

    private void validate() throws InvalidCoordinatesException {
        if (latitude > 90 || longitude > 180 || latitude <= 0 || longitude <= 0) {
            throw new InvalidCoordinatesException();
        }
        if (shipType == null) {
            shipType = ShipType.NotAvailable;
        }
        if (destination == null) {
            destination = "Not available";
        }
        if (trueHeading == null || trueHeading.equals("511")) {
            trueHeading = "Not available";
        }
        if (navigationStatus == null) {
            navigationStatus = NavigationStatus.Undefined;
        }
    }
}