package se.grouprich.closebeacon;

import android.util.Base64;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;

public final class RequestBuilder {

    public byte[] buildAuthorizationRequest(String authorizationCode) {

        int protocolVersion = 1;

        final byte[] protocolVersionBytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
                .putInt(protocolVersion).array();

        final byte[] authorizationCodeBytes = authorizationCode.getBytes();

        final Random random = new SecureRandom();
        final byte[] salt = new byte[32];
        random.nextBytes(salt);

        final byte[] authorizationRequest = new byte[protocolVersionBytes.length + authorizationCodeBytes.length + salt.length];
        System.arraycopy(protocolVersionBytes, 0, authorizationRequest, 0, protocolVersionBytes.length);
        System.arraycopy(authorizationCodeBytes, 0, authorizationRequest, protocolVersionBytes.length,
                authorizationCodeBytes.length);
        System.arraycopy(salt, 0, authorizationRequest, protocolVersionBytes.length + authorizationCodeBytes.length,
                salt.length);

        return authorizationRequest;
    }

    public byte[] encryptRequest(PublicKey publicKey, byte[] request) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(request);
    }

    public String encodeRequest(byte[] encryptedRequest) {

        return Base64.encodeToString(encryptedRequest, Base64.DEFAULT);
    }
}
