package pl.ais.aismarinetracker.decoder;


import pl.ais.aismarinetracker.decoder.enums.MessageType;
import pl.ais.aismarinetracker.decoder.exceptions.UnsupportedMessageType;
import pl.ais.aismarinetracker.decoder.reports.AisMessage;
import pl.ais.aismarinetracker.decoder.reports.AisMessageFactory;
import pl.ais.aismarinetracker.decoder.reports.BaseStationReport;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Service for reading ais messages from file
 */
@Service
public class AisFromFile {

    private static String AIS_FILE_PATH = "src/main/resources/META-INF/resources/aisdata/00.txt";
    private final String AIS_DIR_PATH = "src/main/resources/META-INF/resources/aisdata/";
    private final AisHandler aisHandler;
    private Map<Integer, List<AisMessage>> associatedReports = new HashMap<>();
    private static final Logger logger = Logger.getLogger(AisFromFile.class.getName());

    public AisFromFile(AisHandler aisHandler) {
        this.aisHandler = aisHandler;
    }

    public static void updateAisFilePath(String source) {
        AIS_FILE_PATH = source;
    }

    /**
     * @return list of reports containers containing map<MMSI, List<AisMessage>> and time of report
     * @throws FileNotFoundException
     **/
    public List<ReportsContainer> readFromFile() throws FileNotFoundException {
        var file = new File(AIS_FILE_PATH);
        var scanner = new Scanner(file);
        var reports = new ArrayList<ReportsContainer>();
        if (!scanner.nextLine().trim().equals("OK")) {
            scanner = new Scanner(cleanAisFile(file, scanner));
        }
        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();
            // if line has 2 in second field concatante with next line
            try {
                AisMessage aisMessage = getMessage(line, scanner);
                associatedReports = mapReports(aisMessage);
                if (aisMessage instanceof BaseStationReport x) {
                    reports.add(new ReportsContainer(deepCopy(associatedReports), LocalDateTime.of(x.getYear(), x.getMonth(), x.getDay(), x.getHour(), x.getMinute(), x.getSecond())));
                }
            } catch (Exception e ) {
                logger.info("Creating AisMessage failed " + e.getClass().getName());
            }
        }
        return reports;
    }

    private AisMessage getMessage(String line, Scanner scanner) throws UnsupportedMessageType {
        AisMessage aisMessage;
        if (line.split(",")[1].equals("2")) {
            var nextLine = scanner.nextLine();
            aisMessage = aisHandler.handleAisMessage(line, nextLine);
        } else {
            aisMessage = aisHandler.handleAisMessage(line);
        }
        return aisMessage;
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

    /**
     * Reads ais messages from file and returns list of ais messages
     * @return list of ais messages
     * @throws FileNotFoundException
     **/
    public List<AisMessage> read() throws FileNotFoundException {
        var file = new File(AIS_FILE_PATH);
        var scanner = new Scanner(file);
        var messages = new ArrayList<AisMessage>();

        if (!scanner.nextLine().trim().equals("OK")) {
            scanner = new Scanner(cleanAisFile(file, scanner));
        }

        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();
            // if line has 2 in second field concatante with next line
            try {
                AisMessage aisMessage;
                if (line.split(",")[1].equals("2")) {
                    var nextLine = scanner.nextLine();
                    aisMessage = aisHandler.handleAisMessage(line, nextLine);
                } else {
                    aisMessage = aisHandler.handleAisMessage(line);
                }
                messages.add(aisMessage);

            } catch (Exception e ) {
                logger.info("Creating AisMessage failed  " + e.getClass().getName());
            }
        }
        return messages;
    }

    /**
     * Reads AIS messages from file and returns list of ais messages by message type
     * @param messageType
     * @return only ais messages of given message type
     * @throws FileNotFoundException
     */
    public List<AisMessage> read(MessageType messageType) throws FileNotFoundException {
        var file = new File(AIS_FILE_PATH);
        var scanner = new Scanner(file);
        var messages = new ArrayList<AisMessage>();

        if (!scanner.nextLine().trim().equals("OK")) {
            scanner = new Scanner(cleanAisFile(file, scanner));
        }

        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();
            // if line has 2 in second field concatante with next line
            try {
                AisMessage aisMessage;
                if (line.split(",")[1].equals("2")) {
                    var nextLine = scanner.nextLine();
                    aisMessage = aisHandler.handleAisMessage(line, nextLine);
                } else {
                    aisMessage = aisHandler.handleAisMessage(line);
                }
                if (aisMessage != null) {
                    if (aisMessage.getMessageType().equals(messageType)) {
                        messages.add(aisMessage);
                    }
                }
            } catch (Exception e ) {
                logger.info("Creating message failed! " + e.getClass().getName());
            }
        }
        return messages;
    }

    private static Map<Integer, List<AisMessage>> deepCopy(Map<Integer, List<AisMessage>> original) throws UnsupportedMessageType {
        Map<Integer, List<AisMessage>> copy = new HashMap<>();
        for (Map.Entry<Integer, List<AisMessage>> entry : original.entrySet()) {
            var newList = new ArrayList<AisMessage>();
            AisMessageFactory aisMessageFactory = new AisMessageFactory();
            for (var e : entry.getValue()) {
                newList.add(aisMessageFactory.createAisMessage(e.getBinaryMessagePayload()));
            }
            copy.put(entry.getKey(), newList);
        }
        return copy;
    }

    private File cleanAisFile(File file, Scanner scanner) {
        var validMessages = findValidMessages(scanner);
        var newFile = new File(AIS_DIR_PATH + file.getName());
        if (!validMessages.isEmpty()) {
            try {
                scanner.close();
                file.delete();
                writeToFile(newFile, validMessages);
            } catch (Exception e) {
                logger.info("Cleaning file failed! " + e.getClass().getName());
            }
        }
        return newFile;
    }

    private StringBuilder findValidMessages(Scanner scanner) {
        StringBuilder validMessage = new StringBuilder();
        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();

            if (AisValidator.isMessageFormatValid(line.trim())) {
                validMessage.append(line).append("\n");
            }
        }
        return validMessage;
    }

    private void writeToFile(File newFile, StringBuilder validMessage) throws Exception {
        var writer = new FileWriter(newFile);
        writer.write(validMessage.toString());
        writer.close();
    }

}
