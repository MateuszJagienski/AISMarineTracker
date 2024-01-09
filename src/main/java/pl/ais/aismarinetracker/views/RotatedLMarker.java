package pl.ais.aismarinetracker.views;

import com.vaadin.flow.component.dependency.CssImport;
import pl.ais.aismarinetracker.decoder.enums.ShipType;
import software.xdev.vaadin.maps.leaflet.flow.data.LDivIcon;
import software.xdev.vaadin.maps.leaflet.flow.data.LMarker;

import java.util.UUID;

@CssImport("./rotated_marker.css")
public class RotatedLMarker extends LMarker {

    private LDivIcon lDivIcon;
    private Integer MMSI;

    public RotatedLMarker(ShipData shipData) {
        super(shipData.getLatitude(), shipData.getLongitude(), String.valueOf(UUID.randomUUID()));
        super.setDivIcon(createRotatedIcon(shipData));
    }

    public RotatedLMarker(float lat, float lon, int angle) {
        super(lat, lon);
        super.setDivIcon(createBorderMarker(angle));
    }

     private LDivIcon createBorderMarker(int angle) {
        String cssClass = "border";
        String html = """
                <div class=%s style="transform: rotate(%ddeg);">
                </div>
                """.formatted(cssClass, angle);
        lDivIcon = new LDivIcon(html);
        lDivIcon.setClassName("");
        return lDivIcon;
    }

    private LDivIcon createRotatedIcon(ShipData shipData) {
        var simplifiedShipType = ShipType.from(shipData.getShipType());
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
        if (shipData.getSpeedOverGround() < 0.2f) ship.append("1");
        else ship.append("0");
        int angle = (int) shipData.getCourseOverGround();
        String cssClass = "vessel-icon-%s".formatted(ship);
        String html = """
                <div class=%s style="transform: rotate(%ddeg);">
                </div>
                """.formatted(cssClass, angle);
        lDivIcon = new LDivIcon(html);
        lDivIcon.setClassName(""); // workaround
        return lDivIcon;
    }



    public Integer getMMSI() {
        return MMSI;
    }

    public void setMMSI(Integer MMSI) {
        this.MMSI = MMSI;
    }
}
