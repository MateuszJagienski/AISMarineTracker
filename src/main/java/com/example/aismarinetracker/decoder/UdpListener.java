package com.example.aismarinetracker.decoder;


import com.example.aismarinetracker.decoder.reports.AisMessage;
import com.example.aismarinetracker.decoder.reports.BaseStationReport;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UdpListener {

    private Map<Integer, List<AisMessage>> associatedReports = new HashMap<>();
    private final List<ReportsEvent> reportsEvents = new ArrayList<>();
    private LocalDateTime time;
    private final AisHandler aisHandler;
    private final AisSocket aisSocket;
    private MessageListener messageListener;

    public UdpListener(AisHandler aisHandler) {
        this.aisHandler = aisHandler;
        this.aisSocket = new AisSocket();
        messageListener = messages1 -> {
            try {
                if (messages1[0].split(",")[1].equals("2")) return; // implements waiting mechanizm
                System.out.println("Received: " + Arrays.toString(messages1));
                var aisMessage = aisHandler.handleAisMessage(messages1);
                associatedReports = mapReports(aisMessage);
                if (aisMessage instanceof BaseStationReport report) {
                    time = LocalDateTime.of(report.getYear(), report.getMonth(), report.getDay(), report.getHour(), report.getMinute(), report.getSecond());
                }
                System.out.println("fire event!");
                fireEvent(new ReportsContainer(associatedReports, time));
            } catch (Exception e) {

            }
        };
    }

    public void startListening() {
        aisSocket.addMessageListener(messageListener);
    }

    public void stopListening() {
        aisSocket.removeMessageListener(messageListener);
    }

    public void addMessageListener(ReportsEvent reportsEvent) {
        reportsEvents.add(reportsEvent);
    }

    public void removeMessageListener(ReportsEvent reportsEvent) {
        reportsEvents.remove(reportsEvent);
    }

    private Map<Integer, List<AisMessage>> mapReports(AisMessage aisMessage) {
        associatedReports.computeIfPresent(aisMessage.getMMSI(), (k, v) -> {
            v = v.stream()
                    .filter(x -> !x.getMessageType().equals(aisMessage.getMessageType()))
                    .collect(Collectors.toList());
            v.add(aisMessage);
            return v;
        });
        associatedReports.computeIfAbsent(aisMessage.getMMSI(), k -> List.of(aisMessage));
        return associatedReports;
    }

    public void fireEvent(ReportsContainer reportsContainer) {
        reportsEvents.forEach(reportsEvent -> reportsEvent.onReportsReceived(reportsContainer));
    }

}