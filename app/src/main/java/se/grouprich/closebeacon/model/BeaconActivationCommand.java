package se.grouprich.closebeacon.model;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

import se.grouprich.closebeacon.requestresponsemanager.bytearraybuilder.ByteArrayBuilder;

public final class BeaconActivationCommand {

    private byte[] sha1Hash;
    private byte[] companyId;
    private byte id1;
    private byte id2;
    private byte[] proximityUuidByteArray;
    private byte[] majorNumber;
    private byte[] minorNumber;
    private byte power;
    private byte[] filling;

    public BeaconActivationCommand(byte[] sha1Hash, String proximityUuidString, String majorNumberString, String minorNumberString) {

        this.sha1Hash = sha1Hash;
        companyId = ByteBuffer.allocate(2).put((byte) 0x004C).array();
        id1 = 1;
        id2 = 21;

        UUID proximityUuid = UUID.fromString(proximityUuidString);
        proximityUuidByteArray = ByteBuffer.allocate(16)
                .putLong(proximityUuid.getMostSignificantBits())
                .putLong(proximityUuid.getLeastSignificantBits())
                .array();

        majorNumber = ByteArrayBuilder.buildByteArrayFromNumberAsString(majorNumberString);
        minorNumber = ByteArrayBuilder.buildByteArrayFromNumberAsString(minorNumberString);
        power = (byte) 0xC5;

        filling = new byte[46];
        Arrays.fill(filling, (byte) 0);
    }

    public byte[] getSha1Hash() {
        return sha1Hash;
    }

    public byte[] getCompanyId() {
        return companyId;
    }

    public byte getId1() {
        return id1;
    }

    public byte getId2() {
        return id2;
    }

    public byte[] getProximityUuidByteArray() {
        return proximityUuidByteArray;
    }

    public byte[] getMajorNumber() {
        return majorNumber;
    }

    public byte[] getMinorNumber() {
        return minorNumber;
    }

    public byte getPower() {
        return power;
    }

    public byte[] getFilling() {
        return filling;
    }
}
