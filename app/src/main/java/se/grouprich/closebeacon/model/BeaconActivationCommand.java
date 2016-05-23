package se.grouprich.closebeacon.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;
import java.util.UUID;

import se.grouprich.closebeacon.requestresponsemanager.bytearraybuilder.ByteArrayBuilder;

public final class BeaconActivationCommand {

    private byte commandCode;
    private byte[] adminKey;
    private byte[] mobileKey;
    private byte beaconType;
    private byte[] majorNumber;
    private byte[] minorNumber;
    private byte[] sha1Hash;
    private byte[] companyId;
    private byte id1;
    private byte id2;
    private byte[] proximityUuidByteArray;
    private byte power;

    public BeaconActivationCommand(byte[] adminKey, byte[] mobileKey, byte[] sha1Hash, String proximityUuidString, String majorNumberString, String minorNumberString) {

        commandCode = 80;
        this.adminKey = adminKey;
        this.mobileKey = mobileKey;

        beaconType = 100;

        majorNumber = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN)
                .put(ByteArrayBuilder.numberStringToByteArray(majorNumberString))
                .array();

        minorNumber = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN)
                .put(ByteArrayBuilder.numberStringToByteArray(minorNumberString))
                .array();

        this.sha1Hash = sha1Hash;
        companyId = ByteBuffer.allocate(2).put((byte) 0x004C).array();

        id1 = 2;
        id2 = 21;

        UUID proximityUuid = UUID.fromString(proximityUuidString);
        proximityUuidByteArray = ByteBuffer.allocate(16)
                .putLong(proximityUuid.getMostSignificantBits())
                .putLong(proximityUuid.getLeastSignificantBits())
                .array();

        power = (byte) 0xC5;
    }

    public byte[] getMajorNumber() {
        return majorNumber;
    }

    public byte[] getMinorNumber() {
        return minorNumber;
    }

    public byte[] buildByteArray() {

        return ByteBuffer.allocate(91)
                .put(commandCode)
                .put(adminKey)
                .put(mobileKey)
                .put(beaconType)
                .put(majorNumber)
                .put(minorNumber)
                .put(sha1Hash)
                .put(companyId)
                .put(id1)
                .put(id2)
                .put(proximityUuidByteArray)
                .put(majorNumber)
                .put(minorNumber)
                .put(power)
                .array();
    }
}
