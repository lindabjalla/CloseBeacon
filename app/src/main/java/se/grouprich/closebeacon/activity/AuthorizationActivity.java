package se.grouprich.closebeacon.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.PublicKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.RequestBuilder;
import se.grouprich.closebeacon.RetrofitBuilder;
import se.grouprich.closebeacon.dialog.InvalidAuthCodeDialog;

public class AuthorizationActivity extends AppCompatActivity {

    public static final String TAG = AuthorizationActivity.class.getSimpleName();

    private Button buttonAuth;
    private TextView textAuthCode;
    private String authorizationCode;
    private String authorizationRequest;
    private String response;
    private PublicKey publicKey;
    private Context context = this;
    private InvalidAuthCodeDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        Intent intent = getIntent();
        publicKey = (PublicKey) intent.getSerializableExtra(MainActivity.PUBLIC_KEY_KEY);

        if (publicKey != null) {

            buttonAuth = (Button) findViewById(R.id.button_auth);
            textAuthCode = (EditText) findViewById(R.id.text_authCode);
            dialog = new InvalidAuthCodeDialog(context);

            buttonAuth.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    authorizationCode = textAuthCode.getText().toString();

                    if (authorizationCode.length() != 12) {

                        dialog.show();
//                        textAuthCode.setText("Your field is empty");

                    } else {

//                        RSAEncrypt encryptRequest = new RSAEncrypt(publicKey);

                        // authorizationRequest.equals(encryptRequest.encrypt(,encryptRequest.encodeBase64Authcode(authorizationCode)));

                        RequestBuilder requestBuilder = new RequestBuilder();
                        final byte[] requestBytes = requestBuilder.buildAuthorizationRequest(authorizationCode);

                        byte[] encryptedRequestBytes = new byte[0];
                        try {

                            encryptedRequestBytes = requestBuilder.encryptRequest(publicKey, requestBytes);

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                        final String authorizationRequest = requestBuilder.encodeRequest(encryptedRequestBytes);

                        System.out.println(authorizationRequest);

                        RetrofitBuilder retrofitBuilder = new RetrofitBuilder();
                        Call<String> result = retrofitBuilder.getBeaconService().getAuthorizationResponse(authorizationRequest);

                        result.enqueue(new Callback<String>() {

                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                                Log.d(TAG, "***************** " + response.body());

                                if (response.equals("OK")) {

                                    SharedPreferences preferences = getSharedPreferences(MainActivity.APP_ACTIVATION_CHECK_KEY, 0);
                                    preferences.edit().putBoolean(MainActivity.APP_IS_ACTIVATED_KEY, true).apply();

                                    //TODO: avkommentera när ScanActivity har lagt till
//                                    Intent intent = new Intent(this, ScanActivity.class);
//                                    startActivity(intent);

                                } else {

                                    dialog.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                                Log.d(TAG, "Could not fetch response" + t.getMessage());
                            }
                        });
                    }
                }
            });
        }
    }
}
