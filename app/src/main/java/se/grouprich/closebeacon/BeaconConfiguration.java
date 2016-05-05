package se.grouprich.closebeacon;

public final class BeaconConfiguration {

    private String uuid;
    private String major;
    private String minor;

    public BeaconConfiguration(String uuid, String major, String minor) {

        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
    }
}
