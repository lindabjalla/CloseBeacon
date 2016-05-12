package se.grouprich.closebeacon.httprequestresponsemanager.bytearraybuilder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.SecureRandom;
import java.util.Random;

public final class AuthorizationByteArrayBuilder {

    public byte[] buildAuthRequestByteArray(String authenticationCode) {

        int protocolVersion = 1;

        final byte[] protocolVersionBytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
                .putInt(protocolVersion).array();

        final byte[] authenticationCodeBytes = authenticationCode.getBytes();

        final Random random = new SecureRandom();
        final byte[] salt = new byte[4];
        random.nextBytes(salt);

        return ByteBuffer.allocate(20)
                .put(protocolVersionBytes)
                .put(authenticationCodeBytes)
                .put(salt)
                .array();
    }

    public byte[] buildResponseOkByteArray(byte[] authRequestByteArray) {

        String ok = "OK";

        return ByteBuffer.allocate(22)
                .put(authRequestByteArray)
                .put(ok.getBytes())
                .array();
    }

    public byte[] buildResponseUnknownByteArray(byte[] authRequestByteArray) {

        String unknown = "UNKNOWN";

        return ByteBuffer.allocate(27)
                .put(authRequestByteArray)
                .put(unknown.getBytes())
                .array();
    }
}
