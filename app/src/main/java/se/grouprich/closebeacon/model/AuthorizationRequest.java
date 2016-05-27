package se.grouprich.closebeacon.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.SecureRandom;
import java.util.Random;

public final class AuthorizationRequest {

    private byte[] protocolVersion;
    private byte[] authenticationCode;
    private byte[] salt;

    public AuthorizationRequest(String authenticationCode) {

        protocolVersion = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(1).array();
        this.authenticationCode = authenticationCode.getBytes();
        salt = new byte[4];
        final Random random = new SecureRandom();
        random.nextBytes(salt);
    }

    public byte[] buildByteArray() {

        return ByteBuffer.allocate(20)
                .put(protocolVersion)
                .put(authenticationCode)
                .put(salt)
                .array();
    }

    public byte[] buildResponseOkByteArray() {

        String ok = "OK";

        return ByteBuffer.allocate(22)
                .put(buildByteArray())
                .put(ok.getBytes())
                .array();
    }

    public byte[] buildResponseUnknownByteArray() {

        String unknown = "UNKNOWN";

        return ByteBuffer.allocate(27)
                .put(buildByteArray())
                .put(unknown.getBytes())
                .array();
    }
}
