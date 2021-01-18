package logo.philist.csd_blindwalls_location_aware.Views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Mural;
import logo.philist.csd_blindwalls_location_aware.R;
import logo.philist.csd_blindwalls_location_aware.ViewModels.Blindwalls.MuralsViewModel;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.MuralListAdapter;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.OnItemClickListener;


public class MuralsListActivity extends AppCompatActivity implements OnItemClickListener {

    private LiveData<List<Mural>> murals;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MuralsViewModel muralsViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(MuralsViewModel.class);

        murals = muralsViewModel.getMurals();
        MuralListAdapter muralListAdapter = new MuralListAdapter(this, this);
        recyclerView.setAdapter(muralListAdapter);
        murals.observe((LifecycleOwner) MuralsListActivity.this, muralListAdapter);
    }

    @Override
    public void onItemClick(int clickedPosition) {
        Intent detailIntent = new Intent(this, MuralDetailActivity.class);
        detailIntent.putExtra(MuralDetailActivity.TAG, murals.getValue().get(clickedPosition));
        startActivity(detailIntent);
    }
}
