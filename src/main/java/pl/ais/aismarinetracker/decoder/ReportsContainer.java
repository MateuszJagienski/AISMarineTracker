package pl.ais.aismarinetracker.decoder;

import pl.ais.aismarinetracker.decoder.reports.AisMessage;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
public class ReportsContainer {

    private Map<Integer, List<AisMessage>> reports;
    private LocalDateTime time;

    public ReportsContainer(Map<Integer, List<AisMessage>> reports, LocalDateTime time) {
        this.time = time;
        this.reports = reports;
    }
}
