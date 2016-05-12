package se.grouprich.closebeacon.model;

import java.util.UUID;

public class BeaconActivationRequest {

    private int protocolVersion;
    private String authCode;
    private byte[] macAddress;
    private byte[] adminKey;
    private byte[] mobileKey;
    private byte beaconType;
    private String majorNumber;
    private String minorNumber;
    private UUID proximityUuid;

    public BeaconActivationRequest(String authCode, byte[] macAddress, byte[] adminKey, byte[] mobileKey, String majorNumber,
                                   String minorNumber, UUID proximityUuid) {

        protocolVersion = 1;
        this.authCode = authCode;
        this.macAddress = macAddress;
        this.adminKey = adminKey;
        this.mobileKey = mobileKey;
        beaconType = 100;
        this.majorNumber = majorNumber;
        this.minorNumber = minorNumber;
        this.proximityUuid = proximityUuid;
    }

    public int getProtocolVersion() {

        return protocolVersion;
    }

    public String getAuthCode() {

        return authCode;
    }

    public byte[] getMacAddress() {

        return macAddress;
    }

    public byte[] getAdminKey() {

        return adminKey;
    }

    public byte[] getMobileKey() {

        return mobileKey;
    }

    public byte getBeaconType() {

        return beaconType;
    }

    public String getMajorNumber() {

        return majorNumber;
    }

    public String getMinorNumber() {

        return minorNumber;
    }

    public UUID getProximityUuid() {

        return proximityUuid;
    }
}
