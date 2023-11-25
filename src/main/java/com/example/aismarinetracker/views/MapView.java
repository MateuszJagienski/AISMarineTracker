package com.example.aismarinetracker.views;

import com.example.aismarinetracker.decoder.*;
import com.example.aismarinetracker.decoder.enums.ShipType;
import com.example.aismarinetracker.decoder.exceptions.InvalidCoordinatesException;
import com.example.aismarinetracker.decoder.reports.*;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import software.xdev.vaadin.maps.leaflet.flow.LMap;
import software.xdev.vaadin.maps.leaflet.flow.data.LComponent;
import software.xdev.vaadin.maps.leaflet.flow.data.LTileLayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@Route("map")
@CssImport("./themes/th/styles.css")
public class MapView extends VerticalLayout {

    private LMap map;
    private final List<LComponent> componentsOnMap = new ArrayList<>();
    private static Map<Integer, List<AisMessage>> currentReports;
    private static Map<Integer, ShipData> currentShipData = new HashMap<>();
    private Span reportTime = new Span("Time");
    private Checkbox toggleFilters = new Checkbox();
    private Checkbox toggleNameFilter = new Checkbox();
    private ComboBox<ShipType.SimplifiedShipType> shipTypeFilter = new ComboBox<>("Ship type");
    private TextField shipName = new TextField("Ship name or MMSI");
    private PopupShip popupShip = new PopupShip();
    private final UdpListener udpListener;
    private UI ui;
    private static final Logger logger = Logger.getLogger(MapView.class.getName());


    @Autowired
    public MapView(UdpListener udpListener) {
        this.udpListener = udpListener;
        this.map = new LMap(49.675126, 12.160733, 10);
        this.map.setTileLayer(LTileLayer.DEFAULT_OPENSTREETMAP_TILE);
        this.map.setSizeFull();
        this.map.addMarkerClickListener(ev -> {
            var marker = getMarkerByTag(ev.getTag());
            marker.ifPresent(e -> {
                popupShip.showOverlay(currentShipData.get(marker.get().getMMSI()));
            });
        });

        popupShip.addTrackButtonClickListener(e -> {
            logger.info("Track btn clicked!");
        });
        add(this.map);
        this.setSizeFull();
        var hl = new HorizontalLayout();
        hl.setAlignItems(Alignment.BASELINE);
        var toggleButton = new Button();
        var play = new Icon(VaadinIcon.PLAY);
        var stop = new Icon(VaadinIcon.STOP);
        toggleButton.setIcon(play);
        AtomicBoolean isSimulationRunning = new AtomicBoolean(false);
        shipTypeFilter.setItems(ShipType.SimplifiedShipType.values());
        hl.add(reportTime, toggleButton, shipTypeFilter, toggleFilters, shipName, toggleNameFilter);

        toggleButton.addClickListener(e -> {
            if (isSimulationRunning.get()) {
                stopSimulation();
                toggleButton.setIcon(play);
            } else {
                startSimulation();
                toggleButton.setIcon(stop);
            }
            isSimulationRunning.set(!isSimulationRunning.get()); // Toggle the simulation state
        });

        add(hl);
        add(popupShip);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        ui = attachEvent.getUI();
        System.out.println("Attach");
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        stopSimulation();
    }

    private void updateMapSimulation(ReportsContainer reportsContainer) {
        currentReports = reportsContainer.getReports();
        var time = reportsContainer.getTime();
        if (!componentsOnMap.isEmpty()) {
            this.map.removeLComponents(componentsOnMap);
        }
        consumeMessage();
        reportTime.setText(String.valueOf(time));
    }

    private synchronized void consumeMessage() {
        for (var entry : currentReports.entrySet()) {
            try {
                var shipData = new ShipData(entry.getValue());
                currentShipData.put(shipData.getMmsi(), shipData);
                if (checkFilters(shipData)) addMarker(shipData);
            } catch (InvalidCoordinatesException e) {
                logger.info("Invalid coordinates");
            }
        }
    }

    private boolean checkFilters(ShipData shipData) {
        if (toggleFilters.getValue()) {
            var simplifiedShipType = ShipType.from(shipData.getShipType());
            if (!simplifiedShipType.equals(shipTypeFilter.getValue()))
                return false;
        }
        if (toggleNameFilter.getValue() && shipName.getValue() != null) {
            var pattern = shipName.getValue();
            var name = shipData.getVesselName();
            if (name == null) name = String.valueOf(shipData.getMmsi());
            if (!pattern.matches(name))
                return false;
        }
        return true;
    }

    ReportsEvent reportsEvent = reportsContainer -> {
        currentReports = reportsContainer.getReports();
        var time = reportsContainer.getTime();
        logger.info("UPDATE UI");
        ui.accessSynchronously(() -> {
            if (!componentsOnMap.isEmpty()) {
                this.map.removeLComponents(componentsOnMap);
            }
            consumeMessage();
            reportTime.setText(String.valueOf(time));
        });
        //System.gc();
    };

    private void startSimulation() {
        udpListener.addMessageListener(reportsEvent);
    }

    private void stopSimulation() {
        udpListener.removeMessageListener(reportsEvent);
    }

    public static Map<Integer, List<AisMessage>> getCurrentReports() {
        return currentReports;
    }

    private void addMarker(ShipData shipData) {
        var mmsi = shipData.getMmsi();
        var marker = new RotatedLMarker(shipData);
        marker.setMMSI(mmsi);
        componentsOnMap.add(marker);
        this.map.addLComponents(marker);
    }
    private Optional<RotatedLMarker> getMarkerByTag(String tag) {
        return componentsOnMap.stream()
                .map(c -> (RotatedLMarker) c)
                .filter(c -> c.getTag().equals(tag))
                .findFirst();
    }
}