package com.example.aismarinetracker.decoder;


import com.example.aismarinetracker.decoder.exceptions.UnsupportedMessageType;
import com.example.aismarinetracker.decoder.reports.AisMessage;
import com.example.aismarinetracker.decoder.reports.BaseStationReport;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UdpListener {

    private Map<Integer, List<AisMessage>> associatedReports = new HashMap<>();
    private final List<ReportsEvent> reportsEvents = new ArrayList<>();
    private LocalDateTime time;
    private final AisHandler aisHandler;
    private final MessageListenerManager messageListenerManager;
    private MessageListener messageListener;
    private List<String> messageHistory = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(UdpListener.class.getName());

    public UdpListener(AisHandler aisHandler, MessageListenerManager messageListenerManager) {
        this.aisHandler = aisHandler;
        this.messageListenerManager = messageListenerManager;
        messageListener = messages1 -> {
            try {
                handleMessageEvent(messages1);
            } catch (UnsupportedMessageType e) {
                logger.info("Creating AisMessage failed " + e.getClass().getName());
            }
        };
    }

    private void handleMessageEvent(String rawMessage) {
        AisMessage aisMessage;
        var messageFields = rawMessage.split(",");
        if (messageFields[1].equals("2")) {
            if (messageFields[2].equals("1")) {
                messageHistory.add(rawMessage);
                return;
            } else {
                aisMessage = aisHandler.handleAisMessage(messageHistory.get(messageHistory.size() - 1), rawMessage);
            }
        } else {
            aisMessage = aisHandler.handleAisMessage(rawMessage);
        }
        associatedReports = mapReports(aisMessage);
        if (aisMessage instanceof BaseStationReport report) {
            time = LocalDateTime.of(report.getYear(), report.getMonth(), report.getDay(), report.getHour(), report.getMinute(), report.getSecond());
        }
        fireEvent(new ReportsContainer(associatedReports, time));
    }

    public void startListening() {
        messageListenerManager.addMessageListener(messageListener);
    }

    public void stopListening() {
        messageListenerManager.removeMessageListener(messageListener);
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