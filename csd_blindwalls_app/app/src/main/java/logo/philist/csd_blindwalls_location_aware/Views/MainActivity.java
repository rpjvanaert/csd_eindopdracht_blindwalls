package logo.philist.csd_blindwalls_location_aware.Views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.HashMap;
import java.util.Map;

import logo.philist.csd_blindwalls_location_aware.BuildConfig;
import logo.philist.csd_blindwalls_location_aware.Models.GeoFencing.GeoFenceManager;
import logo.philist.csd_blindwalls_location_aware.R;
import logo.philist.csd_blindwalls_location_aware.ViewModels.Blindwalls.MuralsViewModel;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.MapIndication.Localisation;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.MapIndication.LocalisationListener;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.Markers.MuralMarker;

public class MainActivity extends AppCompatActivity implements LocalisationListener {

    public static final String TAG_VIEWMODEL_STORE_OWNER = MainActivity.class.getName() + "_VIEWMODEL_STORE_OWNER";

    public static final GeoPoint standardLocation = new GeoPoint(51.588016, 4.775422);
    public static final double standardZoom = 18.0;

    private Map<Integer, Marker> markerMap;
    private MapView mapView;
    private IMapController mapController;
    private Marker currentLocationMarker;

    private Localisation localisation;

    private MuralMarker muralMarker;

    private ExtendedFloatingActionButton fabMenu;
    private ExtendedFloatingActionButton fabMurals;
    private ExtendedFloatingActionButton fabRoutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GeoFenceManager.createInstance(this);

        //Get muralsviewmodel for the main map
        MuralsViewModel muralsViewModel = new ViewModelProvider(this).get(MuralsViewModel.class);

        //Init mapview, controller and muralMarker
        this.mapView = findViewById(R.id.main_mapview);

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

        fabMenu = findViewById(R.id.fab_extendMenu);
        fabMurals = findViewById(R.id.fab_go_to_murals);
        fabRoutes = findViewById(R.id.fab_go_to_routes);

        fabMenu.shrink();
        fabMurals.shrink();
        fabRoutes.shrink();

        fabMenu.setOnClickListener(view -> {
            if (fabMenu.isExtended()){
                closeFabMenu();
            } else {
                openFabMenu();
            }
        });

        fabMurals.setOnClickListener(view -> {
            if (fabMurals.isExtended()){
                Intent intent = new Intent(this, MuralsListActivity.class);
                startActivity(intent);
                closeFabMenu();
            } else {
                fabMurals.extend();
            }
        });

        fabRoutes.setOnClickListener(view -> {
            if (fabRoutes.isExtended()){
                Intent intent = new Intent(this, RouteListActivity.class);
                startActivity(intent);
                closeFabMenu();
            } else {
                fabRoutes.extend();
            }
        });
    }

    private void openFabMenu() {
        fabRoutes.animate().translationY(-getResources().getDimension(R.dimen.standard_route_offset));
        fabMurals.animate().translationY(-getResources().getDimension(R.dimen.standard_murals_offset)).withEndAction(() -> {
            fabMenu.extend();
            fabRoutes.extend();
            fabMurals.extend();
        });
    }

    private void closeFabMenu() {
        fabMenu.shrink();
        fabRoutes.shrink();
        fabMurals.shrink(new ExtendedFloatingActionButton.OnChangedCallback() {
            @Override
            public void onShrunken(ExtendedFloatingActionButton extendedFab) {
                super.onShrunken(extendedFab);
                fabRoutes.animate().translationY(0);
                fabMurals.animate().translationY(0);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        this.localisation.destroy();
    }

    @Override
    public void onLocationUpdate(Location location) {
        currentLocationMarker.setPosition(new GeoPoint(location));

        //TODO Opt: center on current location
//        mapController.setCenter(currentLocationMarker.getPosition());
//        mapView.invalidate();
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
}