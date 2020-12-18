package logo.philist.csd_blindwalls_location_aware;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExtendedFloatingActionButton fabMurals = findViewById(R.id.fabButton_toMurals);
        ExtendedFloatingActionButton fabRoutes = findViewById(R.id.fabButton_toRoutes);
    }
}