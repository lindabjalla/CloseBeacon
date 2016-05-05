package se.grouprich.closebeacon.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import se.grouprich.closebeacon.BeaconConfiguration;
import se.grouprich.closebeacon.NoUuidDialog;
import se.grouprich.closebeacon.R;

public class ActivationActivity extends AppCompatActivity {

    public static final String UUID_LIST_KEY = "se.grouprich.closebeacon.UUID_LIST_KEY";

    private Context context = this;
    private List<String> uuidList = new ArrayList<>();
    private BeaconConfiguration beaconConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        Intent intent = getIntent();
        String selectedUuid = intent.getStringExtra("SELECTED_UUID_KEY");

        if (selectedUuid != null) {

            TextView textView = (TextView) findViewById(R.id.proximity_uuid_text);

            if (textView != null) {

                textView.setText(selectedUuid);
            }
        }

        Button generateUuidButton = (Button) findViewById(R.id.generate_uuid_button);
        Button selectUuidButton = (Button) findViewById(R.id.select_uuid_button);
        Button activateButton = (Button) findViewById(R.id.activate_button);

        if (generateUuidButton != null) {

            generateUuidButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    String uuid = UUID.randomUUID().toString();
                    uuidList.add(uuid);
                }
            });
        }

        if (selectUuidButton != null) {

            selectUuidButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (uuidList.isEmpty()) {

                        NoUuidDialog noUuidDialog = new NoUuidDialog(context);
                        noUuidDialog.show();

                    } else {

                        Intent intent = new Intent(context, ShowUuidActivity.class);
                        intent.putStringArrayListExtra(UUID_LIST_KEY, (ArrayList<String>) uuidList);
                        startActivity(new Intent(context, ShowUuidActivity.class));
                    }
                }
            });
        }

        if (activateButton != null) {

            activateButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    String uuid = null;
                    String major = null;
                    String minor = null;

                    TextView uuidTextView = (TextView) findViewById(R.id.proximity_uuid_text);
                    TextView majorTextView = (TextView) findViewById(R.id.major_input);
                    TextView minorTextView = (TextView) findViewById(R.id.minor_input);

                    if (uuidTextView != null) {

                        uuid = uuidTextView.getText().toString();
                    }
                    if (majorTextView != null) {

                        major = majorTextView.getText().toString();
                    }
                    if (minorTextView != null) {

                        minor = minorTextView.getText().toString();
                    }

                    beaconConfiguration = new BeaconConfiguration(uuid, major, minor);
                }
            });
        }
    }
}
