package pl.ais.aismarinetracker.decoder;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class Decoders {


    private static final Map<Integer, String> sixBitMap = new TreeMap<>();

    static {
        sixBitMap.put(0, "@"); // 0
        sixBitMap.put(1, "A"); // 1
        sixBitMap.put(2, "B"); // 2
        sixBitMap.put(3, "C"); // 3
        sixBitMap.put(4, "D"); // 4
        sixBitMap.put(5, "E"); // 5
        sixBitMap.put(6, "F"); // 6
        sixBitMap.put(7, "G"); // 7
        sixBitMap.put(8, "H"); // 8
        sixBitMap.put(9, "I"); // 9
        sixBitMap.put(10, "J"); // 10
        sixBitMap.put(11, "K"); // 11
        sixBitMap.put(12, "L"); // 12
        sixBitMap.put(13, "M"); // 13
        sixBitMap.put(14, "N"); // 14
        sixBitMap.put(15, "O"); // 15
        sixBitMap.put(16, "P"); // 16
        sixBitMap.put(17, "Q"); // 17
        sixBitMap.put(18, "R"); // 18
        sixBitMap.put(19, "S"); // 19
        sixBitMap.put(20, "T"); // 20
        sixBitMap.put(21, "U"); // 21
        sixBitMap.put(22, "V"); // 22
        sixBitMap.put(23, "W"); // 23
        sixBitMap.put(24, "X"); // 24
        sixBitMap.put(25, "Y"); // 25
        sixBitMap.put(26, "Z"); // 26
        sixBitMap.put(27, "["); // 27
        sixBitMap.put(28, "\\"); // 28
        sixBitMap.put(29, "]"); // 29
        sixBitMap.put(30, "^"); // 30
        sixBitMap.put(31, "_"); // 31
        sixBitMap.put(32, " "); // 32
        sixBitMap.put(33, "!"); // 33
        sixBitMap.put(34, "\""); // 34
        sixBitMap.put(35, "#"); // 35
        sixBitMap.put(36, "$"); // 36
        sixBitMap.put(37, "%"); // 37
        sixBitMap.put(38, "&"); // 38
        sixBitMap.put(39, "'"); // 39
        sixBitMap.put(40, "("); // 40
        sixBitMap.put(41, ")"); // 41
        sixBitMap.put(42, "*"); // 42
        sixBitMap.put(43, "+"); // 43
        sixBitMap.put(44, ","); // 44
        sixBitMap.put(45, "-"); // 45
        sixBitMap.put(46, "."); // 46
        sixBitMap.put(47, "/"); // 47
        sixBitMap.put(48, "0"); // 48
        sixBitMap.put(49, "1"); // 49
        sixBitMap.put(50, "2"); // 50
        sixBitMap.put(51, "3"); // 51
        sixBitMap.put(52, "4"); // 52
        sixBitMap.put(53, "5"); // 53
        sixBitMap.put(54, "6"); // 54
        sixBitMap.put(55, "7"); // 55
        sixBitMap.put(56, "8"); // 56
        sixBitMap.put(57, "9"); // 57
        sixBitMap.put(58, ":"); // 58
        sixBitMap.put(59, ";"); // 59
        sixBitMap.put(60, "<"); // 60
        sixBitMap.put(61, "="); // 61
        sixBitMap.put(62, ">"); // 62
        sixBitMap.put(63, "?"); // 63

    }

    public static String undoArmouringToBinaryString(String payload) {
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < payload.length(); i++) {
            int ascii = payload.charAt(i);
            int decimal = ascii - 48;
            if (decimal > 40) {
                decimal -= 8;
            }
            binary.append(String.format("%6s", Integer.toString(decimal, 2))
                    .replace(' ', '0'));
        }
        return binary.toString();
    }

    public static String concatenateMessagesPayloads(RawAisMessage[] rawAisMessages) {
        StringBuilder sb = new StringBuilder();
        for (var rawAisMessage : rawAisMessages) {
            sb.append(rawAisMessage.dataPayload());
        }
        return sb.toString();
    }

    public static String calculateChecksum(String message) {
        int checksum = IntStream.range(0, message.length())
                .map(message::charAt)
                .reduce(0, (a, b) -> a ^ b);
        return Integer.toHexString(checksum);
    }

    public static Integer toInteger(String bitString) {
        char bitSign = bitString.charAt(0);
        var bits = bitString.substring(1);
        if (bitSign == '0') {
            return toUnsignedInteger(bitString.substring(1));
        }
        bits = bits
                .replace('0', 'x')
                .replace('1', '0')
                .replace('x', '1');
        return -1 - toUnsignedInteger(bits);
    }

    public static Integer toUnsignedInteger(String bitString) {
        return Integer.parseInt(bitString, 2);
    }

    public static Float toFloat(String bitString) {
        return Float.valueOf(toInteger(bitString));
    }

    public static Boolean toBoolean(String bitString) {
        return bitString.charAt(0) == '1';
    }

    public static Long toUnsignedLong(String bitString) {
        return Long.parseUnsignedLong(bitString, 2);
    }

    public static Float toUnsignedFloat(String bitString) {
        return Float.valueOf(toUnsignedInteger(bitString));
    }

    public static String toAsciiString(String bitString) {
        StringBuilder sb = new StringBuilder();
        while (bitString.length() >= 6) {
            var sixBit = bitString.substring(0, 6);
            sb.append(sixBitMap.get(toUnsignedInteger(sixBit)));
            bitString = bitString.substring(6);
        }

        return sb.toString().replaceAll("@+", " ").trim(); // strip @ signs
    }
}