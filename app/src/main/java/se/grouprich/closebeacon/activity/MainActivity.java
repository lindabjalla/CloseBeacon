package se.grouprich.closebeacon.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.security.PublicKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.grouprich.closebeacon.service.BeaconService;
import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.requestresponsemanager.converter.KeyConverter;
import se.grouprich.closebeacon.retrofit.StringConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://smartsensor.io/CBtest/";
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String BEACON_PREFERENCES = "se.grouprich.closebeacon.BEACON_PREFERENCES";
    public static final String APP_IS_ACTIVATED_KEY = "se.grouprich.closebeacon.APP_IS_ACTIVATED_KEY";
    public static final String PUBLIC_KEY_AS_STRING_KEY = "se.grouprich.closebeacon.PUBLIC_KEY_AS_STRING_KEY";

    private String publicKeyAsString;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences preferences = getSharedPreferences(BEACON_PREFERENCES, 0);

        if (!preferences.getBoolean(APP_IS_ACTIVATED_KEY, false)) {

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(new StringConverterFactory())
                    .build();

            final BeaconService beaconService = retrofit.create(BeaconService.class);
            final Call<String> result = beaconService.getPublicKey();

            result.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "***************** " + response.body());

                    publicKeyAsString = response.body().replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");

                    preferences.edit().putString(PUBLIC_KEY_AS_STRING_KEY, publicKeyAsString).apply();
                    Intent intent = new Intent(context, AuthorizationActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Log.d(TAG, "Could not fetch publicKey: " + t.getMessage());
                }
            });

        } else {

            final Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent);
        }
    }
}
