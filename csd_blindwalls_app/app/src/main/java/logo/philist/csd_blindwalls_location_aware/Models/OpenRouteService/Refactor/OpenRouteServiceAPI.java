package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Refactor;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpenRouteServiceAPI {

    @GET("/v2/directions/{profile}")
    Call<SimpleDirectionsResponseBody> getSimpleDirections(@Path("profile") String profile, @Query("api_key") String apiKey, @Query("start") String start, @Query("end") String end);

    @POST("/v2/directions/{profile}")
    Call<ComplexDirectionsResponseBody> getComplexDirections(@Path("profile") String profile, @Header("Authorization") String apiKey, @Body DirectionsRequestBody body);

}
