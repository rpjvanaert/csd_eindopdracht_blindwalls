package logo.philist.csd_blindwalls_location_aware.Views.Adapters;

import android.app.Activity;
import android.graphics.Paint;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Navigation;
import logo.philist.csd_blindwalls_location_aware.R;

public class RouteMarker {

    private Navigation navigation;
    private Polyline polyline;
    private MapView mapView;
    private Activity activity;

    public RouteMarker(Activity activity, Navigation navigation, MapView mapView) {
        this.activity = activity;
        this.navigation = navigation;
        this.mapView = mapView;
        this.polyline = new Polyline();
        this.drawRoute();
    }

    public RouteMarker(Activity activity, MapView mapView){
        this.activity = activity;
        this.mapView = mapView;
        this.navigation = null;
        this.polyline = new Polyline();
    }

    private void drawRoute(){
        this.mapView.getOverlayManager().remove(this.polyline);
        this.polyline = new Polyline();

        for(GeoPoint eachGeoPoint : navigation.getGeometry()){
            this.polyline.addPoint(eachGeoPoint);
        }

        Paint outlinePaint = this.polyline.getOutlinePaint();
        outlinePaint.setColor(this.activity.getColor(R.color.colorSecondaryLight));
        outlinePaint.setStrokeWidth(3f);

        this.mapView.getOverlayManager().add(this.polyline);
        this.mapView.invalidate();
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
        this.drawRoute();
    }
}
