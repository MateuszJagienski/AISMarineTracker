package pl.ais.aismarinetracker.views;


import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.shared.Registration;
import pl.ais.aismarinetracker.decoder.enums.ShipType;

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

    private void closeOverlay() {
        this.removeAll();
    }

    public Registration addCloseButtonClickListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
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
        if (shipData.getShipType() == null) {
            shipData.setShipType(ShipType.NotAvailable);
        }
        var anchorName = new Anchor("/ship/" + shipData.getMmsi());
        anchorName.setTarget("_blank");
        anchorName.setText("Ship name: " + shipData.getVesselName());

        var shipType = "<div><strong>Ship type:</strong>" + shipData.getShipType() + "</div>";
        var type = new Html(shipType);
        type.setId("ship_type");
        vlName.add(anchorName, type);
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
            fireEvent(new CloseEvent(this, true));
        });
        hl.add(closeButton);

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

        var draught = "<div><strong>Draught: </strong>" + shipData.getDraught() + "</div>";
        var shipDraught = new Html(draught);
        shipDraught.setId("overlay_text");

        var destination = "<div><strong>Destination: </strong>" + shipData.getDestination() + "</div>";
        var shipDestination = new Html(destination);
        shipDestination.setId("overlay_text");

        vl.add(hl, status, speed, course, shipHeading, shipDraught, shipDestination, shipLatitude, shipLongitude);
        return vl;
    }

}