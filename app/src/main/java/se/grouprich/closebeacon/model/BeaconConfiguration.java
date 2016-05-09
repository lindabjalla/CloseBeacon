package se.grouprich.closebeacon.model;

import java.util.UUID;

public final class BeaconConfiguration {

    private String serialNumber;
    private UUID uuid;
    private String major;
    private String minor;

    public BeaconConfiguration(String serialNumber, UUID uuid, String major, String minor) {

        this.serialNumber = serialNumber;
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
    }
}
