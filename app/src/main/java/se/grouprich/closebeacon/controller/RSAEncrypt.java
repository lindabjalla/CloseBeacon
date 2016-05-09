package se.grouprich.closebeacon.controller;


import android.util.Base64;

import java.security.PublicKey;
import java.util.Random;

import javax.crypto.Cipher;

public final class RSAEncrypt {

    private final static String RSA = "RSA";
    private byte[] encryptedBytes;
    private PublicKey publicRSA;
    private String encrypted;
    private byte[] authorizationRequest;

    public RSAEncrypt(PublicKey publicRSA) {

        this.publicRSA = publicRSA;
    }

    public String encrypt(PublicKey publicKey, byte[] authorizationRequest) throws Exception {

        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicRSA);
        encryptedBytes = cipher.doFinal(authorizationRequest);
        encrypted = new String(encryptedBytes);

        return encrypted;
    }

    public byte[] encodeBase64Authcode(String authorizationCode) {

        StringBuilder authCodeRequest = new StringBuilder();
        String protocolVersion = "0001";

        authCodeRequest.append(protocolVersion);
        authCodeRequest.append(authorizationCode);
        authCodeRequest.append(randomNumbers());

        byte[] authCode = authCodeRequest.toString().getBytes();

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
