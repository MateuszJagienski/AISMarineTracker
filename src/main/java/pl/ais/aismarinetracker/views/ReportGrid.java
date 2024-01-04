package pl.ais.aismarinetracker.views;

import com.vaadin.flow.component.grid.Grid;
import org.apache.commons.lang3.tuple.Pair;
import pl.ais.aismarinetracker.decoder.AisService;
import pl.ais.aismarinetracker.decoder.reports.AisMessage;

import java.util.ArrayList;

public class ReportGrid {

    private ReportGrid() {}

    public static Grid<Pair<String, String>> createReportGrid(AisMessage report) {
        var aisService = new AisService();
        Grid<Pair<String, String>> grid = new Grid<>();
        grid.addColumn(Pair::getLeft).setHeader("Value");
        grid.addColumn(Pair::getRight).setHeader("Description");
        var rowData = new ArrayList<Pair<String, String>>();
        aisService.getMap(report).forEach((k, v) -> rowData.add(Pair.of(k, v)));
        grid.setItems(rowData);
        grid.setAllRowsVisible(true);
        return grid;
    }
}