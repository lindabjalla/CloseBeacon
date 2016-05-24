package se.grouprich.closebeacon.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.service.BluetoothLeService;

@TargetApi(Build.VERSION_CODES.M)
public final class DeviceControlActivity extends Activity {

    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    private TextView dataValue;
    private TextView gattServicesDiscovered;
    private TextView connectionState;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private byte[] activationCommand;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {

            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();

            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }

            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();

            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {

                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {

                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

                gattServicesDiscovered.setText(R.string.gatt_services_discovered);

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_device_control);

        dataValue = (TextView) findViewById(R.id.data_value);
        final TextView macAddressTextView = (TextView) findViewById(R.id.text_view_mac_address);
        gattServicesDiscovered = (TextView) findViewById(R.id.gatt_services_discovered);
        connectionState = (TextView) findViewById(R.id.text_connection_state);
        final Intent intent = getIntent();
        mDeviceAddress = intent.getStringExtra(DeviceDetailsActivity.MAC_ADDRESS_KEY);
        activationCommand = intent.getByteArrayExtra(DeviceDetailsActivity.ACTIVATION_COMMAND_KEY);

        macAddressTextView.setText(mDeviceAddress);

        final Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {

        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        if (mBluetoothLeService != null) {

            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    private void updateConnectionState(final int resourceId) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectionState.setText(resourceId);
            }
        });
    }

    private void displayData(String data) {

        if (data != null) {
            dataValue.setText(data);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {

        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);

        return intentFilter;
    }

    public void onClickWrite(View view) throws InterruptedException {

        if (mBluetoothLeService != null) {

            final String characteristicUuid = "19721006-2004-2007-2014-acc0cbeac010";

            mBluetoothLeService.connect(mDeviceAddress);

            mBluetoothLeService.writeCharacteristic(ScanActivity.SERVICE_UUID, characteristicUuid, activationCommand);
        }
    }
}