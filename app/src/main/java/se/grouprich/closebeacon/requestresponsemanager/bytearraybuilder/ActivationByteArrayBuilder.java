package se.grouprich.closebeacon.requestresponsemanager.bytearraybuilder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

import se.grouprich.closebeacon.model.BeaconActivationRequest;

public final class ActivationByteArrayBuilder {

    public byte[] buildByteArray(BeaconActivationRequest beaconActivationRequest) {

        int protocolVersion = 1;

        final byte[] parsedMajorNumber = parseByteFromString(beaconActivationRequest.getMajorNumber());
        final byte[] parsedMinorNUmber = parseByteFromString(beaconActivationRequest.getMinorNumber());
        final UUID proximityUuid = beaconActivationRequest.getProximityUuid();

        final byte[] protocolVersionBytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
                .putInt(protocolVersion).array();
        final byte[] authCodeBytes = beaconActivationRequest.getAuthCode().getBytes();
        final byte[] macAddressBytes = beaconActivationRequest.getMacAddress();
        final byte[] adminKeyBytes = beaconActivationRequest.getAdminKey();
        final byte[] mobileKeyBytes = beaconActivationRequest.getMobileKey();
        final byte beaconType = beaconActivationRequest.getBeaconType();
        final byte[] majorNumberBytes = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).put(parsedMajorNumber).array();
        final byte[] minorNumberBytes = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).put(parsedMinorNUmber).array();
        final byte[] proximityUuidBytes = ByteBuffer.wrap(new byte[16]).putLong(proximityUuid.getMostSignificantBits())
                .putLong(proximityUuid.getLeastSignificantBits()).array();

        return ByteBuffer.allocate(83)
                .put(protocolVersionBytes)
                .put(authCodeBytes)
                .put(macAddressBytes)
                .put(adminKeyBytes)
                .put(mobileKeyBytes)
                .put(beaconType)
                .put(majorNumberBytes)
                .put(minorNumberBytes)
                .put(proximityUuidBytes)
                .array();
    }

    public static byte[] parseByteFromString(String string) {

        byte[] byteArray = new byte[0];

        for (int i = 0; i < string.length(); i++) {

            final byte parsedByte = Byte.parseByte(String.valueOf(string.charAt(i)));
            byteArray[i] = parsedByte;
        }

        return byteArray;
    }
}
