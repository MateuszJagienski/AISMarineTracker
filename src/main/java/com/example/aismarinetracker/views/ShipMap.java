//package com.example.aismarinetracker.views;
//
//import com.example.aismarinetracker.decoder.AisFromFile;
//import com.example.aismarinetracker.decoder.ReportsContainer;
//import com.example.aismarinetracker.decoder.reports.AisMessage;
//import org.springframework.stereotype.Component;
//import software.xdev.vaadin.maps.leaflet.flow.LMap;
//import software.xdev.vaadin.maps.leaflet.flow.data.LComponent;
//import software.xdev.vaadin.maps.leaflet.flow.data.LTileLayer;
//
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class ShipMap {
//    private final LMap map;
//
//    private final List<LComponent> componentsOnMap = new ArrayList<>();
//    private List<ReportsContainer> reportsContainers;
//    private static Map<Integer, List<AisMessage>> currentReports;
//    private static Map<Integer, ShipData> currentShipData = new HashMap<>();
//    private LComponent componentsOnMap;
//    private PopupShip popupShip = new PopupShip();
//
//
//    public ShipMap(double lat, double lon, int zoom) {
//        this.map = new LMap(lat, lon, zoom);
//        this.map.setTileLayer(LTileLayer.DEFAULT_OPENSTREETMAP_TILE);
//        this.map.setSizeFull();
//        this.map.addMarkerClickListener(ev -> {
//            var marker = getMarkerByTag(ev.getTag());
//            marker.ifPresent(e -> {
//                popupShip.showOverlay(currentShipData.get(marker.get().getMMSI()));
//            });
//        });
//    }
//
//   public void registerEvent() {
//
//   }
//
//
//    private void updateMap(int reportNumber) throws FileNotFoundException, InterruptedException {
//        if (reportsContainers == null) {
//            updateSource();
//        }
//        if (!componentsOnMap.isEmpty()) {
//            this.map.removeLComponents(componentsOnMap);
//        }
//        currentReports = reportsContainers.get(reportNumber).getReports();
//        for (var entry : currentReports.entrySet()) {
//            if (entry.getValue().size() == 0) {
//                continue;
//            }
//            try {
//                var shipData = new ShipData(entry.getValue());
//                currentShipData.put(shipData.getMmsi(), shipData);
//                addMarker(shipData);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        data.setValue(String.valueOf(reportsContainers.get(reportNumber).getTime()));
//    }
//
//    private void updateSource() throws FileNotFoundException, InterruptedException {
//        System.out.println(aisDataPicker.getValue().toString());
//        AisFromFile.updateAisFilePath(aisDataPicker.getValue().toString());
//        AisFromFile aisFromFile = new AisFromFile();
//        reportsContainers = aisFromFile.readFromFile();
//        updateMap(1);
//    }
//}
