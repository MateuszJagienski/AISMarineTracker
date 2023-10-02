package com.example.aismarinetracker.views;

import com.example.aismarinetracker.decoder.AisFromFile;
import com.example.aismarinetracker.decoder.ReportsContainer;
import com.example.aismarinetracker.decoder.enums.ShipType;
import com.example.aismarinetracker.decoder.reports.*;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import software.xdev.vaadin.maps.leaflet.flow.LMap;
import software.xdev.vaadin.maps.leaflet.flow.data.LComponent;
import software.xdev.vaadin.maps.leaflet.flow.data.LTileLayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Route("map")
@PreserveOnRefresh
@CssImport("./themes/th/styles.css")
public class MapView extends VerticalLayout {

    private LMap map;
    private Optional<UI> ui;
    private final List<LComponent> componentsOnMap = new ArrayList<>();
    private List<ReportsContainer> reportsContainers;
    private static Map<Integer, List<AisMessage>> currentReports;
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
                var shipData = new ShipData(currentReports.get(marker.get().getMMSI()));
                popupShip.showOverlay(shipData);
            });
        });

        data = new TextField("Data");
        data.setReadOnly(true);
        add(this.map);
        this.setSizeFull();
        var hl = new HorizontalLayout();
        aisDataPicker = new ComboBox<File>("Pick Ais data");
        File folder = new File("src/main/resources/META-INF/resources/aisdata");
        File[] listOfFiles = folder.listFiles();

        aisDataPicker.setItems(listOfFiles);
        aisDataPicker.getStyle().set("--vaadin-combo-box-overlay-width", "350px");
        aisDataPicker.setValue(listOfFiles != null ? listOfFiles[0] : new File(""));
        var updateButton = new Button();
        updateButton.setIcon(VaadinIcon.REFRESH.create());
        var numberField = new NumberField("Report number");
        numberField.setMin(1);
        numberField.setStepButtonsVisible(true);
        numberField.setStep(10);

        hl.add(aisDataPicker, numberField, data, updateButton);

        aisDataPicker.addValueChangeListener(e -> {
            try {
                updateSource();
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        updateButton.addClickListener(e -> {
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

    private void openOverlay() {

        var hl = new HorizontalLayout();
        var outsideElement = new Div();
        outsideElement.getStyle().set("position", "fixed");
        outsideElement.getStyle().set("z-index", "1");
        outsideElement.getStyle().set("width", "100%");
        outsideElement.getStyle().set("height", "100%");
        outsideElement.getStyle().set("background-color", "rgba(0,0,0,0)");
        outsideElement.addClickListener(event -> {
            outsideElement.setVisible(false);
            hl.setVisible(false);
        });
        outsideElement.add(hl);
        var hlStyle = hl.getStyle();
        hlStyle.set("z-index", "2");
        hlStyle.setWidth("250px");
        hlStyle.setRight("0");
        hlStyle.setPosition(Style.Position.FIXED);
        hlStyle.setHeight("100%");
        // disable hl when clicked outside
        hl.getElement().addEventListener("click", event -> {
            hl.setVisible(false);
        });
        // disable when cliecked outside


        Div div = new Div();
        var style = div.getStyle();
        div.getElement().getStyle().set("position", "fixed");

        div.setWidth("25%");
        div.setHeight("100%");
        style.set("background-color", "rgba(0,0,0,0.5)");
        style.set("z-index", "2");
        style.setRight("0");
        style.setWhiteSpace(Style.WhiteSpace.PRE_LINE);
        div.setText("");
        hl.add(div);

        add(outsideElement);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        System.out.println("onAttach");
    }

    public void updateMap(int reportNumber) throws FileNotFoundException, InterruptedException {
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
            float lat = 0;
            float lon = 0;
            float angle = 0;
            float speed = 0;
            ShipType shipType = null;
            int mmsi = entry.getKey();
            for (var message : entry.getValue()) {
                if (message instanceof PositionReport m) {
                    lat = m.getLatitude();
                    lon = m.getLongitude();
                    angle = m.getCourseOverGround();
                    speed = m.getSpeedOverGround();
                }
                if (message instanceof StaticAndVoyageRelatedData m) {
                    shipType = m.getShipType();
                }
                if (message instanceof BaseStationReport report) {
                    lat = report.getLatitude();
                    lon = report.getLongitude();
                }
                if (message instanceof ExtendedClassBEquipmentPositionReport m) {
                    lat = m.getLatitude();
                    lon = m.getLongitude();
                    angle = m.getCourseOverGround();
                    speed = m.getSpeedOverGround();
                    shipType = m.getShipType();
                }
            }
            addMarker(shipType, (int) angle, lat, lon, mmsi, speed);
        }
        data.setValue(String.valueOf(reportsContainers.get(reportNumber).getTime()));
    }

    public void updateSource() throws FileNotFoundException, InterruptedException {
        System.out.println(aisDataPicker.getValue().toString());
        AisFromFile.updateAisFilePath(aisDataPicker.getValue().toString());
        AisFromFile aisFromFile = new AisFromFile();
        reportsContainers = aisFromFile.readFromFile();
        updateMap(1);
    }

    public static Map<Integer, List<AisMessage>> getCurrentReports() {
        return currentReports;
    }

    private void addMarker(ShipType shipType, int angle, double lat, double lon, int mmsi, float speed) {
        var marker = new RotatedLMarker(shipType, angle, lat, lon, speed);
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