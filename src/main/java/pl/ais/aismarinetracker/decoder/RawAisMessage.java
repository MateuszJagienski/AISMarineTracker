package pl.ais.aismarinetracker.decoder;

public record RawAisMessage(String identifier, int countOfFragments, int fragmentNumber,
                            String sequentialMessageId, String channelCode, String dataPayload,
                            int fillBits, String checksum) { }