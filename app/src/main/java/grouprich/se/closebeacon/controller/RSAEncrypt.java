package grouprich.se.closebeacon.controller;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class RSAEncrypt {

    private final static String RSA = "RSA";
    public static PublicKey publicKey;
    public static PrivateKey privateKey;

    public static void generateKey() throws Exception{
        KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);

        generator.initialize(512, new SecureRandom());

        KeyPair keyPair = generator.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }


}
