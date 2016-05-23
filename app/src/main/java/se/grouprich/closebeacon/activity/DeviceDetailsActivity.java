package se.grouprich.closebeacon.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.dialog.ActivationFailedDialog;
import se.grouprich.closebeacon.dialog.InvalidMajorMinorDialog;
import se.grouprich.closebeacon.dialog.NoUuidDialog;
import se.grouprich.closebeacon.dialog.UuidGeneratedDialog;
import se.grouprich.closebeacon.model.BeaconActivationCommand;
import se.grouprich.closebeacon.model.BeaconActivationRequest;
import se.grouprich.closebeacon.requestresponsemanager.RequestBuilder;
import se.grouprich.closebeacon.requestresponsemanager.converter.KeyConverter;
import se.grouprich.closebeacon.requestresponsemanager.converter.SHA1Converter;
import se.grouprich.closebeacon.retrofit.RetrofitManager;

public class DeviceDetailsActivity extends AppCompatActivity {

    public static final String UUID_STRING_LIST_KEY = "se.grouprich.closebeacon.UUID_LIST_KEY";
    public static final String SAVED_BEACON_KEY = "se.grouprich.closebeacon.SAVED_BEACON_KEY";
    public static final String ACTIVATION_COMMAND_KEY = "se.grouprich.closebeacon.ACTIVATION_COMMAND";
    public static final String MAC_ADDRESS_KEY = "se.grouprich.closebeacon.MAC_ADDRESS_KEY";
    public static final String PROXIMITY_UUID_KEY = "se.grouprich.closebeacon.PROXIMITY_UUID_KEY";
    public static final String MAJOR_KEY = "se.grouprich.closebeacon.MAJOR_KEY";
    public static final String MINOR_KEY = "se.grouprich.closebeacon.MINOR_KEY";

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
    private String macAddress;
    private String majorNumber;
    private String minorNumber;
    private String proximityUuid;
    private BeaconActivationRequest beaconActivationRequest;

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

//        final String macAddress;
        String name;
        String rssi;
        String serviceUuid;
        String serialNumber;

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

                // skicka activate request och får svar från servern.

                preferences = getSharedPreferences(MainActivity.BEACON_PREFERENCES, 0);
                String authCode = preferences.getString(AuthorizationActivity.AUTH_CODE_KEY, null);

                beaconActivationRequest = new BeaconActivationRequest(authCode, macAddress, majorNumber, minorNumber, proximityUuid);
                final byte[] activationRequestAsByteArray = beaconActivationRequest.buildByteArray();

                // ------ Loggar activationRequest byteArray value ------
                List<String> activationRequestBytes = new ArrayList<>();
                for (byte aByte : activationRequestAsByteArray) {
                    activationRequestBytes.add(String.valueOf(aByte));
                }

                Log.d("activationRequest", activationRequestBytes.toString());
                // ------ Log ends ------

                String publicKeyAsString = preferences.getString(MainActivity.PUBLIC_KEY_AS_STRING_KEY, null);
                PublicKey publicKey = null;

                try {
                    publicKey = KeyConverter.stringToPublicKey(publicKeyAsString);

                } catch (GeneralSecurityException e) {

                    e.printStackTrace();
                }

                String activationRequest = null;

                try {
                     activationRequest = RequestBuilder.buildRequest(publicKey, activationRequestAsByteArray);

                } catch (Exception e) {

                    e.printStackTrace();
                }
                RetrofitManager retrofitManager = new RetrofitManager();

                Call<String> result = retrofitManager.getBeaconService().getActivationResponse(activationRequest);

                result.enqueue(new Callback<String>() {

                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("response", response.body());

                        String responseBody = response.body();

                        if(responseBody.contains("OK")){

                            String sha1Hex = responseBody.replace("OK ", "");
                            final byte[] sha1 = SHA1Converter.hexStringToByteArray(sha1Hex);

                            //----- Loggar SHA1 value -----
                            List<String> sha1List = new ArrayList<>();

                            for (byte sha1Byte : sha1) {

                                sha1List.add(String.valueOf(sha1Byte));
                            }

                            Log.d("SHA1", sha1List.toString());
                            //----- Log ends-----

                            BeaconActivationCommand beaconActivationCommand = new BeaconActivationCommand(beaconActivationRequest.getAdminKey(), beaconActivationRequest.getMobileKey(), sha1, proximityUuid, majorNumber, minorNumber);
                            final byte[] beaconActivationCommandAsByteArray = beaconActivationCommand.buildByteArray();

                            preferences.edit().putString(ACTIVATION_COMMAND_KEY, Base64.encodeToString(beaconActivationCommandAsByteArray, Base64.DEFAULT)).apply();
                            preferences.edit().putString(PROXIMITY_UUID_KEY, proximityUuid).apply();
                            preferences.edit().putString(MAJOR_KEY, Base64.encodeToString(beaconActivationCommand.getMajorNumber(), Base64.DEFAULT)).apply();
                            preferences.edit().putString(MINOR_KEY, Base64.encodeToString(beaconActivationCommand.getMinorNumber(), Base64.DEFAULT)).apply();

                            Intent intent = new Intent(context, DeviceControlActivity.class);
                            intent.putExtra(ACTIVATION_COMMAND_KEY, beaconActivationCommandAsByteArray);
                            intent.putExtra(MAC_ADDRESS_KEY, macAddress);
                            startActivity(intent);

                            } else {

                            ActivationFailedDialog activationFailedDialog = new ActivationFailedDialog(context);
                            activationFailedDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("failure", "failed");
                    }
                });
            }
        });
    }
}
