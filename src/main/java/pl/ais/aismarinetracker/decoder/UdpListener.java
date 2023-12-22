package pl.ais.aismarinetracker.decoder;


import pl.ais.aismarinetracker.decoder.exceptions.UnsupportedMessageType;
import pl.ais.aismarinetracker.decoder.reports.AisMessage;
import pl.ais.aismarinetracker.decoder.reports.BaseStationReport;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UdpListener {

    private Map<Integer, List<AisMessage>> associatedReports = new HashMap<>();
    private final List<ReportsEvent> reportsEvents = new CopyOnWriteArrayList<>();
    private LocalDateTime time;
    private final AisHandler aisHandler;
    private final MessageListenerManager messageListenerManager;
    private final Stack<String> tempMessage = new Stack<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private final long delayInSeconds = 1; // Time interval in seconds


    private static final Logger logger = Logger.getLogger(UdpListener.class.getName());

    public UdpListener(AisHandler aisHandler, MessageListenerManager messageListenerManager) {
        this.aisHandler = aisHandler;
        this.messageListenerManager = messageListenerManager;
        startListening(messageListener);
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
        messageQueue.offer(rawMessage);
        scheduleProcessing();
    }

    private void scheduleProcessing() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                processMessage();
            } catch (UnsupportedMessageType e) {
                logger.info("Unsupported message!");
            }
        }, 0, delayInSeconds, TimeUnit.SECONDS);
    }

    private void processMessage() throws UnsupportedMessageType {
        var messages = new ArrayList<String>();
        messageQueue.drainTo(messages);

        for (String message : messages) {
            AisMessage aisMessage;
            var messageFields = message.split(",");
            if (messageFields[1].equals("2")) {
                if (messageFields[2].equals("1")) {
                    tempMessage.push(message);
                    continue;
                } else {
                    aisMessage = aisHandler.handleAisMessage(tempMessage.pop(), message);
                }
            } else {
                aisMessage = aisHandler.handleAisMessage(message);
            }
            associatedReports = mapReports(aisMessage);
            if (aisMessage instanceof BaseStationReport report) {
                time = LocalDateTime.of(report.getYear(), report.getMonth(), report.getDay(), report.getHour(), report.getMinute(), report.getSecond());
            }
        }
        fireEvent(new ReportsContainer(associatedReports, time));
    }

    public void startListening(MessageListener messageListener) {
        messageListenerManager.addMessageListener(messageListener);
    }

    public void stopListening(MessageListener messageListener) {
        messageListenerManager.removeMessageListener(messageListener);
    }

    public void addMessageListener(ReportsEvent reportsEvent) {
         synchronized (reportsEvents) {
             reportsEvents.add(reportsEvent);
         }
    }

    public void removeMessageListener(ReportsEvent reportsEvent) {
        synchronized (reportsEvents) {
            reportsEvents.remove(reportsEvent);
        }
    }

    private synchronized Map<Integer, List<AisMessage>> mapReports(AisMessage aisMessage) {
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