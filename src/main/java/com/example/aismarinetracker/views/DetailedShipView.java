package com.example.aismarinetracker.views;

import com.example.aismarinetracker.decoder.reports.AisMessage;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.Map;

@Route("ship/:mmsi")
public class DetailedShipView extends VerticalLayout implements BeforeEnterObserver {

    private Map<Integer, List<AisMessage>> currentReports;
    private Integer currentMMSI;
    private final Scroller scroller = new Scroller();

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        System.out.println("beforeEnter");
        currentMMSI = Integer.parseInt(beforeEnterEvent.getRouteParameters().get("mmsi").orElse("0"));
        this.currentReports = MapView.getCurrentReports();
        if (currentReports == null || currentReports.get(currentMMSI) == null) {
            beforeEnterEvent.rerouteTo(MapView.class);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        setWidth("100%");
        setHeight("100%");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().setBackground("blue");
        System.out.println("curmmsi: " + currentMMSI);
        System.out.println("currentReports: " + currentReports);
        System.out.println("currentReports.get(currentMMSI): " + currentReports.get(currentMMSI));
        var vl = new VerticalLayout();
        currentReports.get(currentMMSI).forEach(e -> {
            var grid = ReportGrid.createReportGrid(e);
            vl.add(grid);
        });

        scroller.setContent(vl);
        scroller.setWidth("100%");
        scroller.setHeight("100%");
        add(scroller);
    }
}
