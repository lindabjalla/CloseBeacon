package se.grouprich.closebeacon.requestresponsemanager.converter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public final class SHA1Converter {

    public static String byteArrayToSHA1(byte[] byteArrayToConvert) throws NoSuchAlgorithmException {

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

    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }
}
