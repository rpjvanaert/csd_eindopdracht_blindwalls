package logo.philist.csd_blindwalls_location_aware.Models.GPS;

import org.osmdroid.util.GeoPoint;

public interface GpsListener {
    void onLocationUpdate(GeoPoint location);

    void onGpsConnectionUpdate(boolean gpsIsOn);
}
