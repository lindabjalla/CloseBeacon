package se.grouprich.closebeacon.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.adapter.BeaconAdapter;
import se.grouprich.closebeacon.model.Beacon;
import se.grouprich.closebeacon.requestresponsemanager.converter.SHA1Converter;

@TargetApi(21)
public class ScanActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 1000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothGatt mGatt;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<Beacon> beacons;
    private final String serviceUuid = "19721006-2004-2007-2014-acc0cbeac000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mHandler = new Handler();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

            Toast.makeText(this, "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();

            finish();
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    @Override
    protected void onResume() {

        super.onResume();
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        } else {

            if (Build.VERSION.SDK_INT >= 21) {

                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                filters = new ArrayList<ScanFilter>();
                beacons = Collections.synchronizedList(new ArrayList<Beacon>());

            }

            scanLeDevice(true);
        }

    }

    @Override
    protected void onPause() {

        super.onPause();

        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {

            scanLeDevice(false);
        }
    }

    @Override
    protected void onDestroy() {

        if (mGatt == null) {

            return;
        }

        mGatt.close();
        mGatt = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_ENABLE_BT) {

            if (resultCode == Activity.RESULT_CANCELED) {
                //Bluetooth not enabled.
                finish();
                return;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void scanLeDevice(final boolean enable) {

        if (enable) {
//            mHandler.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {

                    if (Build.VERSION.SDK_INT < 21) {

                        mBluetoothAdapter.stopLeScan(mLeScanCallback);

                    } else {

                        mLEScanner.stopScan(mScanCallback);
                    }
//                }
//            }, SCAN_PERIOD);

            if (Build.VERSION.SDK_INT < 21) {

                mBluetoothAdapter.startLeScan(mLeScanCallback);

            } else {

                mLEScanner.startScan(filters, settings, mScanCallback);
            }


        } else {

            if (Build.VERSION.SDK_INT < 21) {

                mBluetoothAdapter.stopLeScan(mLeScanCallback);

            } else {

                mLEScanner.stopScan(mScanCallback);
            }
        }
    }

    private ScanCallback mScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            //Log.i("callbackType", String.valueOf(callbackType));
            //Log.i("result", result.toString());

            BluetoothDevice btDevice = result.getDevice();

            Log.i("** RECORD *****  ", String.valueOf(result.getScanRecord()));
            Log.i("** RECORD2 *****  ", String.valueOf(result.getScanRecord().getManufacturerSpecificData()));

//            String uuidFromScan = String.valueOf(result.getScanRecord().getServiceUuids());

            final List<ParcelUuid> serviceUuids = result.getScanRecord().getServiceUuids();
            final List<String> serviceUuidsString = new ArrayList<>();

            if(serviceUuids != null) {

                for (ParcelUuid serviceUuid : serviceUuids) {

                    serviceUuidsString.add(serviceUuid.toString());
                }
            }

            if (serviceUuidsString.contains(serviceUuid) && checkMacAddress(String.valueOf(result.getDevice().getAddress()))){

//                if ((uuidFromScan != null) && (serviceUuid.equals(uuidFromScan)) &&
//                        checkMacAddress(String.valueOf(result.getDevice().getAddress()))) {

                    Beacon iBeacon = new Beacon(String.valueOf(result.getDevice().getName()),
                            String.valueOf(result.getDevice().getAddress()),
                            String.valueOf(result.getRssi()),
//                            String.valueOf(result.getScanRecord().getServiceUuids()).replace("[", "").replace("]", ""));
                            serviceUuid);

                    //addBeacon(iBeacon);
                    Log.i("**== ADDED ***", "");
                    beacons.add(iBeacon);
                }

            displayBeaconsList();

            connectToDevice(btDevice);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {

            for (ScanResult sr : results) {
                Log.i("## ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("onLeScan", device.toString());
                            connectToDevice(device);
                        }
                    });
                }
            };

    public void connectToDevice(BluetoothDevice device) {

        if (mGatt == null) {

            mGatt = device.connectGatt(this, false, gattCallback);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    scanLeDevice(false);// will stop after five seconds
                }
            }, 10000);

        }else{

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    scanLeDevice(false);// will stop after five seconds
                }
            }, 10000);
        }
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

            Log.i("onConnectionStateChange", "Status: " + status);

            switch (newState) {

                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    gatt.discoverServices();
                    break;

                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    break;

                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            List<BluetoothGattService> services = gatt.getServices();
            Log.i("onServicesDiscovered", services.toString());
            if(!services.isEmpty()) {
                gatt.readCharacteristic(services.get(1).getCharacteristics().get(0));
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

            Log.i("onCharacteristicRead", characteristic.toString());
            gatt.disconnect();
        }
    };

    public void displayBeaconsList() {

        recyclerView = (RecyclerView) findViewById(R.id.beacons_recycler_view);
        layoutManager = new LinearLayoutManager(this);

        adapter = new BeaconAdapter(this, beacons);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public boolean checkMacAddress(String address) {

        if (beacons.size() != 0) {

            Iterator<Beacon> it = beacons.iterator();

            while (it.hasNext()) {

                Beacon beacon = it.next();
                String addressBeacon = String.valueOf(beacon.getMacAddress());
                boolean bool = (addressBeacon.equals(address));

                if (bool) {

                    return false;
                }
            }
        }

        return true;
    }

    public void addBeacon(Beacon iBeacon) {

        if (beacons.size() != 0) {

            Iterator<Beacon> it = beacons.iterator();

            while (it.hasNext()) {

                Beacon beacon = it.next();
                String addressBeacon = String.valueOf(beacon.getMacAddress());
                String addressIBeacon = String.valueOf(iBeacon.getMacAddress());
                boolean bool = (addressBeacon.equals(addressIBeacon));

                if (bool) {

                    it.remove();
                }
            }
        }

        beacons.add(iBeacon);
    }
}
