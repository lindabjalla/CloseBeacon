package grouprich.se.closebeacon.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.security.GeneralSecurityException;
import java.security.PublicKey;

import grouprich.se.closebeacon.BeaconService;
import grouprich.se.closebeacon.KeyConverter;
import grouprich.se.closebeacon.R;
import grouprich.se.closebeacon.StringConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://smartsensor.io/CBtest/";
    public static final String TAG = MainActivity.class.getSimpleName();

    private Retrofit retrofit;
    private PublicKey publicKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(new StringConverterFactory())
                .build();

        BeaconService beaconService = retrofit.create(BeaconService.class);
        Call<String> result = beaconService.getPublicKey("getpubkey.php");

        result.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "***************** " + response.body());

                String keyInString = response.body().replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
                try {
                    publicKey = KeyConverter.stringToPublicKey(keyInString);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Could not fetch publicKey: " + t.getMessage());
            }
        });
    }
}
