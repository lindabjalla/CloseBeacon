package se.grouprich.closebeacon.httprequestresponsemanager;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.PublicKey;

import javax.crypto.Cipher;

public abstract class RequestBuilder {

    protected int protocolVersion = 1;

    private static byte[] encryptRequest(PublicKey publicKey, byte[] request) throws Exception {

        Cipher cipher = Cipher.getInstance("rsa/none/pkcs1padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(request);
    }

    private static String encodeRequest(byte[] encryptedRequest) throws UnsupportedEncodingException {

//        final String encodedRequest = Base64.encodeToString(encryptedRequest, Base64.DEFAULT);
        return Base64.encodeToString(encryptedRequest, Base64.DEFAULT);
//        return URLEncoder.encode(encodedRequest, "UTF-8");
    }

    public static String buildRequest(PublicKey publicKey, byte[] byteArray) throws Exception {

        final byte[] encreptedByteArray = encryptRequest(publicKey, byteArray);
        return encodeRequest(encreptedByteArray);
    }
}
