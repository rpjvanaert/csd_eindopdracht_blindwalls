package logo.philist.csd_blindwalls_location_aware.Views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Mural;
import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Route;
import logo.philist.csd_blindwalls_location_aware.Models.Language;
import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Data.Navigation;
import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.MuralNavigationRepository;
import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.NavigationListener;
import logo.philist.csd_blindwalls_location_aware.Models.UserNotifier;
import logo.philist.csd_blindwalls_location_aware.R;
import logo.philist.csd_blindwalls_location_aware.ViewModels.Blindwalls.MuralsViewModel;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.MapIndication.Localisation;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.MapIndication.LocalisationListener;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.Markers.MuralMarker;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.Markers.RouteMarker;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.MessageDialog;

import static logo.philist.csd_blindwalls_location_aware.Views.MainActivity.standardLocation;
import static logo.philist.csd_blindwalls_location_aware.Views.MainActivity.standardZoom;

public class RouteNavigationActivity extends AppCompatActivity implements LocalisationListener, NavigationListener, UserNotifier {

    public static final String TAG = RouteNavigationActivity.class.getName();
    public static final String TAG_MURAL = TAG + "_MURAL";
    public static final String TAG_ROUTE = TAG + "_ROUTE";

    private List<Mural> murals;
    private Route route;

    private Map<Integer, Marker> markerMap;
    private MapView mapView;
    private IMapController mapController;
    private Marker currentLocationMarker;

    private Localisation localisation;

    private MuralMarker muralMarker;
    private RouteMarker routeMarker;
    private Navigation navigation;

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

    private void init(){
        Intent intent = getIntent();
        murals = (List<Mural>)intent.getSerializableExtra(TAG_MURAL);
        route = (Route)intent.getSerializableExtra(TAG_ROUTE);

        TextView textViewHeader = findViewById(R.id.textView_navigationHeader);
        String headText = getString(R.string.going_to_mural_to) + route.getName();
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

        List<GeoPoint> geoPointsMurals = getGeoPoints(murals);
        geoPointsMurals.add(new GeoPoint(localisation.getLocation()));
        MuralNavigationRepository repos = MuralNavigationRepository.getInstance();
        Log.i(TAG, "requesting navigation");
        repos.requestNavigation(
                MuralNavigationRepository.PROFILE_WALKING,
                geoPointsMurals,
                this,
                this
        );

        this.routeMarker = new RouteMarker(this, mapView);

        ExtendedFloatingActionButton sheetButton = findViewById(R.id.button_bottomSheetExtend);

        sheetButton.setOnClickListener(view -> {
            NavigationInstructionDialog navigationInstructionDialog = new NavigationInstructionDialog(route.getName(),navigation, route.getMurals(murals));
            navigationInstructionDialog.show(getSupportFragmentManager(), "ModalBottomSheet");
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.localisation.destroy();
    }

    private List<GeoPoint> getGeoPoints(List<Mural> murals) {
        List<GeoPoint> geoPoints = new ArrayList<>();
        for (Mural each : murals){
            geoPoints.add(each.getGeoPoint());
        }
        return geoPoints;
    }

    @Override
    public void onLocationUpdate(Location location) {
        currentLocationMarker.setPosition(new GeoPoint(location));

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
    public void updateNavigation(Navigation navigation) {
        routeMarker.setNavigation(navigation);
        this.navigation = navigation;
    }

    @Override
    public void setRotation(float rotation) {
        currentLocationMarker.setRotation(rotation);
        mapView.invalidate();
    }

    @Override
    public void showError(String title, int stringResourceId) {
        MessageDialog dialog = new MessageDialog(title, getString(stringResourceId));
        dialog.show(getSupportFragmentManager(), MainActivity.class.getName());
    }
}
