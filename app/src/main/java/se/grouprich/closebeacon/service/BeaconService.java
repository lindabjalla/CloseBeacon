package se.grouprich.closebeacon.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BeaconService {

    @GET("getpubkey.php")
    Call<String> getPublicKey();

    @GET("auth_user.php")
    Call<String> getAuthorizationResponse(@Query("enc") String authorizationRequest);
}
