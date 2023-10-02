package com.example.aismarinetracker.views;


import com.example.aismarinetracker.decoder.AisHandler;
import com.example.aismarinetracker.decoder.enums.ShipType;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.theme.Theme;

import java.util.List;

public class PopupShip extends VerticalLayout {
    private ShipData shipData;
    public PopupShip() {
        super();
        AisHandler aisHandler = new AisHandler();

        shipData = new ShipData(List.of(aisHandler.handleAisMessage("!AIVDM,1,1,,A,13q5BjhP00Q1IFHNlMJP0?v824H0,0*1D")));
        createOverlayView();
        closeOverlay();

    }

    public void showOverlay(ShipData shipData) {
        this.setVisible(true);
        setData(shipData);
    }

    public void closeOverlay() {
        this.setVisible(false);
    }

    private void setData(ShipData shipData) {

    }

    public void createOverlayView() {
        // outside element for closing
        // overlay hl with shipData
        // shipData: name, image, speed, course, type, drought, destination, eta, navigation status
        var vlStyle = this.getStyle();


        vlStyle.set("z-index", "2");
        vlStyle.setWidth("320px");
        vlStyle.setRight("0");
        vlStyle.setPosition(Style.Position.FIXED);
        vlStyle.setHeight("100%");
        vlStyle.set("background-color", "rgba(0,0,0,0.8)");
        //this.setWidthFull();
        this.setSpacing(false);
        this.setPadding(false);
        //this.setAlignItems(FlexComponent.Alignment.CENTER);

        // hl background image
        var hl = new HorizontalLayout();
        hl.setWidth("100%");
        if (shipData.getVesselName() == null) {
            shipData.setVesselName(String.valueOf(shipData.getMmsi()));
        }
        var vlName = new VerticalLayout();
        vlName.setPadding(false);
        vlName.setSpacing(false);
        var shipName = "<div><strong>Ship type:</strong>" + shipData.getVesselName() + "</div>";
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
            this.setVisible(false);
        });
        hl.add(closeButton);

        var shipImage = new Image("img/720x480_Vessel_on_sea.jpg", "ship");
        shipImage.setHeight(225, Unit.PIXELS);
        shipImage.setWidth(320, Unit.PIXELS);

        this.add(hl, shipImage);

        var navigationStatus = "<div><strong>Navigation status:</strong>" + shipData.getNavigationStatus() + "</div>";
        var status = new Html(navigationStatus);
        status.setId("overlay_text");
        this.add(status);

        var speedOverGround = "<div><strong>Speed over ground:</strong>" + shipData.getSpeedOverGround() + "</div>";
        var speed = new Html(speedOverGround);
        speed.setId("overlay_text");
        this.add(speed);

        var courseOverGround = "<div><strong>Course over ground:</strong>" + shipData.getCourseOverGround() + "</div>";
        var course = new Html(courseOverGround);
        course.setId("overlay_text");
        this.add(course);

        var heading = "<div><strong>Heading:</strong>" + shipData.getHeading() + "</div>";
        var shipHeading = new Html(heading);
        shipHeading.setId("overlay_text");
        this.add(shipHeading);
    }
}
