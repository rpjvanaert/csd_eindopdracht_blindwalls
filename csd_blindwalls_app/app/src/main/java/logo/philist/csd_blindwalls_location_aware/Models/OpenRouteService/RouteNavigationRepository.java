package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService;

import android.app.DownloadManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RouteNavigationRepository extends MuralNavigationRepository{

    private static RouteNavigationRepository instance;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private RouteNavigationRepository(){
        super();
    }

    public static synchronized RouteNavigationRepository getInstance(){
        if (instance == null){
            instance = new RouteNavigationRepository();
        }
        return instance;
    }

    public void requestNavigation(String profile, List<GeoPoint> geoPoints, NavigationListener navigationListener){
        super.getHttpClient().newCall(putRequest(profile, geoPoints)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                RouteNavigationRepository.super.getExecutorService().submit(()->{
                    try {
                        navigationListener.updateNavigation(RouteNavigationRepository.super.getNavigation(new JSONObject(response.body().string())));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public Request putRequest(String profile, List<GeoPoint> geoPoints){
        JSONObject coordinates = new JSONObject();
        JSONArray arrayCoordinates = new JSONArray();

        for (GeoPoint eachPoint : geoPoints) {
            try {
                arrayCoordinates.put(new JSONArray().put(eachPoint.getLatitude()).put(eachPoint.getLongitude()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            coordinates.put("coordinates", arrayCoordinates);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder()
                .url(url + s + profile)
                .put(RequestBody.create(JSON, coordinates.toString()))
                .header("api-key", apiKey)
                .build();
        return request;
    }
}
