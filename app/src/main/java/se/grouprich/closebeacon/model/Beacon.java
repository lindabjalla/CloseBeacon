package se.grouprich.closebeacon.model;

import java.util.UUID;

public class Beacon {

    private String name;
    private String address;
    private String rssi;
    private String serviceUuid;
    private String serialNumber;
    private String proximityUuid;

    public Beacon(String name, String address, String rssi, String serviceUuid) {

        this.name = name;
        this.address = address;
        this.rssi = rssi;
        this.serviceUuid = serviceUuid;
        this.serialNumber = createSerialNumber(address);
        proximityUuid = UUID.randomUUID().toString();
    }

    public String getMacAddress() {

        return address;
    }

    public String getName() {

        return name;
    }

    public String getRssi() {

        return rssi;
    }

    public String getServiceUuid() {

        return serviceUuid;
    }

    public String getSerialNumber() {

        return serialNumber;
    }

    public String getProximityUuid(){

        return proximityUuid;
    }

    private String createSerialNumber(String macAddress) {

        String[] macAddressArray = macAddress.split(":");
        byte[] macAddressAsByte = new byte[6];
        int[] serialNumberAsInt = new int[6];
        StringBuilder createdSerialNumber = new StringBuilder();

        for (int i = 0; i < macAddressArray.length; i++) {
            macAddressAsByte[i] = Integer.decode("0x" + macAddressArray[i]).byteValue();
            serialNumberAsInt[i] = macAddressAsByte[i] & 0xff;
            String stringToAppend = String.format("%03d", serialNumberAsInt[i]);

            createdSerialNumber.append(stringToAppend);
            if (i < macAddressArray.length - 1) {
                createdSerialNumber.append("-");
            }
        }

        return createdSerialNumber.toString();
    }

    private byte[] convertMacAddressToByteArray(String macAddress){

        String[] macAddressArray = macAddress.split(":");

        byte[] macAddressAsByteArray = new byte[6];

        for(int i = 0; i < macAddressArray.length; i++){

            macAddressAsByteArray[i] = Integer.decode("0x" + macAddressArray[i]).byteValue();
        }

        return macAddressAsByteArray;
    }
}
