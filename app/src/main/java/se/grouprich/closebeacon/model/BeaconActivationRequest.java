package se.grouprich.closebeacon.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;
import java.util.UUID;

public class BeaconActivationRequest {

    private byte[] protocolVersion;
    private byte[] authCode;
    private byte[] macAddress;
    private byte[] adminKey;
    private byte[] mobileKey;
    private byte beaconType;
    private byte[] majorNumber;
    private byte[] minorNumber;
    private byte[] proximityUuidAsByteArray;

    public BeaconActivationRequest(String authCode, String macAddress, String majorNumber,
                                   String minorNumber, String proximityUuidAsString) {

        protocolVersion = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(1).array();
        this.authCode = authCode.getBytes();
        this.macAddress = convertMacAddressToByteArray(macAddress);

        adminKey = new byte[20];
        new Random().nextBytes(adminKey);

        mobileKey = new byte[20];
        new Random().nextBytes(mobileKey);

        beaconType = 100;

        this.majorNumber = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN)
                .put(convertMajorMinorNumberToByteArray(majorNumber))
                .array();

        this.minorNumber = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN)
                .put(convertMajorMinorNumberToByteArray(minorNumber))
                .array();

        UUID proximityUuid = UUID.fromString(proximityUuidAsString);
        proximityUuidAsByteArray = ByteBuffer.allocate(16)
                .putLong(proximityUuid.getMostSignificantBits())
                .putLong(proximityUuid.getLeastSignificantBits())
                .array();
    }

    private byte[] convertMacAddressToByteArray(String macAddress){

        String[] macAddressArray = macAddress.split(":");

        byte[] macAddressAsByteArray = new byte[6];

        for(int i = 0; i < macAddressArray.length; i++){

            macAddressAsByteArray[i] = Integer.decode("0x" + macAddressArray[i]).byteValue();
        }

        return macAddressAsByteArray;
    }

    private byte[] convertMajorMinorNumberToByteArray(String string) {

        byte[] byteArray = new byte[2];

        for (int i = 0; i < string.length(); i++) {

            final byte parsedByte = Byte.parseByte(String.valueOf(string.charAt(i)));
            byteArray[i] = parsedByte;
        }

        return byteArray;
    }

    public byte[] buildActivationRequestAsByteArray(){

        return ByteBuffer.allocate(83)
                .put(protocolVersion)
                .put(authCode)
                .put(macAddress)
                .put(adminKey)
                .put(mobileKey)
                .put(beaconType)
                .put(majorNumber)
                .put(minorNumber)
                .put(proximityUuidAsByteArray)
                .array();
    }
}
