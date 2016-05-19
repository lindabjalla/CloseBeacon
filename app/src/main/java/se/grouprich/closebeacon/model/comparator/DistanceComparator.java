package se.grouprich.closebeacon.model.comparator;

import org.altbeacon.beacon.Beacon;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Beacon>{

    @Override
    public int compare(Beacon beacon1, Beacon beacon2) {
        return beacon1.getDistance() < beacon2.getDistance() ? -1 : beacon1.getDistance() == beacon2.getDistance() ? 0 : 1;
    }
}
