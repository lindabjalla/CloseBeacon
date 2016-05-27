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

    public BeaconActivationRequest(String authCode, String macAddress, String majorNumberAsString,
                                   String minorNumberAsString, String proximityUuidAsString) {

        protocolVersion = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(1).array();
        this.authCode = authCode.getBytes();
        this.macAddress = macAddressToByteArray(macAddress);

        adminKey = new byte[20];
        new Random().nextBytes(adminKey);

        mobileKey = new byte[20];
        new Random().nextBytes(mobileKey);

        beaconType = 100;

        this.majorNumber = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN)
                .putShort(Short.parseShort(majorNumberAsString))
                .array();

        this.minorNumber = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN)
                .putShort(Short.parseShort(minorNumberAsString))
                .array();

        UUID proximityUuid = UUID.fromString(proximityUuidAsString);
        proximityUuidAsByteArray = ByteBuffer.allocate(16)
                .putLong(proximityUuid.getMostSignificantBits())
                .putLong(proximityUuid.getLeastSignificantBits())
                .array();
    }

    public byte[] getAdminKey() {
        return adminKey;
    }

    public byte[] getMobileKey() {
        return mobileKey;
    }

    public static byte[] macAddressToByteArray(String macAddress) {

        String[] macAddressArray = macAddress.split(":");

        byte[] macAddressAsByteArray = new byte[6];

        for (int i = 0; i < macAddressArray.length; i++) {

            macAddressAsByteArray[i] = Integer.decode("0x" + macAddressArray[i]).byteValue();
        }

        return macAddressAsByteArray;
    }

    public byte[] buildByteArray() {

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
