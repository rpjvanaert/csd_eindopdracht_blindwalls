package logo.philist.csd_blindwalls_location_aware.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import logo.philist.csd_blindwalls_location_aware.R;
import logo.philist.csd_blindwalls_location_aware.ViewModels.MuralsViewModel;

public class MainActivity extends AppCompatActivity {

    public static final String TAG_VIEWMODEL_STORE_OWNER = MainActivity.class.getName() + "_VIEWMODEL_STORE_OWNER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExtendedFloatingActionButton fabMurals = findViewById(R.id.fabButton_toMurals);
        ExtendedFloatingActionButton fabRoutes = findViewById(R.id.fabButton_toRoutes);

        fabMurals.setOnClickListener((view -> {
            Log.i(MainActivity.class.getName(), "clicked fab Murals");
            Intent intent = new Intent(this, MuralsListActivity.class);
            MuralsViewModel muralsViewModel = new ViewModelProvider(this).get(MuralsViewModel.class);
            startActivity(intent);
        }));

        fabRoutes.setOnClickListener(view -> {
            Log.i(MainActivity.class.getName(), "clicked fab Routes");
            Intent intent = new Intent(this, RouteListActivity.class);
            startActivity(intent);
        });
    }
}