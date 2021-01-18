package logo.philist.csd_blindwalls_location_aware.Views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.HashMap;
import java.util.Map;

import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Mural;
import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.MuralNavigationRepository;
import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Navigation;
import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.NavigationListener;
import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.RouteNavigationRepository;
import logo.philist.csd_blindwalls_location_aware.R;
import logo.philist.csd_blindwalls_location_aware.ViewModels.Blindwalls.MuralsViewModel;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.Localisation;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.LocalisationListener;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.MuralMarker;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.RouteMarker;

import static logo.philist.csd_blindwalls_location_aware.Views.MainActivity.standardLocation;
import static logo.philist.csd_blindwalls_location_aware.Views.MainActivity.standardZoom;

public class MuralNavigationActivity extends AppCompatActivity implements LocalisationListener, NavigationListener {

    public static final String TAG = MuralNavigationActivity.class.getName();
    public static final String TAG_MURAL = TAG + "_MURAL";

    private Mural mural;

    private Map<Integer, Marker> markerMap;
    private MapView mapView;
    private IMapController mapController;
    private Marker currentLocationMarker;

    private Localisation localisation;

    private MuralMarker muralMarker;
    private RouteMarker routeMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_layout);
    }

    @Override
    protected void onStart() {
        init();
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    private void init(){
        Intent intent = getIntent();
        mural = (Mural)intent.getSerializableExtra(TAG_MURAL);

        TextView textViewHeader = findViewById(R.id.textView_navigationHeader);
        String headText = getString(R.string.going_to_mural_to) + mural.getAddress();
        textViewHeader.setText(headText);

        //Get muralsviewmodel for the main map
        MuralsViewModel muralsViewModel = new ViewModelProvider(this).get(MuralsViewModel.class);

        //Init mapview, controller and muralMarker
        this.mapView = findViewById(R.id.nav_mapView);

        this.muralMarker = new MuralMarker(this, mapView);
        muralsViewModel.getMurals().observe(this, muralMarker);

        this.mapController = mapView.getController();
        this.mapController.setZoom(standardZoom);

        //check gps permission
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        }

        //mapView settings
        this.mapView.setMultiTouchControls(true);
        this.mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mapController.animateTo(standardLocation);

        markerMap = new HashMap<>();

        currentLocationMarker = new Marker(mapView, this);
        currentLocationMarker.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_nav, null));
        currentLocationMarker.setInfoWindow(null);
        mapView.getOverlays().add(currentLocationMarker);
        localisation = new Localisation(this, (LocalisationListener) this);

        //          Routemarker section

        MuralNavigationRepository repos = MuralNavigationRepository.getInstance();
        Log.i(TAG, "requesting navigation");
        repos.requestNavigation(
                MuralNavigationRepository.PROFILE_WALKING,
                new GeoPoint(localisation.getLocation()), mural.getGeoPoint(),
                this
        );

        this.routeMarker = new RouteMarker(this, mapView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.localisation.destroy();
    }

    @Override
    public void onLocationUpdate(Location location) {
        currentLocationMarker.setPosition(new GeoPoint(location));

//        mapController.setCenter(currentLocationMarker.getPosition());
        mapView.invalidate();
    }

    @Override
    public void onGpsConnectionUpdate(boolean gpsIsOn) {
        if (gpsIsOn){
            Toast.makeText(this, "GPS has been connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "GPS signal was lost!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setRotation(float rotation) {
        currentLocationMarker.setRotation(rotation);
        mapView.invalidate();
    }

    @Override
    public void updateNavigation(Navigation navigation) {
        routeMarker.setNavigation(navigation);
    }
}
