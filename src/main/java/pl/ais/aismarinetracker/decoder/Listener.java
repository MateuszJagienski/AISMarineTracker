package pl.ais.aismarinetracker.decoder;


import pl.ais.aismarinetracker.decoder.exceptions.UnsupportedMessageType;
import pl.ais.aismarinetracker.decoder.reports.AisMessage;
import pl.ais.aismarinetracker.decoder.reports.AisMessageFactory;
import pl.ais.aismarinetracker.decoder.reports.BaseStationReport;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class Listener {

    private Map<Integer, List<AisMessage>> associatedReports = new ConcurrentHashMap<>();
    private final List<ReportsEvent> reportsEvents = new CopyOnWriteArrayList<>();
    private LocalDateTime time = LocalDateTime.now();
    private final AisHandler aisHandler;
    private final MessageListenerManager messageListenerManager;
    private final Stack<String> tempMessage = new Stack<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final long delayInSeconds = 1; // Time interval in seconds

    private static final Logger logger = Logger.getLogger(Listener.class.getName());

    public Listener(AisHandler aisHandler, MessageListenerManager messageListenerManager) {
        this.aisHandler = aisHandler;
        this.messageListenerManager = messageListenerManager;
        startListening(messageListener);
        scheduleProcessing();
    }

    MessageListener messageListener = message1 -> {
        if (!AisValidator.validateMessageFieldCount(message1)) {
            logger.info("Invalid message format received!");
            return;
        }
        try {
            handleMessageEvent(message1);
        } catch (Exception e) {
            logger.info("Creating AisMessage failed: " + e.getClass().getName());
        }
    };

    private void handleMessageEvent(String rawMessage) throws UnsupportedMessageType {
        processMessage(rawMessage);
    }

    private void scheduleProcessing() {
        scheduler.scheduleWithFixedDelay(() -> {
            try {
                fireEvent(new ReportsContainer(deepCopy(associatedReports), LocalDateTime.of(time.toLocalDate(), time.toLocalTime())));
            } catch (RuntimeException e) {
                logger.info("Time: " + time);
                logger.info("Exception in: " + getClass() + " " + e.getClass().getName());
                logger.info("Message: " + e.getMessage());
                logger.info("Cause: " + e.getCause());
            } catch (UnsupportedMessageType e) {
            }
        }, 0, delayInSeconds, TimeUnit.SECONDS);
    }

    private void processMessage(String message) throws UnsupportedMessageType {
        AisMessage aisMessage;
        var messageFields = message.split(",");
        if (!messageFields[1].equals("2")) {
            aisMessage = aisHandler.handleAisMessage(message);
            associatedReports = mapReports(aisMessage);
            setTime(aisMessage);
            return;
        }
        if (messageFields[2].equals("1")) {
            tempMessage.clear();
            tempMessage.push(message);
            return;
        }
        if (messageFields[2].equals("2") && !tempMessage.isEmpty()) {
            aisMessage = aisHandler.handleAisMessage(tempMessage.pop(), message);
            associatedReports = mapReports(aisMessage);
            setTime(aisMessage);
        }
    }

    private void setTime(AisMessage aisMessage) {
        if (aisMessage instanceof BaseStationReport report) {
            this.time = LocalDateTime.of(report.getYear(), report.getMonth(), report.getDay(), report.getHour(), report.getMinute(), report.getSecond());
        }
    }

    private static Map<Integer, List<AisMessage>> deepCopy(Map<Integer, List<AisMessage>> original) throws UnsupportedMessageType {
        Map<Integer, List<AisMessage>> copy = new HashMap<>();
        for (Map.Entry<Integer, List<AisMessage>> entry : original.entrySet()) {
            var newList = new ArrayList<AisMessage>();
            for (var e : entry.getValue()) {
                var ais = AisMessageFactory.createAisMessage(e.getBinaryMessagePayload());
                ais.setMessageRaw(e.getMessageRaw());
                newList.add(ais);
            }
            copy.put(entry.getKey(), newList);
        }
        return copy;
    }

    private void startListening(MessageListener messageListener) {
        messageListenerManager.addMessageListener(messageListener);
    }

    private void stopListening(MessageListener messageListener) {
        messageListenerManager.removeMessageListener(messageListener);
    }

    public void addReportEventListener(ReportsEvent reportsEvent) {
         synchronized (reportsEvents) {
             reportsEvents.add(reportsEvent);
         }
    }

    public void removeReportEventListener(ReportsEvent reportsEvent) {
        synchronized (reportsEvents) {
            reportsEvents.remove(reportsEvent);
        }
    }

    private void reset() {
        associatedReports.clear();
    }

    public void changeSource(String address, int port, MessageListenerManager.Provider source) {
        stopListening(messageListener);
        reset();
        messageListenerManager.changeSource(address, port, source);
        startListening(messageListener);
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

    private void fireEvent(ReportsContainer reportsContainer) {
        reportsEvents.forEach(reportsEvent -> reportsEvent.onReportsReceived(reportsContainer));
    }

}