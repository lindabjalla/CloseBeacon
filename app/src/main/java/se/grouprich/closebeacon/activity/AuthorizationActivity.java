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

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.dialog.InvalidAuthCodeDialog;
import se.grouprich.closebeacon.requestresponsemanager.RequestBuilder;
import se.grouprich.closebeacon.requestresponsemanager.bytearraybuilder.AuthorizationByteArrayBuilder;
import se.grouprich.closebeacon.requestresponsemanager.converter.KeyConverter;
import se.grouprich.closebeacon.requestresponsemanager.converter.SHA1Converter;
import se.grouprich.closebeacon.retrofit.RetrofitManager;

public final class AuthorizationActivity extends AppCompatActivity {

    public static final String TAG = AuthorizationActivity.class.getSimpleName();
    public static final String AUTH_CODE_KEY = "se.grouprich.closebeacon.AUTH_CODE_KEY";
    private TextView textAuthCode;
    private String authenticationCode;
    private PublicKey publicKey;
    private Context context = this;
    private InvalidAuthCodeDialog dialog;
    private String responseOk;
    private String responseUnknown;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        preferences = getSharedPreferences(MainActivity.BEACON_PREFERENCES, MODE_PRIVATE);
        final String publicKeyAsString = preferences.getString(MainActivity.PUBLIC_KEY_AS_STRING_KEY, null);

        try {

            publicKey = KeyConverter.stringToPublicKey(publicKeyAsString);

        } catch (GeneralSecurityException e) {

            e.printStackTrace();
        }

        if (publicKey != null) {

            final Button buttonAuth = (Button) findViewById(R.id.button_authorize);
            textAuthCode = (EditText) findViewById(R.id.editText_auth_code);
            dialog = new InvalidAuthCodeDialog(context);

            if (buttonAuth != null) {

                buttonAuth.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        authenticationCode = textAuthCode.getText().toString();

                        if (authenticationCode.length() != 12) {

                            dialog.show();

                        } else {

                            final AuthorizationByteArrayBuilder requestBuilder = new AuthorizationByteArrayBuilder();
                            final byte[] authRequestByteArray = requestBuilder.buildAuthRequestByteArray(authenticationCode);
                            final byte[] authCodePlusOkByteArray = requestBuilder.buildResponseOkByteArray(authRequestByteArray);
                            final byte[] authCodePlusUnknownByteArray = requestBuilder.buildResponseUnknownByteArray(authRequestByteArray);

                            Log.d("authReq", Arrays.toString(authRequestByteArray));

                            try {
                                responseOk = SHA1Converter.byteArrayToSHA1(authCodePlusOkByteArray);
                                responseUnknown = SHA1Converter.byteArrayToSHA1(authCodePlusUnknownByteArray);

                            } catch (NoSuchAlgorithmException e) {

                                e.printStackTrace();
                            }

                            Log.d("authCodePlusOk", responseOk);
                            Log.d("authCodePlusUnknown", responseUnknown);

                            String authorizationRequest = null;
                            try {
                                authorizationRequest = RequestBuilder.buildRequest(publicKey, authRequestByteArray);

                            } catch (Exception e) {

                                e.printStackTrace();
                            }

                            Log.d("authReq", authorizationRequest);

                            final RetrofitManager retrofitManager = new RetrofitManager();
                            final Call<String> result = retrofitManager.getBeaconService().getAuthorizationResponse(authorizationRequest);

                            result.enqueue(new Callback<String>() {

                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {

                                    Log.d("url", call.request().url().toString());
                                    Log.d(TAG, "***************** " + response.body());
                                    Log.d("responseOk", responseOk);

                                    if (response.body().replaceFirst("=", "").equals(responseOk)) {

                                        preferences.edit().putBoolean(MainActivity.APP_IS_ACTIVATED_KEY, true)
                                                .putString(AUTH_CODE_KEY, authenticationCode)
                                                .apply();

                                        final Intent intent = new Intent(context, ScanActivity.class);
                                        startActivity(intent);

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
}
