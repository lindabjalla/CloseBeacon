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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.dialog.NoUuidDialog;
import se.grouprich.closebeacon.dialog.UuidGeneratedDialog;

public class DeviceDetailsActivity extends AppCompatActivity {

    public static final String UUID_STRING_LIST_KEY = "se.grouprich.closebeacon.UUID_LIST_KEY";
    public static final String SAVED_BEACON_KEY = "se.grouprich.closebeacon.SAVED_BEACON_KEY";

    private TextView textViewAddress;
    private TextView textViewName;
    private TextView textViewRSSI;
    private TextView textViewUuid;
    private TextView textViewSerialNumber;
    private TextView textViewProximityUuid;
    private Button buttonGenerate;
    private Button buttonSelect;
    private NoUuidDialog noUuidDialog;
    private UuidGeneratedDialog uuidGeneratedDialog;
    private EditText editTextMajor;
    private EditText editTextMinor;
    private Context context = this;
    private ArrayList<String> uuidStringList;
    private Set<String> uuidStringSet;
    private String major;
    private String minor;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        textViewAddress = (TextView) findViewById(R.id.device_address_details);
        textViewName = (TextView) findViewById(R.id.device_name_details);
        textViewRSSI = (TextView) findViewById(R.id.device_rssi_details);
        textViewUuid = (TextView) findViewById(R.id.device_uuid_detail);
        textViewSerialNumber = (TextView) findViewById(R.id.device_serialnumber_details);
        textViewProximityUuid = (TextView) findViewById(R.id.text_proximity_uuid);
        buttonGenerate = (Button) findViewById(R.id.button_generate);
        buttonSelect = (Button) findViewById(R.id.button_select);
        editTextMajor = (EditText) findViewById(R.id.edit_major);
        editTextMinor = (EditText) findViewById(R.id.edit_minor);
        noUuidDialog = new NoUuidDialog(this);
        uuidGeneratedDialog = new UuidGeneratedDialog(this);
        uuidStringList = new ArrayList<>();
        uuidStringSet = new LinkedHashSet<>();
        preferences = getSharedPreferences(SAVED_BEACON_KEY, 0);

        Bundle bundle = getIntent().getExtras();

        String address;
        String name;
        String rssi;
        String uuid;
        String serialNumber;

        if (bundle.getString("address") != null) {

            address = bundle.getString("address");
            name = bundle.getString("name");
            rssi = bundle.getString("rssi");
            uuid = bundle.getString("uuid");
            serialNumber = bundle.getString("serialNumber");

            preferences.edit()
                    .putString("address", address)
                    .putString("name", name)
                    .putString("rssi", rssi)
                    .putString("uuid", uuid)
                    .putString("serialNumber", serialNumber)
                    .apply();
        } else {

            address = preferences.getString("address", "");
            name = preferences.getString("name", "");
            rssi = preferences.getString("rssi", "");
            uuid = preferences.getString("uuid", "");
            serialNumber = preferences.getString("serialNumber", "");
            major = preferences.getString("major", "");
            minor = preferences.getString("minor", "");
        }

        textViewAddress.setText(address);
        textViewName.setText(name);
        textViewRSSI.setText(rssi);
        textViewUuid.setText(uuid);
        textViewSerialNumber.setText(serialNumber);
        editTextMajor.setText(major);
        editTextMinor.setText(minor);

        String selectedUuidString = getIntent().getStringExtra(SelectUuidActivity.SELECTED_UUID_KEY);
        textViewProximityUuid.setText(selectedUuidString);

        uuidStringSet = preferences.getStringSet("uuidStringSet", null);
        uuidStringList.addAll(uuidStringSet);

        buttonGenerate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String uuidString = UUID.randomUUID().toString();
                uuidStringList.add(uuidString);
                uuidStringSet.addAll(uuidStringList);
//                Toast.makeText(context, R.string.dialog_uuid_generated, Toast.LENGTH_SHORT).show();
                uuidGeneratedDialog.show();
            }
        });

        buttonSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (uuidStringList.isEmpty()) {

                    noUuidDialog.show();

                } else {

                    major = editTextMajor.getText().toString();
                    minor = editTextMinor.getText().toString();

                    if (!major.isEmpty() || !minor.isEmpty()) {

                        preferences.edit()
                                .putString("major", major)
                                .putString("minor", minor).apply();
                    }

                    preferences.edit()
                            .putStringSet("uuidStringSet", DeviceDetailsActivity.this.uuidStringSet)
                            .apply();

                    Intent intent = new Intent(context, SelectUuidActivity.class);
                    intent.putStringArrayListExtra(UUID_STRING_LIST_KEY, uuidStringList);
                    startActivity(intent);
                }
            }
        });
    }
}
