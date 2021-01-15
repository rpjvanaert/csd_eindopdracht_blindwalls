package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MuralNavigationRepository {

    private final OkHttpClient httpClient;

    private ExecutorService executorService;

    private NavigationListener navigationListener;

    private static MuralNavigationRepository instance;

    public static final String url = "https://api.openrouteservice.org";
    public static final String s = "/v2/directions";
    public static final String PROFILE_WALKING = "/foot-walking";
    public static final String PROFILE_CYCLING = "/cycling-regular";
    public static final String apiKey = "5b3ce3597851110001cf6248345a40d8741343389fc17da4f5ad70e0";

    protected MuralNavigationRepository(){
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .build();

        this.executorService = Executors.newSingleThreadExecutor();
    }

    public static synchronized MuralNavigationRepository getInstance(){
        if (instance == null){
            instance = new MuralNavigationRepository();
        }
        return instance;
    }

    protected OkHttpClient getHttpClient(){
        return this.httpClient;
    }

    protected ExecutorService getExecutorService(){
        return this.executorService;
    }

    public void requestNavigation(String profile, GeoPoint start, GeoPoint end, NavigationListener navigationListener){
        this.navigationListener = navigationListener;
        httpClient.newCall(getRequest(profile ,start, end)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                executorService.submit(() -> {
                    try {
                        navigationListener.updateNavigation(getNavigation(new JSONObject(response.body().string())));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    /**
     * source: https://github.com/GIScience/openrouteservice-docs#geometry-decoding
     * @param encodedGeometry
     * @param inclElevation
     * @return
     */
    private static JSONArray decodeGeometry(String encodedGeometry, boolean inclElevation) {
        JSONArray geometry = new JSONArray();

        int len = encodedGeometry.length();
        int index = 0;
        int lat = 0;
        int lng = 0;
        int ele = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedGeometry.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedGeometry.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);


            if(inclElevation){
                result = 1;
                shift = 0;
                do {
                    b = encodedGeometry.charAt(index++) - 63 - 1;
                    result += b << shift;
                    shift += 5;
                } while (b >= 0x1f);
                ele += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);
            }

            JSONArray location = new JSONArray();
            try {
                location.put(lat / 1E5);
                location.put(lng / 1E5);
                if(inclElevation){
                    location.put((float) (ele / 100));
                }
                geometry.put(location);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return geometry;
    }

    protected static Navigation getNavigation(JSONObject responseObject){
        Navigation nav = new Navigation();

        try {
            JSONObject routeObject = responseObject.getJSONArray("routes").getJSONObject(0);

            JSONObject summary = routeObject.getJSONObject("summary");

            nav.setSummary(new NavigationSummary(
                    summary.getDouble("distance"),
                    summary.getDouble("duration")
            ));

            JSONArray segmentsArray = routeObject.getJSONArray("segments");

            for (int indexSegments = 0; indexSegments < segmentsArray.length(); ++indexSegments){

                JSONObject segmentObject = segmentsArray.getJSONObject(indexSegments);
                NavigationSegment segment = new NavigationSegment(new NavigationSummary(
                        segmentObject.getDouble("distance"),
                        segmentObject.getDouble("duration")
                ));

                JSONArray stepsArray = segmentObject.getJSONArray("steps");
                for (int indexSteps = 0; indexSteps < stepsArray.length(); ++indexSteps){

                    JSONObject stepObject = stepsArray.getJSONObject(indexSteps);
                    segment.addStep(new NavigationStep(
                            stepObject.getDouble("distance"),
                            stepObject.getDouble("duration"),
                            stepObject.getInt("type"),
                            stepObject.getString("instruction"),
                            stepObject.getString("name")
                    ));
                }
                nav.addSegment(segment);
            }

            JSONArray geometryArray = decodeGeometry(routeObject.getString("geometry"), false);
            for (int indexGeometry = 0; indexGeometry < geometryArray.length(); ++indexGeometry){
                JSONArray coordinates = geometryArray.getJSONArray(indexGeometry);
                nav.addGeometry(new GeoPoint(coordinates.getDouble(0), coordinates.getDouble(1)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return nav;
    }

    private static Request getRequest(String profile, GeoPoint start, GeoPoint end) {
        Request request = new Request.Builder()
                .url(url + s + profile)
                .header("api_key", apiKey)
                .header("start", start.getLatitude() + "," + start.getLongitude())
                .header("end", end.getLatitude() + "," + end.getLongitude())
                .build();
        return request;
    }
}
