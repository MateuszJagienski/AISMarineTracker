package com.example.aismarinetracker.views;


import com.example.aismarinetracker.decoder.enums.ShipType;
import com.example.aismarinetracker.decoder.reports.AisMessage;
import com.example.aismarinetracker.decoder.reports.PositionReport;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import software.xdev.vaadin.maps.leaflet.flow.data.LPoint;
import software.xdev.vaadin.maps.leaflet.flow.data.LPolyline;

import java.util.ArrayList;
import java.util.List;

public class PopupShip extends VerticalLayout {
    private ShipData shipData;
    public PopupShip() {
        super();
        var vlStyle = this.getStyle();
        vlStyle.set("z-index", "2");
        vlStyle.set("width", "320px");
        vlStyle.set("right", "0px");
        vlStyle.setPosition(Style.Position.FIXED);
        this.setSpacing(false);
        this.setPadding(false);
    }

    public void showOverlay(ShipData shipData) {
        closeOverlay();
        this.shipData = shipData;
        this.add(createOverlayView());
    }

    public void closeOverlay() {
        this.removeAll();
    }

    public VerticalLayout createOverlayView() {
        var vl = new VerticalLayout();
        var vlStyle = vl.getStyle();
        vlStyle.set("background-color", "rgba(0,0,0,0.8)");
        vl.setSpacing(false);
        vl.setPadding(false);

        // hl background image
        var hl = new HorizontalLayout();
        hl.setWidth("100%");
        if (shipData.getVesselName() == null) {
            shipData.setVesselName(String.valueOf(shipData.getMmsi()));
        }
        var vlName = new VerticalLayout();
        vlName.setPadding(false);
        vlName.setSpacing(false);
        var shipName = "<div><strong>Ship name:</strong>" + shipData.getVesselName() + "</div>";
        var name = new Html(shipName);
        name.setId("ship_name");
        if (shipData.getShipType() == null) {
            shipData.setShipType(ShipType.NotAvailable);
        }
        System.out.println("shipData.getShipType(): " + shipData.getShipType());
        var shipType = "<div><strong>Ship type:</strong>" + shipData.getShipType() + "</div>";

        var type = new Html(shipType);
        type.setId("ship_type");
        vlName.add(name, type);
        hl.add(vlName);
        hl.setAlignItems(FlexComponent.Alignment.CENTER);
        hl.getStyle().setDisplay(Style.Display.FLEX);
        hl.getStyle().set("justify-content", "space-between");
        hl.setWidth("100%");
        var closeButton = new Button();
        Icon icon = new Icon("lumo", "cross");
        closeButton.setIcon(icon);
        closeButton.getStyle().setMargin("auto");
        closeButton.getStyle().set("margin-right", "10px");
        closeButton.addClickListener(event -> {
            closeOverlay();
        });
        hl.add(closeButton);

        var shipImage = new Image("img/720x480_Vessel_on_sea.jpg", "ship");
        shipImage.setHeight(225, Unit.PIXELS);
        shipImage.setWidth(320, Unit.PIXELS);

        var navigationStatus = "<div><strong>Navigation status: </strong>" + shipData.getNavigationStatus() + "</div>";
        var status = new Html(navigationStatus);
        status.setId("overlay_text");

        var speedOverGround = "<div><strong>Speed over ground: </strong>" + shipData.getSpeedOverGround() + " knots</div>";
        var speed = new Html(speedOverGround);
        speed.setId("overlay_text");

        var courseOverGround = "<div><strong>Course over ground: </strong>" + shipData.getCourseOverGround() + "</div>";
        var course = new Html(courseOverGround);
        course.setId("overlay_text");

        var heading = "<div><strong>Heading: </strong>" + shipData.getTrueHeading() + "</div>";
        var shipHeading = new Html(heading);
        shipHeading.setId("overlay_text");

        var latitude = "<div><strong>Latitude: </strong>" + shipData.getLatitude() + "</div>";
        var shipLatitude = new Html(latitude);
        shipLatitude.setId("overlay_text");

        var longitude = "<div><strong>Longitude: </strong>" + shipData.getLongitude() + "</div>";
        var shipLongitude = new Html(longitude);
        shipLongitude.setId("overlay_text");

        var trackButton = new Button("Track");
        var trackIcon = new Icon(VaadinIcon.ANCHOR);
        trackButton.setIcon(trackIcon);
        trackButton.addClickListener(event -> {
            drawLine(shipData);
        });

        vl.add(hl, shipImage, status, speed, course, shipHeading, shipLatitude, shipLongitude, trackButton);
        return vl;
    }

    private void drawLine(ShipData shipData) {
//        var points = new ArrayList<LPoint>();
//        var currentReport = MapView.getCurrentReports();
//        var mmsi = shipData.getMmsi();
//        for (int i = 0; i < currentReport; i++) {
//            var reports = reportsContainers.get(i);
//            var report = reports.getReports().get(mmsi);
//            if (report == null) continue;
//            report.forEach(e -> {
//                if (e instanceof PositionReport r) {
//                    points.add(new LPoint(r.getLatitude(), r.getLongitude()));
//                }
//            });
//        }
//        var polyline = new LPolyline(points);
//        polyline.setNoClip(true);
//        this.map.addLComponents(polyline);
    }
}
