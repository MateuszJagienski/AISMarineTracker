package com.example.aismarinetracker.views;

import com.example.aismarinetracker.decoder.enums.NavigationStatus;
import com.example.aismarinetracker.decoder.enums.ShipType;
import com.example.aismarinetracker.decoder.reports.AisMessage;
import com.example.aismarinetracker.decoder.reports.ExtendedClassBEquipmentPositionReport;
import com.example.aismarinetracker.decoder.reports.PositionReport;
import com.example.aismarinetracker.decoder.reports.StaticAndVoyageRelatedData;
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
    private float heading;
    private float rateOfTurn;
    private float trueHeading;
    private String vesselName;
    private ShipType shipType;
    private int mmsi;
    public ShipData(List<AisMessage> messages) {
        messages.forEach(e -> {
            mmsi = e.getMMSI();
            if (e instanceof PositionReport report) {
                navigationStatus = report.getNavigationStatus();
                latitude = report.getLatitude();
                longitude = report.getLongitude();
                speedOverGround = report.getSpeedOverGround();
                courseOverGround = report.getCourseOverGround();
                rateOfTurn = report.getRateOfTurn();
            } else if (e instanceof StaticAndVoyageRelatedData report) {
                shipType = report.getShipType();
                vesselName = report.getVesselName();
            } else if (e instanceof ExtendedClassBEquipmentPositionReport report) {
                vesselName = report.getName();
            }
        });
    }
}