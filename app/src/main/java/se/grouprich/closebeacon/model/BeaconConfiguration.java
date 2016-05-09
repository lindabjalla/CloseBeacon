package se.grouprich.closebeacon.model;

public final class BeaconConfiguration {

    private String serialNumber;
    private String uuid;
    private String major;
    private String minor;

    public BeaconConfiguration(String serialNumber, String uuid, String major, String minor) {

        this.serialNumber = serialNumber;
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
    }
}
