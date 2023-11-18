package com.example.aismarinetracker.views;

import com.example.aismarinetracker.decoder.*;
import com.example.aismarinetracker.decoder.reports.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import software.xdev.vaadin.maps.leaflet.flow.LMap;
import software.xdev.vaadin.maps.leaflet.flow.data.LComponent;
import software.xdev.vaadin.maps.leaflet.flow.data.LTileLayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Route("map")
@PreserveOnRefresh
@CssImport("./themes/th/styles.css")
public class MapView extends VerticalLayout {

    private LMap map;
    private final List<LComponent> componentsOnMap = new ArrayList<>();
    private List<ReportsContainer> reportsContainers;
    private static Map<Integer, List<AisMessage>> currentReports;
    private static Map<Integer, ShipData> currentShipData = new HashMap<>();
    private TextField reportTime;
    private ComboBox<File> aisDataPicker;
    private PopupShip popupShip = new PopupShip();
    private final UdpListener udpListener;
    private final AisFromFile aisFromFile;

    @Autowired
    public MapView(AisFromFile aisFromFile, UdpListener udpListener) {
        this.aisFromFile = aisFromFile;
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
            System.out.println("Track btn clicked!");
        });
        reportTime = new TextField("Data");
        reportTime.setReadOnly(true);
        add(this.map);
        this.setSizeFull();
        var hl = new HorizontalLayout();
        aisDataPicker = new ComboBox<>("Sources");
        File folder = new File("src/main/resources/META-INF/resources/aisdata");
        File[] listOfFiles = folder.listFiles();

        aisDataPicker.setItems(listOfFiles);
        aisDataPicker.getStyle().set("--vaadin-combo-box-overlay-width", "350px");
        aisDataPicker.setValue(listOfFiles != null ? listOfFiles[0] : new File(""));
        var numberField = new NumberField("Report number");
        numberField.setMin(1);
        numberField.setStepButtonsVisible(true);
        numberField.setStep(10);
        var toggleButton = new Button();
        var play = new Icon(VaadinIcon.PLAY);
        var stop = new Icon(VaadinIcon.STOP);
        toggleButton.setIcon(play);
        AtomicBoolean isSimulationRunning = new AtomicBoolean(false);
        hl.add(aisDataPicker, numberField, reportTime, toggleButton);

//        aisDataPicker.addValueChangeListener(e -> {
//            try {
//                updateSource();
//            } catch (FileNotFoundException fileNotFoundException) {
//                fileNotFoundException.printStackTrace();
//            } catch (InterruptedException ex) {
//                throw new RuntimeException(ex);
//            }
//        });
//
//        numberField.addValueChangeListener(e -> {
//            try {
//                updateMap(numberField.getOptionalValue().orElse(1.0).intValue());
//            } catch (FileNotFoundException fileNotFoundException) {
//                fileNotFoundException.printStackTrace();
//            } catch (InterruptedException ex) {
//                throw new RuntimeException(ex);
//            }
//        });
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

    private void updateMap(int reportNumber) throws FileNotFoundException, InterruptedException {
        if (reportsContainers == null) {
            updateSource();
        }
        if (!componentsOnMap.isEmpty()) {
            this.map.removeLComponents(componentsOnMap);
        }
        currentReports = reportsContainers.get(reportNumber).getReports();
        for (var entry : currentReports.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            try {
                var shipData = new ShipData(entry.getValue());
                currentShipData.put(shipData.getMmsi(), shipData);
                addMarker(shipData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        reportTime.setValue(String.valueOf(reportsContainers.get(reportNumber).getTime()));
    }

    private void updateSource() throws FileNotFoundException, InterruptedException {
        AisFromFile.updateAisFilePath(aisDataPicker.getValue().toString());
        reportsContainers = aisFromFile.readFromFile();
        updateMap(1);
    }

    private void updateMapSimulation(ReportsContainer reportsContainer) {
        currentReports = reportsContainer.getReports();
        var time = reportsContainer.getTime();
        if (!componentsOnMap.isEmpty()) {
            this.map.removeLComponents(componentsOnMap);
        }
        for (var entry : currentReports.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            try {
                var shipData = new ShipData(entry.getValue());
                currentShipData.put(shipData.getMmsi(), shipData);
                addMarker(shipData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        reportTime.setValue(String.valueOf(time));
    }

    ReportsEvent reportsEvent = reportsContainer -> {
        getUI().ifPresent(ui -> ui.access(() -> {
            updateMapSimulation(reportsContainer);
        }));
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