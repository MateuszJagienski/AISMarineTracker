package com.example.aismarinetracker.views;

import com.example.aismarinetracker.decoder.enums.ShipType;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import software.xdev.vaadin.maps.leaflet.flow.data.LDivIcon;
import software.xdev.vaadin.maps.leaflet.flow.data.LMarker;

import java.util.UUID;

@CssImport("./rotated_marker.css")
public class RotatedLMarker extends LMarker {

    private LDivIcon lDivIcon;
    private Div div;
    private String uniqueTag;
    private Integer MMSI;


    public RotatedLMarker(ShipType shipType, int angle, double lat, double lon, float speed) {
        super(lat, lon, String.valueOf(UUID.randomUUID()));
        uniqueTag = super.getTag();
        super.setDivIcon(createRotatedIcon(shipType, angle, speed));
    }

    private LDivIcon createRotatedIcon(ShipType shipType, int angle, float speed) {
        System.out.println("siptype: " + shipType);
        if (shipType == null) {
            shipType = ShipType.NotAvailable;
        }
        var simplifiedShipType = ShipType.from(shipType);
        StringBuilder ship = new StringBuilder(switch (simplifiedShipType) {
            case Fishing -> "fishing-";
            case PleasureCraft -> "pleasure-";
            case HighSpeedCraft -> "hsc-";
            case Tug -> "tugspecialcraft-";
            case Passenger -> "passenger-";
            case Cargo -> "cargo-";
            case Tanker -> "tanker-";
            default -> "other-";
        });
        if (speed < 0.2f) ship.append("1");
        else ship.append("0");

        String cssClass = "vessel-icon-%s".formatted(ship);
        String html = """
                <div class=%s style="transform: rotate(%ddeg);">
                </div>
                """.formatted(cssClass, angle);
        lDivIcon = new LDivIcon(html);
        lDivIcon.setClassName("");
        return lDivIcon;
    }
//<img src="%s" width="24" height="24" style="transform: rotate(%ddeg);">
    public void setAngle(int angle) {
        String html = """
                <img src="%s" style="transform: rotate(%ddeg);">
                """.formatted(lDivIcon.getHtml(), angle);
        lDivIcon.setHtml(html);
        super.setDivIcon(lDivIcon);
    }

    public Div getDiv() {
        return div;
    }

    public RotatedLMarker getByTag(String tag) {
        if (tag.equals(uniqueTag)) {
            return this;
        }
        return null;
    }

    public Integer getMMSI() {
        return MMSI;
    }

    public void setMMSI(Integer MMSI) {
        this.MMSI = MMSI;
    }
}
