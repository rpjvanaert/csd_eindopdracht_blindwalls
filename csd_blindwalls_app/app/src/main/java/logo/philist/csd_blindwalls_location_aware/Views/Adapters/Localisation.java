package logo.philist.csd_blindwalls_location_aware.Views.Adapters;

import android.app.Activity;
import android.location.Location;

import org.osmdroid.util.GeoPoint;

import logo.philist.csd_blindwalls_location_aware.Models.GPS.GpsListener;
import logo.philist.csd_blindwalls_location_aware.Models.GPS.GpsManager;

public class Localisation {

    private GpsManager gpsManager;
    private DirectionListener directionListener;

    public Localisation(Activity activity, LocalisationListener localisationListener){
        this.gpsManager = new GpsManager(activity, (GpsListener)localisationListener);
        this.directionListener = new DirectionListener(activity, (RotationListener)localisationListener);
    }

    public void destroy(){
        this.gpsManager.destroy();
        this.directionListener.unregister();
    }

    public Location getLocation() {
        return this.gpsManager.getLastKnownLocation();
    }
}
