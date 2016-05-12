package se.grouprich.closebeacon.retrofit;

import retrofit2.Retrofit;
import se.grouprich.closebeacon.BeaconService;

public final class RetrofitManager {

    public static final String BASE_URL = "http://smartsensor.io/CBtest/";

    private Retrofit retrofit;
    private BeaconService beaconService;

    public RetrofitManager() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(new StringConverterFactory())
                .build();

        beaconService = retrofit.create(BeaconService.class);
    }

    public Retrofit getRetrofit() {

        return retrofit;
    }

    public BeaconService getBeaconService() {

        return beaconService;
    }
}
