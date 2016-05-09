package se.grouprich.closebeacon;

import retrofit2.Retrofit;

public final class RetrofitBuilder {

    public static final String BASE_URL = "http://smartsensor.io/CBtest/";

    private Retrofit retrofit;
    private BeaconService beaconService;

    public RetrofitBuilder() {

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
