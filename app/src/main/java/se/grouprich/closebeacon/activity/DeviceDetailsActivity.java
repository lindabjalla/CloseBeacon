package se.grouprich.closebeacon.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.dialog.ActivationFailedDialog;
import se.grouprich.closebeacon.dialog.InvalidMajorMinorDialog;
import se.grouprich.closebeacon.model.BeaconActivationCommand;
import se.grouprich.closebeacon.model.BeaconActivationRequest;
import se.grouprich.closebeacon.requestresponsemanager.RequestBuilder;
import se.grouprich.closebeacon.requestresponsemanager.converter.KeyConverter;
import se.grouprich.closebeacon.requestresponsemanager.converter.SHA1Converter;
import se.grouprich.closebeacon.retrofit.RetrofitManager;

public class DeviceDetailsActivity extends AppCompatActivity {

    public static final String UUID_STRING_LIST_KEY = "se.grouprich.closebeacon.UUID_LIST_KEY";
    public static final String ACTIVATION_COMMAND_KEY = "se.grouprich.closebeacon.ACTIVATION_COMMAND";
    public static final String MAC_ADDRESS_KEY = "se.grouprich.closebeacon.MAC_ADDRESS_KEY";
    public static final String PROXIMITY_UUID_KEY = "se.grouprich.closebeacon.PROXIMITY_UUID_KEY";
    public static final String MAJOR_KEY = "se.grouprich.closebeacon.MAJOR_KEY";
    public static final String MINOR_KEY = "se.grouprich.closebeacon.MINOR_KEY";

    private InvalidMajorMinorDialog invalidMajorMinorDialog;
    private EditText editTextMajor;
    private EditText editTextMinor;
    private Context context = this;
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

        final TextView textViewMacAddress = (TextView) findViewById(R.id.device_mac_address_details);
        final TextView textViewName = (TextView) findViewById(R.id.device_name_details);
        final TextView textViewRSSI = (TextView) findViewById(R.id.device_rssi_details);
        final TextView textViewUuid = (TextView) findViewById(R.id.device_uuid_detail);
        final TextView textViewSerialNumber = (TextView) findViewById(R.id.device_serialnumber_details);
        final TextView textViewProximityUuid = (TextView) findViewById(R.id.text_proximity_uuid);
        final Button buttonActivate = (Button) findViewById(R.id.button_activate);

        editTextMajor = (EditText) findViewById(R.id.edit_major);
        editTextMinor = (EditText) findViewById(R.id.edit_minor);
        invalidMajorMinorDialog = new InvalidMajorMinorDialog(this);

        final Bundle bundle = getIntent().getExtras();

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

            if (textViewMacAddress != null) {

                textViewMacAddress.setText(macAddress);
            }
            if (textViewName != null) {

                textViewName.setText(name);
            }
            if (textViewRSSI != null) {

                textViewRSSI.setText(rssi);
            }
            if (textViewUuid != null) {

                textViewUuid.setText(serviceUuid);
            }
            if (textViewSerialNumber != null) {

                textViewSerialNumber.setText(serialNumber);
            }
            if (textViewProximityUuid != null) {

                textViewProximityUuid.setText(proximityUuid);
            }

            if (buttonActivate != null) {
                buttonActivate.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        majorNumber = editTextMajor.getText().toString();
                        minorNumber = editTextMinor.getText().toString();

                        if ((majorNumber.length() != 2) || (minorNumber.length() != 2)) {

                            invalidMajorMinorDialog.show();
                        }

                        preferences = getSharedPreferences(MainActivity.BEACON_PREFERENCES_KEY, MODE_PRIVATE);
                        final String authCode = preferences.getString(AuthorizationActivity.AUTH_CODE_KEY, null);

                        beaconActivationRequest = new BeaconActivationRequest(authCode, macAddress, majorNumber, minorNumber, proximityUuid);
                        final byte[] activationRequestAsByteArray = beaconActivationRequest.buildByteArray();

                        Log.d("activationRequest", Arrays.toString(activationRequestAsByteArray));

                        final String publicKeyAsString = preferences.getString(MainActivity.PUBLIC_KEY_AS_STRING_KEY, null);
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

                        final RetrofitManager retrofitManager = new RetrofitManager();

                        final Call<String> result = retrofitManager.getBeaconService().getActivationResponse(activationRequest);

                        result.enqueue(new Callback<String>() {

                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.d("response", response.body());

                                final String responseBody = response.body();

                                if (responseBody.contains("OK")) {

                                    String sha1Hex = responseBody.replace("OK ", "");
                                    final byte[] sha1 = SHA1Converter.hexStringToByteArray(sha1Hex);

                                    Log.d("SHA1", Arrays.toString(sha1));

                                    final BeaconActivationCommand beaconActivationCommand = new BeaconActivationCommand(beaconActivationRequest.getAdminKey(), beaconActivationRequest.getMobileKey(), sha1, proximityUuid, majorNumber, minorNumber);
                                    final byte[] beaconActivationCommandAsByteArray = beaconActivationCommand.buildByteArray();

                                    preferences.edit().putString(ACTIVATION_COMMAND_KEY, Base64.encodeToString(beaconActivationCommandAsByteArray, Base64.DEFAULT)).apply();
                                    preferences.edit().putString(PROXIMITY_UUID_KEY, proximityUuid).apply();
                                    preferences.edit().putString(MAJOR_KEY, Base64.encodeToString(beaconActivationCommand.getMajorNumber(), Base64.DEFAULT)).apply();
                                    preferences.edit().putString(MINOR_KEY, Base64.encodeToString(beaconActivationCommand.getMinorNumber(), Base64.DEFAULT)).apply();

                                    final Intent intent = new Intent(context, DeviceControlActivity.class);
                                    intent.putExtra(ACTIVATION_COMMAND_KEY, beaconActivationCommandAsByteArray);
                                    intent.putExtra(MAC_ADDRESS_KEY, macAddress);
                                    startActivity(intent);

                                } else {

                                    final ActivationFailedDialog activationFailedDialog = new ActivationFailedDialog(context);
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
    }
}
