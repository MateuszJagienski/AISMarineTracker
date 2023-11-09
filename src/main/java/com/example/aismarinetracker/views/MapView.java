package com.example.aismarinetracker.views;

import com.example.aismarinetracker.decoder.AisFromFile;
import com.example.aismarinetracker.decoder.ReportsContainer;
import com.example.aismarinetracker.decoder.enums.ShipType;
import com.example.aismarinetracker.decoder.reports.*;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import software.xdev.vaadin.maps.leaflet.flow.LMap;
import software.xdev.vaadin.maps.leaflet.flow.data.LComponent;
import software.xdev.vaadin.maps.leaflet.flow.data.LPoint;
import software.xdev.vaadin.maps.leaflet.flow.data.LPolyline;
import software.xdev.vaadin.maps.leaflet.flow.data.LTileLayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Route("map")
@PreserveOnRefresh
@CssImport("./themes/th/styles.css")
public class MapView extends VerticalLayout {

    private LMap map;
    private final List<LComponent> componentsOnMap = new ArrayList<>();
    private List<ReportsContainer> reportsContainers;
    private static Map<Integer, List<AisMessage>> currentReports;
    private static Map<Integer, ShipData> currentShipData = new HashMap<>();
    private TextField data;
    private ComboBox<File> aisDataPicker;
    private PopupShip popupShip = new PopupShip();

    public MapView() {

        this.map = new LMap(49.675126, 12.160733, 10);
        this.map.setTileLayer(LTileLayer.DEFAULT_OPENSTREETMAP_TILE);
        this.map.setSizeFull();
        this.map.addMarkerClickListener(ev -> {
            var marker = getMarkerByTag(ev.getTag());
            marker.ifPresent(e -> {
                popupShip.showOverlay(currentShipData.get(marker.get().getMMSI()));
            });
        });

        data = new TextField("Data");
        data.setReadOnly(true);
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

        hl.add(aisDataPicker, numberField, data);

        aisDataPicker.addValueChangeListener(e -> {
            try {
                updateSource();
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        numberField.addValueChangeListener(e -> {
            try {
                updateMap(numberField.getOptionalValue().orElse(1.0).intValue());
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        add(hl);
        add(popupShip);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        System.out.println("onAttach");
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
            if (entry.getValue().size() == 0) {
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
        data.setValue(String.valueOf(reportsContainers.get(reportNumber).getTime()));
    }

    private void updateSource() throws FileNotFoundException, InterruptedException {
        System.out.println(aisDataPicker.getValue().toString());
        AisFromFile.updateAisFilePath(aisDataPicker.getValue().toString());
        AisFromFile aisFromFile = new AisFromFile();
        reportsContainers = aisFromFile.readFromFile();
        updateMap(1);
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