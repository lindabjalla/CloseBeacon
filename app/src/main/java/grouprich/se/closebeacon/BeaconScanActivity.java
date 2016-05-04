package grouprich.se.closebeacon;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BeaconScanActivity extends ListActivity {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;

    private boolean scanning;
    private Handler handler;

    private static final long SCAN_PERIOD = 10000;

    private List<BluetoothDevice> devices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scan);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    bluetoothLeScanner.stopScan(new ScanCallback() {
                        @Override
                        public void onScanResult(int callbackType, ScanResult result) {
                            super.onScanResult(callbackType, result);

                            final BluetoothDevice device = result.getDevice();
                            final ParcelUuid[] uuidArray = device.getUuids();
                            List<String> uuidList = new ArrayList<>();

                            for (ParcelUuid uuid : uuidArray) {
                                uuidList.add(uuid.toString());
                            }

                            if(uuidList.contains("19721006-2004-2007-2014-acc0cbeac000")){
                               devices.add(device);
                            }
                        }
                    });
                }
            }, SCAN_PERIOD);

            scanning = true;
            bluetoothLeScanner.startScan(new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    //TODO hantera result
                }
            });
        } else {
            scanning = false;
            bluetoothLeScanner.stopScan(new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    //TODO hantera result
                }
            });
        }
    }
}