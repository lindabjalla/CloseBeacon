package se.grouprich.closebeacon.requestresponsemanager.converter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public final class SHA1Converter {

    public static String convertToSHA1(byte[] byteArrayToConvert) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return byteArrayToHex(md.digest(byteArrayToConvert));
    }

    private static String byteArrayToHex(final byte[] byteArray) {

        Formatter formatter = new Formatter();

        for (byte aByte : byteArray) {

            formatter.format("%02x", aByte);
        }

        return formatter.toString();
    }
}
