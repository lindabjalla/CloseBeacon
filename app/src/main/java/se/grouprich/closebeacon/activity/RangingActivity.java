package se.grouprich.closebeacon.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.adapter.ActivatedBeaconAdapter;


public class RangingActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;
    private SharedPreferences preferences;
    private String proximityUuid;
    private String majorString;
    private String minorString;
    private byte[] major;
    private byte[] minor;
    private Context context = this;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ActivatedBeaconAdapter adapter;
    private Set<Beacon> activatedBeacons;
    private final Region ALL_BEACONS_REGION = new Region("allBeacons", null, null, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);

        preferences = getSharedPreferences(MainActivity.BEACON_PREFERENCES, MODE_PRIVATE);
        proximityUuid = preferences.getString(DeviceDetailsActivity.PROXIMITY_UUID_KEY, null);
        majorString = preferences.getString(DeviceDetailsActivity.MAJOR_KEY, null);
        minorString = preferences.getString(DeviceDetailsActivity.MINOR_KEY, null);
        major = Base64.decode(majorString, Base64.DEFAULT);
        minor = Base64.decode(minorString, Base64.DEFAULT);

        activatedBeacons = new LinkedHashSet<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_ranging);
        layoutManager = new LinearLayoutManager(context);
        adapter = new ActivatedBeaconAdapter(context, activatedBeacons);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        // beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId",
                    null, null, null));

        } catch (RemoteException e) {
        }

    }

//        beaconManager.setRangeNotifier(new RangeNotifier() {
//            @Override
//            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//
//                Set<Beacon> activatedBeacons = new LinkedHashSet<>();
//
////                recyclerView = (RecyclerView) findViewById(R.id.recycler_view_ranging);
////                layoutManager = new LinearLayoutManager(context);
//
////                if (beacons.size() > 0) {
////
//////                    EditText editText = (EditText) findViewById(R.id.rangingText);
////
////                    for (Beacon beacon : beacons) {
//
////                        if (beacon.getId1() != null) {
//
//                System.out.println(beacons.toString());
//                             activatedBeacons.addAll(beacons);
////                            activatedBeacons.add(beacon);
////                            Log.d("activatedBeacon", beacon.toString() + " is about " + beacon.getDistance() + " meters away." + "serviceUUID " + beacon.getServiceUuid());
////                        }
////                    }
////                }
//
//                adapter = new ActivatedBeaconAdapter(context, activatedBeacons);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        recyclerView.setLayoutManager(layoutManager);
//                        recyclerView.setAdapter(adapter);
//
//                    }
//                });
//
//            }
//        });
//
//        try {
//            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId",
//                    Identifier.parse(proximityUuid),
//                    Identifier.fromBytes(major, 0, major.length-1, true),
//                    Identifier.fromBytes(minor, 0, minor.length-1, true)));
//        } catch (RemoteException e) {   }
//    }

//    private BluetoothAdapter.LeScanCallback mLeScanCallback =
//            new BluetoothAdapter.LeScanCallback() {
//
//                @Override
//                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            runOnUiThread(new Runnable() {
//
//
//                                @Override
//                                public void run() {
//                                    activatedBeacons.add(device);
//                                    recyclerView.setLayoutManager(layoutManager);
//                                    recyclerView.setAdapter(adapter);
//                                }
//                            });
//
//                        }
//                    });
//                }
//            };
}