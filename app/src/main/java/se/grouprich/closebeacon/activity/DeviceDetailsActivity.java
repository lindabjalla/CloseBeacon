package se.grouprich.closebeacon.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.dialog.InvalidMajorMinorDialog;
import se.grouprich.closebeacon.dialog.NoUuidDialog;
import se.grouprich.closebeacon.dialog.UuidGeneratedDialog;

public class DeviceDetailsActivity extends AppCompatActivity {

    public static final String UUID_STRING_LIST_KEY = "se.grouprich.closebeacon.UUID_LIST_KEY";
    public static final String SAVED_BEACON_KEY = "se.grouprich.closebeacon.SAVED_BEACON_KEY";

    private TextView textViewMacAddress;
    private TextView textViewName;
    private TextView textViewRSSI;
    private TextView textViewUuid;
    private TextView textViewSerialNumber;
    private TextView textViewProximityUuid;
    private Button buttonGenerate;
    private Button buttonSelect;
    private Button buttonActivate;
    private NoUuidDialog noUuidDialog;
    private UuidGeneratedDialog uuidGeneratedDialog;
    private InvalidMajorMinorDialog invalidMajorMinorDialog;
    private EditText editTextMajor;
    private EditText editTextMinor;
    private Context context = this;
    private ArrayList<String> uuidStringList;
    private Set<String> uuidStringSet;
    private String majorNumber;
    private String minorNumber;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        textViewMacAddress = (TextView) findViewById(R.id.device_mac_address_details);
        textViewName = (TextView) findViewById(R.id.device_name_details);
        textViewRSSI = (TextView) findViewById(R.id.device_rssi_details);
        textViewUuid = (TextView) findViewById(R.id.device_uuid_detail);
        textViewSerialNumber = (TextView) findViewById(R.id.device_serialnumber_details);
        textViewProximityUuid = (TextView) findViewById(R.id.text_proximity_uuid);

        buttonGenerate = (Button) findViewById(R.id.button_generate);
        buttonSelect = (Button) findViewById(R.id.button_select);
        buttonActivate = (Button) findViewById(R.id.button_activate);

        editTextMajor = (EditText) findViewById(R.id.edit_major);
        editTextMinor = (EditText) findViewById(R.id.edit_minor);

        noUuidDialog = new NoUuidDialog(this);
        uuidGeneratedDialog = new UuidGeneratedDialog(this);
        invalidMajorMinorDialog = new InvalidMajorMinorDialog(this);

        uuidStringList = new ArrayList<>();
        uuidStringSet = new LinkedHashSet<>();
        preferences = getSharedPreferences(SAVED_BEACON_KEY, 0);

        Bundle bundle = getIntent().getExtras();

        String macAddress;
        String name;
        String rssi;
        String serviceUuid;
        String serialNumber;
        String proximityUuid;

        if (bundle.getString("macAddress") != null) {

            macAddress = bundle.getString("macAddress");
            name = bundle.getString("name");
            rssi = bundle.getString("rssi");
            serviceUuid = bundle.getString("serviceUuid");
            serialNumber = bundle.getString("serialNumber");
            proximityUuid = bundle.getString("proximityUuid");

            preferences.edit()
                    .putString("macAddress", macAddress)
                    .putString("name", name)
                    .putString("rssi", rssi)
                    .putString("serviceUuid", serviceUuid)
                    .putString("serialNumber", serialNumber)
                    .putString("proximityUuid", proximityUuid)
                    .apply();
        } else {

            macAddress = preferences.getString("macAddress", "");
            name = preferences.getString("name", "");
            rssi = preferences.getString("rssi", "");
            serviceUuid = preferences.getString("serviceUuid", "");
            serialNumber = preferences.getString("serialNumber", "");
            proximityUuid = preferences.getString("proximityUuid", "");
            majorNumber = preferences.getString("majorNumber", "");
            minorNumber = preferences.getString("minorNumber", "");
        }

        textViewMacAddress.setText(macAddress);
        textViewName.setText(name);
        textViewRSSI.setText(rssi);
        textViewUuid.setText(serviceUuid);
        textViewSerialNumber.setText(serialNumber);
        textViewProximityUuid.setText(proximityUuid);

        editTextMajor.setText(majorNumber);
        editTextMinor.setText(minorNumber);

        String selectedUuidString = getIntent().getStringExtra(UuidSelectionActivity.SELECTED_UUID_KEY);

        if (selectedUuidString != null) {

            proximityUuid = selectedUuidString;
            textViewProximityUuid.setText(selectedUuidString);
        }

        Set<String> savedUuidStringSet = preferences.getStringSet("uuidStringSet", null);

        if (savedUuidStringSet != null) {

            uuidStringSet.addAll(savedUuidStringSet);
        }

        uuidStringSet.add(proximityUuid);
        uuidStringList.addAll(uuidStringSet);

        buttonGenerate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String uuidString = UUID.randomUUID().toString();
                uuidStringList.add(uuidString);
                uuidStringSet.addAll(uuidStringList);
                uuidGeneratedDialog.show();
            }
        });

        buttonSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (uuidStringList.isEmpty()) {

                    noUuidDialog.show();

                } else {

                    majorNumber = editTextMajor.getText().toString();
                    minorNumber = editTextMinor.getText().toString();

                    preferences.edit()
                            .putString("majorNumber", majorNumber)
                            .putString("minorNumber", minorNumber)
                            .putStringSet("uuidStringSet", DeviceDetailsActivity.this.uuidStringSet)
                            .apply();

                    Intent intent = new Intent(context, UuidSelectionActivity.class);

                    intent.putStringArrayListExtra(UUID_STRING_LIST_KEY, uuidStringList);
                    startActivity(intent);
                }
            }
        });

        buttonActivate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                majorNumber = editTextMajor.getText().toString();
                minorNumber = editTextMinor.getText().toString();

                if ((majorNumber.length() != 2) || (minorNumber.length() != 2)) {

                    invalidMajorMinorDialog.show();
                }
//                Beacon beacon = new Beacon();
//                Intent intent = new Intent(context, ActivationActivity.class);

                // skicka activate request och får svar från servern.
                // gör "write" activation command till den valda beacon.
            }
        });
    }
}
