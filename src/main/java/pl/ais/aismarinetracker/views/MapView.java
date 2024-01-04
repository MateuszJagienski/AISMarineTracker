package pl.ais.aismarinetracker.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.ais.aismarinetracker.decoder.MessageListenerManager;
import pl.ais.aismarinetracker.decoder.ReportsContainer;
import pl.ais.aismarinetracker.decoder.ReportsEvent;
import pl.ais.aismarinetracker.decoder.Listener;
import pl.ais.aismarinetracker.decoder.enums.ShipType;
import pl.ais.aismarinetracker.decoder.exceptions.InvalidCoordinatesException;
import pl.ais.aismarinetracker.decoder.reports.AisMessage;
import software.xdev.vaadin.maps.leaflet.flow.LMap;
import software.xdev.vaadin.maps.leaflet.flow.data.LComponent;
import software.xdev.vaadin.maps.leaflet.flow.data.LTileLayer;

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
    private Checkbox toggleShipTypeFilter = new Checkbox();
    private Checkbox toggleNameFilter = new Checkbox();
    private Checkbox toggleSpeedFilter = new Checkbox();
    private ComboBox<ShipType.SimplifiedShipType> shipTypeFilter = new ComboBox<>("Ship type");
    private TextField shipName = new TextField("Ship name or MMSI");
    private NumberField shipSpeed = new NumberField("Speed");
    private PopupShip popupShip = new PopupShip();
    private final Listener listener;
    private UI ui;
    private static final Logger logger = Logger.getLogger(MapView.class.getName());


    @Autowired
    public MapView(Listener listener) {
        this.listener = listener;
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
        var connect = new Button("Connect");
        var openDialog = new Button("Connect");
        var play = new Icon(VaadinIcon.PLAY);
        var stop = new Icon(VaadinIcon.STOP);
        var address = new TextField("Address");
        var port = new NumberField("Port");
        var provider = new ComboBox<MessageListenerManager.Provider>("Provider");
        var dialog = new Dialog();
        var dialogLayout = new VerticalLayout();
        dialogLayout.setAlignItems(Alignment.BASELINE);
        dialogLayout.add(address, port, provider, connect);
        dialog.add(dialogLayout);
        provider.setItems(MessageListenerManager.Provider.values());
        toggleButton.setIcon(play);
        AtomicBoolean isSimulationRunning = new AtomicBoolean(false);
        shipTypeFilter.setItems(ShipType.SimplifiedShipType.values());
        hl.add(reportTime, toggleButton, shipTypeFilter, toggleShipTypeFilter, shipName, toggleNameFilter, shipSpeed, toggleSpeedFilter, openDialog);
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

        openDialog.addClickListener(e -> {
            dialog.open();
        });

        connect.addClickListener(e -> {
            if (port.isEmpty() || address.isEmpty() || provider.isEmpty()) return;
            listener.changeSource(address.getValue(), port.getValue().intValue(), provider.getValue());
        });

        toggleNameFilter.addClickListener(e -> {
           updateMap();
        });
        toggleSpeedFilter.addClickListener(e -> {
            updateMap();
        });
        toggleShipTypeFilter.addClickListener(e -> {
            updateMap();
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
        if (toggleShipTypeFilter.getValue()) {
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
        if (toggleSpeedFilter.getValue() && !shipSpeed.isEmpty()) {
            return !(shipData.getSpeedOverGround() < shipSpeed.getValue());
        }
        return true;
    }

    ReportsEvent reportsEvent = reportsContainer -> {
        currentReports = reportsContainer.getReports();
        var time = reportsContainer.getTime();
        logger.info("UPDATE UI");
        ui.accessSynchronously(() -> {
            updateMap();
            reportTime.setText(String.valueOf(time));
        });
    };

    private void updateMap() {
        ui.accessSynchronously(() -> {
            if (!componentsOnMap.isEmpty()) {
                this.map.removeLComponents(componentsOnMap);
            }
            consumeMessage();
        });
    }


    private void startSimulation() {
        listener.addReportEventListener(reportsEvent);
    }

    private void stopSimulation() {
        listener.removeReportEventListener(reportsEvent);
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