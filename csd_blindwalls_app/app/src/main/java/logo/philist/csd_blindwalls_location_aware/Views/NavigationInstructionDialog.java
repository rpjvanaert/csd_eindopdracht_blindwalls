package logo.philist.csd_blindwalls_location_aware.Views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Data.Navigation;
import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Data.NavigationSegment;
import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Data.NavigationStep;
import logo.philist.csd_blindwalls_location_aware.R;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.NavigationStepsAdapter;

public class NavigationInstructionDialog extends BottomSheetDialogFragment {

    private Navigation navigation;
    private String title;

    public NavigationInstructionDialog(String title, Navigation navigation) {
        super();
        this.title = title;
        this.navigation = navigation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routing_bottom_sheet_layout, container, false);

        init(view);

        return view;
    }

    public void init(View view){
        TextView textViewTitle = view.findViewById(R.id.textView_titleSheet);
        textViewTitle.setText(title);

        RecyclerView navigationList = view.findViewById(R.id.recyclerview_navigation_steps);
        navigationList.setLayoutManager(new LinearLayoutManager(view.getContext()));

        List<NavigationStep> steps = new ArrayList<>();
        for(NavigationSegment segment : navigation.getSegments()){
            steps.addAll(segment.getSteps());
            Log.i("Navigation Step of " + title, segment.getSteps().get(0).getInstruction());
        }

        NavigationStepsAdapter navigationStepsAdapter = new NavigationStepsAdapter(steps);
        navigationList.setAdapter(navigationStepsAdapter);
    }
}
