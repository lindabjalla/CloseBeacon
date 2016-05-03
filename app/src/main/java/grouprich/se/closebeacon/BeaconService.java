package grouprich.se.closebeacon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BeaconService {

    @GET("{php}")
    Call<String> getPublicKey(@Path("php") String php);
}
