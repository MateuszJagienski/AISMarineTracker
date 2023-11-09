package com.example.aismarinetracker.decoder;


import com.example.aismarinetracker.decoder.enums.MessageType;
import com.example.aismarinetracker.decoder.reports.AisMessage;
import com.example.aismarinetracker.decoder.reports.AisMessageFactory;
import com.example.aismarinetracker.decoder.reports.BaseStationReport;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AisFromFile {

    private static String AIS_FILE_PATH = "src/main/resources/META-INF/resources/aisdata/00.txt";
    private final String AIS_DIR_PATH = "src/main/resources/META-INF/resources/aisdata/";

    public static void updateAisFilePath(String source) {
        AIS_FILE_PATH = source;
    }

    public List<ReportsContainer> readFromFile() throws FileNotFoundException {
        var file = new File(AIS_FILE_PATH);
        var scanner = new Scanner(file);
        var reports = new ArrayList<ReportsContainer>();
        Map<Integer, List<AisMessage>> mmsiMessages = new HashMap<>();
        var aisHandler = new AisHandler();

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

                mmsiMessages.computeIfPresent(aisMessage.getMMSI(), (k, v) -> {
                     v = v.stream()
                            .filter(x -> !x.getMessageType().equals(aisMessage.getMessageType()))
                            .collect(Collectors.toList());
                     v.add(aisMessage);
                     return v;
                });
                mmsiMessages.computeIfAbsent(aisMessage.getMMSI(), k -> List.of(aisMessage));

                if (aisMessage instanceof BaseStationReport x) {
                    reports.add(new ReportsContainer(new HashMap<>(mmsiMessages), LocalDateTime.of(x.getYear(), x.getMonth(), x.getDay(), x.getHour(), x.getMinute(), x.getSecond())));
                    mmsiMessages = deepCopy(mmsiMessages);
                }
            } catch (RuntimeException e ) {
                //e.printStackTrace();
            }
        }
        return reports;
    }

    public List<AisMessage> read() throws FileNotFoundException {
        var file = new File(AIS_FILE_PATH);
        var scanner = new Scanner(file);
        var aisHandler = new AisHandler();
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
                    messages.add(aisMessage);
                }

            } catch (RuntimeException e ) {
                //e.printStackTrace();
            }
        }
        return messages;
    }

    public List<AisMessage> read(MessageType messageType) throws FileNotFoundException {
        var file = new File(AIS_FILE_PATH);
        var scanner = new Scanner(file);
        var aisHandler = new AisHandler();
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

            } catch (RuntimeException e ) {
                //e.printStackTrace();
            }
        }
        return messages;
    }

    private static Map<Integer, List<AisMessage>> deepCopy(Map<Integer, List<AisMessage>> original) {
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
        if (validMessages.length() > 0) {
            try {
                scanner.close();
                file.delete();
                writeToFile(newFile, validMessages);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newFile;
    }

    private StringBuilder findValidMessages(Scanner scanner) {
        StringBuilder validMessage = new StringBuilder();
        //validMessage.append("OK\n");
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

    public static void main(String[] args) throws Exception {
        // get all txt files from AIS_FILE_PATH1
        File folder = new File("src/main/resources/META-INF/resources/aisdata");
        File[] listOfFiles = folder.listFiles();
        var f = new AisFromFile();
        if (listOfFiles == null) {
            System.out.println("No files found");
            return;
        }
        // elapsed time
        long startTime = System.currentTimeMillis();
        var sb = new StringBuilder();
        for (var e : listOfFiles) {
            sb.append(f.findValidMessages(new Scanner(e)));
        }
        File newFile = new File(f.AIS_DIR_PATH + "allAisData.txt");
        f.writeToFile(newFile, sb);
        long endTime = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (endTime - startTime) + "ms");

        // elapsed time
        startTime = System.currentTimeMillis();
        f.readFromFile();
        endTime = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (endTime - startTime) + "ms");

    }
}
