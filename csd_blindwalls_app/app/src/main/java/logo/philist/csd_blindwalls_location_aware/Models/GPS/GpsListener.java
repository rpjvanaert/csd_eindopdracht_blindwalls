package logo.philist.csd_blindwalls_location_aware.Models.GPS;

import android.location.Location;

import org.osmdroid.util.GeoPoint;

public interface GpsListener {
    void onLocationUpdate(Location location);

    void onGpsConnectionUpdate(boolean gpsIsOn);
}
