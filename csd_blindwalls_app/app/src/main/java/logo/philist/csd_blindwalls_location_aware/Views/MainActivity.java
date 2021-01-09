package logo.philist.csd_blindwalls_location_aware.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logo.philist.csd_blindwalls_location_aware.BuildConfig;
import logo.philist.csd_blindwalls_location_aware.Models.GPS.GpsListener;
import logo.philist.csd_blindwalls_location_aware.Models.GPS.GpsManager;
import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Mural;
import logo.philist.csd_blindwalls_location_aware.R;
import logo.philist.csd_blindwalls_location_aware.ViewModels.Blindwalls.MuralsViewModel;

public class MainActivity extends AppCompatActivity implements Observer<List<Mural>>, GpsListener {

    public static final String TAG_VIEWMODEL_STORE_OWNER = MainActivity.class.getName() + "_VIEWMODEL_STORE_OWNER";

    private static final GeoPoint standardLocation = new GeoPoint(51.588016, 4.775422);
    private static final double standardZoom = 18.0;

    private Map<Integer, Marker> markerMap;
    private Polyline routePolyLine;
    private MapView mapView;
    private IMapController mapController;
    private Marker currentLocationMarker;

    private GpsManager gpsManager;

    private MuralsViewModel muralsViewModel;
    private List<Mural> murals;

    private ExtendedFloatingActionButton fabMenu;
    private ExtendedFloatingActionButton fabMurals;
    private ExtendedFloatingActionButton fabRoutes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get muralsviewmodel for the main map
        this.muralsViewModel = new ViewModelProvider(this).get(MuralsViewModel.class);
        this.murals = new ArrayList<>();
        muralsViewModel.getMurals().observe(this, this);


        //Init mapview and controller
        this.mapView = findViewById(R.id.main_mapview);
        this.mapController = mapView.getController();
        this.mapController.setZoom(standardZoom);

        //check gps permission
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        }

        //mapview settings
        this.mapView.setMultiTouchControls(true);
        this.mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        mapController.animateTo(standardLocation);

        markerMap = new HashMap<>();

        this.gpsManager = new GpsManager(this, this);

//        this.myLocation = new MyLocationNewOverlay(mapView);

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


//        //Init fab buttons of murals and routes
//        ExtendedFloatingActionButton fabMurals = findViewById(R.id.fabButton_toMurals);
//        ExtendedFloatingActionButton fabRoutes = findViewById(R.id.fabButton_toRoutes);
//
//        fabMurals.setOnClickListener((view -> {
//            Log.i(MainActivity.class.getName(), "clicked fab Murals");
//            Intent intent = new Intent(this, MuralsListActivity.class);
//            startActivity(intent);
//        }));
//
//        fabRoutes.setOnClickListener(view -> {
//            Log.i(MainActivity.class.getName(), "clicked fab Routes");
//            Intent intent = new Intent(this, RouteListActivity.class);
//            startActivity(intent);
//        });
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
    public void onChanged(List<Mural> murals) {
        this.murals = murals;

        if (routePolyLine != null){
            mapView.getOverlayManager().remove(routePolyLine);
        }
        this.routePolyLine = new Polyline();

        routePolyLine.addPoint(murals.get(0).getGeoPoint());
        routePolyLine.addPoint(murals.get(1).getGeoPoint());
        routePolyLine.addPoint(murals.get(2).getGeoPoint());

        Paint outlinePaint = routePolyLine.getOutlinePaint();
        outlinePaint.setColor(getColor(R.color.colorSecondaryLight));
        outlinePaint.setStrokeWidth(3f);

        mapView.getOverlayManager().add(routePolyLine);

        for (Mural mural : this.murals) {
            getOrSetMarker(mural);
        }
    }

    private Marker getOrSetMarker(Mural mural) {
        if (!markerMap.containsKey(mural.getId())){
            markerMap.put(mural.getId(), createMarker(mural));
            mapView.getOverlayManager().add(markerMap.get(mural.getId()));
        }

        return markerMap.get(mural.getId());
    }

    private Marker createMarker(Mural mural) {
        Marker marker = new Marker(mapView, this);
        marker.setAlpha(1f);
        marker.setPosition(mural.getGeoPoint());
        marker.setIcon(getDrawable(R.drawable.ic_baseline_location_on_24));
        marker.setInfoWindow(null);
        marker.setOnMarkerClickListener((markerC, viewC) ->{
            openMural(mural);
            return true;
        });

        return marker;
    }

    private void openMural(Mural mural) {
        Intent detailIntent = new Intent(this, MuralDetailActivity.class);
        detailIntent.putExtra(MuralDetailActivity.TAG, mural);
        startActivity(detailIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        this.gpsManager.destroy();
    }

    @Override
    public void onLocationUpdate(Location location) {
        Marker selfMarker = new Marker(mapView, this);
        selfMarker.setPosition(new GeoPoint(location));
        selfMarker.setRotation(location.getBearing());
        Log.d(GpsManager.class.getName(), "Bearing:= " + location.hasBearing() + location.getBearing());
        selfMarker.setIcon(getDrawable(R.drawable.ic_nav));




        mapView.getOverlays().remove(currentLocationMarker);
        currentLocationMarker = selfMarker;
        mapView.getOverlays().add(currentLocationMarker);


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
}