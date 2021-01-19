package logo.philist.csd_blindwalls_location_aware.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Data.NavigationStep;
import logo.philist.csd_blindwalls_location_aware.R;

public class NavigationStepsAdapter extends RecyclerView.Adapter<NavigationStepsAdapter.ViewHolder> {

    private List<NavigationStep> navigationSteps;

    public NavigationStepsAdapter(List<NavigationStep> navigationSteps) {
        this.navigationSteps = navigationSteps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.nav_step_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NavigationStep step = navigationSteps.get(position);

//        TODO holder.iconView.setImageResource();

        holder.textViewInstructions.setText(step.getInstruction());
    }

    @Override
    public int getItemCount() {
        return navigationSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iconView;
        private TextView textViewInstructions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iconView = itemView.findViewById(R.id.imageView_navIcon);
            textViewInstructions = itemView.findViewById(R.id.textView_instructions);
        }
    }
}
