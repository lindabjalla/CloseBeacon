package se.grouprich.closebeacon.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.security.GeneralSecurityException;
import java.security.PublicKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.grouprich.closebeacon.BeaconService;
import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.requestresponsemanager.converter.KeyConverter;
import se.grouprich.closebeacon.retrofit.StringConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://smartsensor.io/CBtest/";
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String APP_ACTIVATION_CHECK_KEY = "appActivationCheck";
    public static final String APP_IS_ACTIVATED_KEY = "appIsActivated";
    public static final String PUBLIC_KEY_KEY = "publicKey";

    private PublicKey publicKey;
    private boolean appIsActivated;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences(APP_ACTIVATION_CHECK_KEY, 0);

        preferences.edit().putBoolean(APP_IS_ACTIVATED_KEY, false).apply(); // bara för test

        System.out.println(preferences.getBoolean(APP_IS_ACTIVATED_KEY, false));
        if (!preferences.getBoolean(APP_IS_ACTIVATED_KEY, false)) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(new StringConverterFactory())
                    .build();

            BeaconService beaconService = retrofit.create(BeaconService.class);
            Call<String> result = beaconService.getPublicKey();

            result.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "***************** " + response.body());

                    String keyString = response.body().replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
                    System.out.println(keyString);

                    try {

                        publicKey = KeyConverter.stringToPublicKey(keyString);
                        System.out.println(publicKey.toString());

                    } catch (GeneralSecurityException e) {

                        e.printStackTrace();

                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    Intent intent = new Intent(context, AuthorizationActivity.class);
                    intent.putExtra(PUBLIC_KEY_KEY, publicKey);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "Could not fetch publicKey: " + t.getMessage());
                }
            });

        } else {

            // TODO: Avkommenteras när ScanActivity läggs till
//            Intent intent = new Intent(this, ScanActivity.class);
//            startActivity(intent);
        }
    }
}
