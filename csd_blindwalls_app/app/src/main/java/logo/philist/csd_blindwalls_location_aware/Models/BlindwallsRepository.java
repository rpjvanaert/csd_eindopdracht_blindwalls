package logo.philist.csd_blindwalls_location_aware.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BlindwallsRepository {

    private final MutableLiveData<List<Mural>> murals;
    private final MutableLiveData<List<Route>> routes;
    private final OkHttpClient httpClient;

    private static BlindwallsRepository instance;
    private static final String headerAccessToken = "X-Access-Token";
    private static final String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJibGluZHdhbGwiLCJleHAiOjE2MDgxMjMxMzA2MzB9.R5bpThiuXb4M9u8cG5KmARv6M_BTWL4xtAzRFGXkSTM";
    private static final String blindwallsUrl = "api.blindwalls.gallery";
    private static final String muralUrl = "/murals";
    private static final String routeUrl = "/routes";

    private BlindwallsRepository(){
        this.murals = new MutableLiveData<>();
        this.routes = new MutableLiveData<>();

        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public static synchronized BlindwallsRepository getInstance(){
        if(instance == null){
            instance = new BlindwallsRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Mural>> getMurals(){
        return this.murals;
    }

    public MutableLiveData<List<Route>> getRoutes(){
        return this.routes;
    }

    public void requestMurals(){
        httpClient.newCall(getRequest(muralUrl)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    murals.setValue(readMurals(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<Mural> readMurals(Response response) throws JSONException, IOException {
        JSONArray jsonResponse = new JSONArray(response.body().string());
        List<Mural> murals = new ArrayList<>();

        for (int i = 0; i < jsonResponse.length(); i++) {
            JSONObject muralJson = jsonResponse.getJSONObject(i);

            int id = muralJson.getInt("id");
            Date date = new Date(muralJson.getLong("date"));
            GeoPoint geoPoint = new GeoPoint(muralJson.getDouble("latitude"), muralJson.getDouble("longitude"));
            String address = muralJson.getString("address");
            String author = muralJson.getString("author");
            double rating = muralJson.getDouble("rating");

            Mural mural = new Mural(id, geoPoint, address, date, author, rating);

            JSONObject title = muralJson.getJSONObject("title");
            String titleEN = title.getString("en");
            String titleNL = title.getString("nl");
            mural.setTitle(titleEN, titleNL);

            JSONObject description = muralJson.getJSONObject("description");
            String descriptionEN = description.getString("en");
            String descriptionNL = description.getString("nl");
            mural.setDescription(descriptionEN, descriptionNL);

            JSONObject material = muralJson.getJSONObject("material");
            String materialEN = material.getString("en");
            String materialNL = material.getString("nl");
            mural.setMaterial(materialEN, materialNL);

            JSONObject category = muralJson.getJSONObject("category");
            String categoryEN = category.getString("en");
            String categoryNL = category.getString("nl");
            mural.setCategory(categoryEN, categoryNL);

            JSONArray images = muralJson.getJSONArray("images");
            List<String> imageList = new ArrayList<>();
            for (int j = 0; j < images.length(); j++) {
                JSONObject imageObject = images.getJSONObject(j);
                imageList.add(blindwallsUrl + "/" + imageObject.getString("url"));
            }
            mural.setImages(imageList);

            murals.add(mural);
        }
        return murals;
    }

    public void requestRoutes(){
        httpClient.newCall(getRequest(routeUrl)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    routes.setValue(readRoutes(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<Route> readRoutes(Response response) throws IOException, JSONException {
        JSONArray routesArray = new JSONArray(response.body().string());
        List<Route> routes = new ArrayList<>();

        for (int i = 0; i < routesArray.length(); i++) {
            JSONObject routeObject = routesArray.getJSONObject(i);

            String name = routeObject.getJSONObject("name").getString("en");
            String time = routeObject.getString("time");
            String distance = routeObject.getString("distance");
            String type = routeObject.getString("type");

            Route route = new Route(name, time, distance, type);

            JSONArray points = routeObject.getJSONArray("points");
            for (int j = 0; j < points.length(); j++) {
                JSONObject point = points.getJSONObject(j);
                route.setMuralId(point.getInt("order"), point.getInt("muralId"));
            }

            routes.add(route);
        }

        return routes;
    }

    public Request getRequest(String typeUrl){
        Request request = new Request.Builder()
                .url(blindwallsUrl + typeUrl)
                .header(headerAccessToken, accessToken)
                .build();
        return request;
    }

}
