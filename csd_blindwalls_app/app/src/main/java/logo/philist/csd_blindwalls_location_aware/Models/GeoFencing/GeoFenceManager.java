package logo.philist.csd_blindwalls_location_aware.Models.GeoFencing;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Arrays;
import java.util.HashMap;

import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Mural;

public class GeoFenceManager extends ContextWrapper implements LocationListener {
    private static final float GEOFENCE_RADIUS = 100;
    private static GeoFenceManager instance;
    private GeofencingClient geofencingClient;
    private Context context;
    private PendingIntent geofencePendingIntent;
    private HashMap<String, Mural> muralHashMap;

    private HashMap<String, GeoFenceRunnable> runnableHashMap;


    public static GeoFenceManager createInstance(Context context) {
        if (instance == null) {
            instance = new GeoFenceManager(context);
        }
        return instance;
    }

    public static GeoFenceManager getInstance() {
        return instance;
    }

    private GeoFenceManager(Context context) {
        super(context);
        this.geofencingClient = LocationServices.getGeofencingClient(this);
        this.context = context;
        this.runnableHashMap = new HashMap<>();
        this.muralHashMap = new HashMap<>();


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION},1337);
            return;
        }

          /*
        this is incredibly stupid and i absolutely hate this but GeoFencing simply wont work well
         if we don't call these useless functions
         */
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    /** adds a fence to the system
     * @param mural the point for the geofence
     * @param runnable the code that is executed upon entering the fence
     */
    public void AddFence(Mural mural, GeoFenceRunnable runnable) {
        Geofence geofence = getGeoFence(mural);

        GeofencingRequest request = generateGeoRequest(geofence);

        this.geofencePendingIntent = getGeofencePendingIntent();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION},1337);
            return;
        }

        geofencingClient.addGeofences(request,geofencePendingIntent)
        .addOnFailureListener(e -> {
       //     Toast.makeText(context,"onFailed",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        });
        runnableHashMap.put(mural.getId()+"",runnable);
        muralHashMap.put(mural.getId()+"",mural);

    }

    /**
     * Removes the given point of interest from the list of fences
     * @param mural
     */
    public void ClearFence(Mural mural){
        geofencingClient.removeGeofences(Arrays.asList(mural.getId()+""));
        runnableHashMap.remove(mural.getId()+"");
        muralHashMap.remove(mural.getId()+"");
    }


    private GeofencingRequest generateGeoRequest(Geofence geofence){
        return new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();
    };

    public Geofence getGeoFence(Mural mural){
         return new Geofence.Builder()
                .setRequestId(mural.getId() + "")
                .setCircularRegion(
                        mural.getGeoPoint().getLatitude(),
                        mural.getGeoPoint().getLongitude(),
                        GEOFENCE_RADIUS
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                 .setLoiteringDelay(10)
                .setNotificationResponsiveness(10)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();


    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);

        geofencePendingIntent = PendingIntent.getBroadcast(this, 1337, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return geofencePendingIntent;
    }

    /**
     * alert() will execute a runnable linked to a point
     * alert() is called by GeoFenceBroadcastReciever and should not be called by the user
     * @param id id of the given poi
     */
    public void alert(String id){
        ((Activity)context).runOnUiThread(()->{
            if (runnableHashMap.containsKey(id)) {
                runnableHashMap.get(id).run(muralHashMap.get(id));
            }
        });

    }

    /**
     * All these functions are empty and serve 0 purpose but removing the breaks everything so they have to remain
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
