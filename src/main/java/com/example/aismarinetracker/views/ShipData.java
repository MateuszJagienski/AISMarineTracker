package com.example.aismarinetracker.views;

import com.example.aismarinetracker.decoder.AisService;
import com.example.aismarinetracker.decoder.enums.NavigationStatus;
import com.example.aismarinetracker.decoder.enums.ShipType;
import com.example.aismarinetracker.decoder.reports.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    public ShipData(List<AisMessage> messages) throws Exception {
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
        validate();

    }

    private void validate() {
        if (latitude > 90 || longitude > 180) {
            //throw new RuntimeException("Invalid coordinates");
            // todo
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