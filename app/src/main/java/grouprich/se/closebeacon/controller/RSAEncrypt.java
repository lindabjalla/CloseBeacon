package grouprich.se.closebeacon.controller;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class RSAEncrypt {

    private final static String RSA = "RSA";
    private byte[] encryptedBytes;
    private PublicKey publicRSA;
    private String encrypted;

    private String encrypt(final String plain) throws Exception {

        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicRSA);
        encryptedBytes = cipher.doFinal(plain.getBytes());
        encrypted = new String(encryptedBytes);

        return encrypted;
    }

    public String descrypt(final String result)
    {
        return null;
    }







}
