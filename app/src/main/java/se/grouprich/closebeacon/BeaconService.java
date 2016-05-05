package se.grouprich.closebeacon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BeaconService {

    @GET("{publicKey}")
    Call<String> getPublicKey(@Path("publicKey") String publicKey);
}
