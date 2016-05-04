package grouprich.se.closebeacon.controller;


import android.util.Base64;

import java.security.PublicKey;
import java.util.Random;

import javax.crypto.Cipher;

public class RSAEncrypt {

    private final static String RSA = "RSA";
    private byte[] encryptedBytes;
    private PublicKey publicRSA;
    private String encrypted;

    public String encrypt(PublicKey publicKey, byte[] authorizationRequest) throws Exception {

        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicRSA);
        encryptedBytes = cipher.doFinal(authorizationRequest);
        encrypted = new String(encryptedBytes);

        return encrypted;
    }

    public byte[] encodeBase64Authcode(String authorizationCode) {
        StringBuilder authCodeResquest = new StringBuilder();
        String protokollVersion = "0001";

        authCodeResquest.append(protokollVersion);
        authCodeResquest.append(authorizationCode);
        authCodeResquest.append(randomNumbers());

        byte[] authCode = authCodeResquest.toString().getBytes();

        return Base64.encode(authCode, Base64.DEFAULT);
    }

    private String randomNumbers() {
        Random random = new Random();
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            number.append(random.nextInt(9));
        }
        return number.toString();
    }
}
