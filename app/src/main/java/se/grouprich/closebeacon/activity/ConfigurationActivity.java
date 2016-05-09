package se.grouprich.closebeacon.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.dialog.NoUuidDialog;
import se.grouprich.closebeacon.model.BeaconConfiguration;

public class ConfigurationActivity extends AppCompatActivity {

    public static final String UUID_STRING_LIST_KEY = "se.grouprich.closebeacon.UUID_LIST_KEY";

    private String serialNumber;
    private Context context = this;
    private List<UUID> uuidList = new ArrayList<>();
    private ArrayList<String> uuidStringList = new ArrayList<>();
    private BeaconConfiguration beaconConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        Intent intent = getIntent();
        String selectedUuid = intent.getStringExtra("SELECTED_UUID_KEY");

        if (selectedUuid != null) {

            EditText editText = (EditText) findViewById(R.id.proximity_uuid_text);

            if (editText != null) {

                editText.setText(selectedUuid);
            }
        }

        TextView serialNumberText = (TextView) findViewById(R.id.serial_number_text);

        if (serialNumberText != null) {

            serialNumberText.setText(serialNumber);
        }

        Button generateUuidButton = (Button) findViewById(R.id.generate_uuid_button);
        Button selectUuidButton = (Button) findViewById(R.id.select_uuid_button);
        Button activateButton = (Button) findViewById(R.id.activate_button);

        if (generateUuidButton != null) {

            generateUuidButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    UUID uuid = UUID.randomUUID();
                    uuidList.add(uuid);
                    uuidStringList.add(uuid.toString());
                }
            });
        }

        if (selectUuidButton != null) {

            selectUuidButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (uuidList.isEmpty()) {

                        NoUuidDialog dialog = new NoUuidDialog(context);
                        dialog.show();

                    } else {

                        Intent intent = new Intent(context, ShowUuidActivity.class);
                        intent.putStringArrayListExtra(UUID_STRING_LIST_KEY, uuidStringList);
                        startActivity(intent);
                    }
                }
            });
        }

        if (activateButton != null) {

            activateButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    UUID uuid = null;
                    String uuidString = null;
                    String major = null;
                    String minor = null;

                    EditText uuidText = (EditText) findViewById(R.id.proximity_uuid_text);
                    EditText majorText = (EditText) findViewById(R.id.major_input);
                    EditText minorText = (EditText) findViewById(R.id.minor_input);

                    if (uuidText != null) {

                        uuidString = uuidText.getText().toString();
                    }
                    if (majorText != null) {

                        major = majorText.getText().toString();
                    }
                    if (minorText != null) {

                        minor = minorText.getText().toString();
                    }

                    for (UUID aUuid : uuidList) {

                        if (aUuid.toString().equals(uuidString)) {

                            uuid = aUuid;
                        }
                    }

                    beaconConfiguration = new BeaconConfiguration(serialNumber, uuid, major, minor);
                }
            });
        }
    }
}
