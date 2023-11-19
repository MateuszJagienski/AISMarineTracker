package com.example.aismarinetracker.views;

import com.example.aismarinetracker.decoder.enums.NavigationStatus;
import com.example.aismarinetracker.decoder.enums.ShipType;
import com.example.aismarinetracker.decoder.exceptions.InvalidCoordinatesException;
import com.example.aismarinetracker.decoder.reports.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.logging.Logger;

@Getter
@Setter
public class ShipData {
    private NavigationStatus navigationStatus;
    private float latitude;
    private float longitude;
    private float speedOverGround;
    private float courseOverGround;
    private float trueHeading;
    private float draught;
    private float rateOfTurn;
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
                rateOfTurn = report.getRateOfTurn();
                trueHeading = report.getTrueHeading();
            }
            if (e instanceof BaseStationReport report) {
                latitude = report.getLatitude();
                longitude = report.getLongitude();
            }
        });
        try {
            validate();
        } catch (Exception e) {
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
        if (trueHeading == 511) {
            //
        }
        if (navigationStatus == null) {
            navigationStatus = NavigationStatus.Undefined;
        }
    }
}