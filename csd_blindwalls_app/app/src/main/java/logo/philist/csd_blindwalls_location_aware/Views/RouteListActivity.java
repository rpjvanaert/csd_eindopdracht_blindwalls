package logo.philist.csd_blindwalls_location_aware.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import logo.philist.csd_blindwalls_location_aware.Models.Mural;
import logo.philist.csd_blindwalls_location_aware.Models.Route;
import logo.philist.csd_blindwalls_location_aware.R;
import logo.philist.csd_blindwalls_location_aware.ViewModels.RoutesViewModel;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.RouteListAdapter;

public class RouteListActivity extends AppCompatActivity implements OnItemClickListener {

    private LiveData<List<Route>> routes;
    private LiveData<List<Mural>> murals;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RoutesViewModel routesViewModel = new ViewModelProvider((ViewModelStoreOwner)RouteListActivity.this).get(RoutesViewModel.class);

        routes = routesViewModel.getRoutes();
        murals = routesViewModel.getMurals();

        RouteListAdapter routeListAdapter = new RouteListAdapter(this);
        recyclerView.setAdapter(routeListAdapter);
        routes.observe((LifecycleOwner)RouteListActivity.this, routeListAdapter);
    }

    @Override
    public void onItemClick(int clickedPosition) {
        Log.i(RouteListActivity.class.getName(), "click");
        Intent detailIntent = new Intent(this, RouteDetailActivity.class);
        detailIntent.putExtra(RouteDetailActivity.TAG_ROUTE, routes.getValue().get(clickedPosition));
        detailIntent.putExtra(RouteDetailActivity.TAG_MURALS, (Serializable) routes.getValue().get(clickedPosition).getMurals(murals.getValue()));
        startActivity(detailIntent);
    }
}