package se.grouprich.closebeacon.requestresponsemanager.bytearraybuilder;

import java.nio.ByteBuffer;

import se.grouprich.closebeacon.model.BeaconActivationCommand;

public final class ActivationCommandByteArrayBuilder {

    public byte[] buildByteArray(BeaconActivationCommand beaconActivationCommand) {

        return ByteBuffer.allocate(91)
                .put(beaconActivationCommand.getSha1Hash())
                .put(beaconActivationCommand.getCompanyId())
                .put(beaconActivationCommand.getId1())
                .put(beaconActivationCommand.getId2())
                .put(beaconActivationCommand.getProximityUuidByteArray())
                .put(beaconActivationCommand.getMajorNumber())
                .put(beaconActivationCommand.getMinorNumber())
                .put(beaconActivationCommand.getPower())
                .put(beaconActivationCommand.getFilling())
                .array();
    }
}
