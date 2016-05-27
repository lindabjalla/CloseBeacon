package se.grouprich.closebeacon.requestresponsemanager;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.PublicKey;

import javax.crypto.Cipher;

public final class RequestBuilder {

    private static byte[] encryptRequest(PublicKey publicKey, byte[] request) throws Exception {

        Cipher cipher = Cipher.getInstance("rsa/none/pkcs1padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(request);
    }

    private static String encodeRequest(byte[] encryptedRequest) throws UnsupportedEncodingException {

        return Base64.encodeToString(encryptedRequest, Base64.DEFAULT);
    }

    public static String buildRequestAsString(PublicKey publicKey, byte[] byteArray) throws Exception {

        final byte[] encryptedByteArray = encryptRequest(publicKey, byteArray);
        return encodeRequest(encryptedByteArray);
    }
}
