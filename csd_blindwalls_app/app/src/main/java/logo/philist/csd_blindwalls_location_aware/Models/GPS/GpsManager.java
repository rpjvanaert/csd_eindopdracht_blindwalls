package logo.philist.csd_blindwalls_location_aware.Models.GPS;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class GpsManager implements LocationListener {

    private final GpsListener listener;
    private final LocationManager locationManager;
    private GeoPoint lastKnownLocation;

    public GpsManager(Activity activity, GpsListener listener, GeoPoint lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
        if (activity == null) {
            throw new IllegalArgumentException("Context is not allowed to be null");
        }

        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        this.listener = listener;

        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        }

        for (String provider : this.locationManager.getProviders(true)) {
            try {
                this.locationManager.requestLocationUpdates(provider, 0, 0, this);
            }
            catch (Exception ex) {
                Log.e("GPSManager", "Unable to request location updates for provider " + provider, ex);
            }
        }
    }

    public GeoPoint getLastKnownLocation() {
        return this.lastKnownLocation;
    }

    public void destroy(){
        try {
            this.locationManager.removeUpdates(this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.lastKnownLocation = new GeoPoint(location);
        this.listener.onLocationUpdate(this.lastKnownLocation);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)){
            this.listener.onGpsConnectionUpdate(true);
        }
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)){
            this.listener.onGpsConnectionUpdate(false);
        }
    }
}
