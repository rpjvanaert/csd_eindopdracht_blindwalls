package logo.philist.csd_blindwalls_location_aware.Views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.io.Serializable;
import java.util.List;

import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Mural;
import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Route;
import logo.philist.csd_blindwalls_location_aware.R;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.MuralListAdapter;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.OnItemClickListener;

public class RouteDetailActivity extends AppCompatActivity implements OnItemClickListener {

    public static final String TAG = RouteDetailActivity.class.getName();
    public static final String TAG_ROUTE = TAG + "_ROUTE";
    public static final String TAG_MURALS = TAG + "_MURALS";

    private Route route;
    private MutableLiveData<List<Mural>> murals;

    private MaterialTextView mTextViewTitle;
    private MaterialTextView mTextViewTimeAndType;
    private MaterialTextView mTextViewDistance;

    private RecyclerView recyclerViewMurals;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_detail_activity);

        route = (Route) getIntent().getSerializableExtra(TAG_ROUTE);
        murals = new MutableLiveData<>((List<Mural>) getIntent().getSerializableExtra(TAG_MURALS));

        mTextViewTitle = findViewById(R.id.mTextview_routeTitle);
        mTextViewTimeAndType = findViewById(R.id.mTextview_timeAndType);
        mTextViewDistance = findViewById(R.id.mTextview_distance);

        recyclerViewMurals = findViewById(R.id.recyclerview_routeMurals);

        mTextViewTitle.setText(route.getName());
        mTextViewTimeAndType.setText(route.getTime() + " " + route.getType());
        mTextViewDistance.setText(route.getDistance());

        recyclerViewMurals.setLayoutManager(new LinearLayoutManager(this));
        MuralListAdapter muralListAdapter = new MuralListAdapter(this, this);
        recyclerViewMurals.setAdapter(muralListAdapter);
        murals.observe((LifecycleOwner) RouteDetailActivity.this, muralListAdapter);

        ExtendedFloatingActionButton floatingActionButton = findViewById(R.id.fab_nav);
        floatingActionButton.setOnClickListener(view -> {
            Intent intentNavigate = new Intent(this, RouteNavigationActivity.class);
            intentNavigate.putExtra(RouteNavigationActivity.TAG_ROUTE, route);
            intentNavigate.putExtra(RouteNavigationActivity.TAG_MURAL, (Serializable) murals.getValue());
            startActivity(intentNavigate);
        });
    }

    @Override
    public void onItemClick(int clickedPosition) {
        Intent detailIntent = new Intent(this, MuralDetailActivity.class);
        detailIntent.putExtra(MuralDetailActivity.TAG, murals.getValue().get(clickedPosition));
        startActivity(detailIntent);
    }
}
