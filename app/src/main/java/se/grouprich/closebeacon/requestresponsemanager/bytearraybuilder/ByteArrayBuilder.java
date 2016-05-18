package se.grouprich.closebeacon.requestresponsemanager.bytearraybuilder;

public final class ByteArrayBuilder {

    public static byte[] numberStringToByteArray(String numberString) {

        byte[] byteArray = new byte[numberString.length()];

        for (int i = 0; i < byteArray.length; i++) {

            final byte parsedByte = Byte.parseByte(String.valueOf(numberString.charAt(i)));
            byteArray[i] = parsedByte;
        }

        return byteArray;
    }

    public static byte[] macAddressToByteArray(String macAddress){

        String[] macAddressArray = macAddress.split(":");

        byte[] macAddressAsByteArray = new byte[6];

        for(int i = 0; i < macAddressArray.length; i++){

            macAddressAsByteArray[i] = Integer.decode("0x" + macAddressArray[i]).byteValue();
        }

        return macAddressAsByteArray;
    }
}
