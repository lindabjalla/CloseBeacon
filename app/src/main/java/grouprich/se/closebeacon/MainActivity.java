package grouprich.se.closebeacon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://smartsensor.io/CBtest/";
    public static final String TAG = MainActivity.class.getSimpleName();

    private Retrofit retrofit;
    private String publicKey;

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

                publicKey = response.body().replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Could not fetch publicKey: " + t.getMessage());
            }
        });
    }
}
