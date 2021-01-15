package logo.philist.csd_blindwalls_location_aware.Views.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Mural;
import logo.philist.csd_blindwalls_location_aware.R;
import logo.philist.csd_blindwalls_location_aware.Views.MuralDetailActivity;

public class MuralMarker implements Observer<List<Mural>> {

    private List<Mural> murals;
    private MapView mapView;
    private Map<Integer, Marker> markerMap;

    private Activity activity;

    public MuralMarker(Activity activity, MapView mapView, List<Mural> murals) {
        this.mapView = mapView;
        this.murals = murals;
        this.markerMap = new HashMap<>();

        this.activity = activity;

        this.update();
    }

    public MuralMarker(Activity activity, MapView mapView) {
        this(activity, mapView, new ArrayList<>());
    }

    public void setMurals(List<Mural> murals) {
        this.murals = murals;
    }

    private void setMarker(Mural mural){
        markerMap.remove(mural.getId());
        markerMap.put(mural.getId(), createMarker(mural));
        mapView.getOverlayManager().add(markerMap.get(mural.getId()));
    }

    private Marker createMarker(Mural mural) {
        Marker marker = new Marker(mapView, activity);
        marker.setAlpha(1f);
        marker.setPosition(mural.getGeoPoint());
        marker.setIcon(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_baseline_location_on_24, null));
        marker.setInfoWindow(null);
        marker.setOnMarkerClickListener((marker1, mapView) -> {
            openMural(mural);
            return true;
        });
        return marker;
    }

    private void openMural(Mural mural){
        Intent detailIntent = new Intent(activity, MuralDetailActivity.class);
        detailIntent.putExtra(MuralDetailActivity.TAG, mural);
        activity.startActivity(detailIntent);
    }

    private void update() {
        for(Mural eachMural : this.murals){
            setMarker(eachMural);
        }
    }

    @Override
    public void onChanged(List<Mural> murals) {
        this.murals = murals;
        this.update();
    }
}
