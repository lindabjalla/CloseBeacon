package se.grouprich.closebeacon.requestresponsemanager.bytearraybuilder;

public final class ByteArrayBuilder {

    public static byte[] buildByteArrayFromNumberAsString(String string) {

        byte[] byteArray = new byte[0];

        for (int i = 0; i < string.length(); i++) {

            final byte parsedByte = Byte.parseByte(String.valueOf(string.charAt(i)));
            byteArray[i] = parsedByte;
        }

        return byteArray;
    }
}
