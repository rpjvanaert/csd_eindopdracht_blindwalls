package logo.philist.csd_blindwalls_location_aware.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import logo.philist.csd_blindwalls_location_aware.R;

public class NavigationStepsAdapter extends RecyclerView.Adapter<NavigationStepsAdapter.ViewHolder> {

    private List<NavigationSheetInfo> sheetInfo;

    public NavigationStepsAdapter(List<NavigationSheetInfo> sheetInfo) {
        this.sheetInfo = sheetInfo;
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
        NavigationSheetInfo sheetInfo = this.sheetInfo.get(position);

//        TODO holder.iconView.setImageResource();

//        holder.textViewInstructions.setText(step.getText());

        if (sheetInfo.getType() == -1){
            holder.arriveLayout.setVisibility(View.VISIBLE);
            holder.textArrive.setText(sheetInfo.getText());
        } else {
            holder.navLayout.setVisibility(View.VISIBLE);
            holder.textNav.setText(sheetInfo.getText());
        }

    }

    @Override
    public int getItemCount() {
        return sheetInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout navLayout;
        private ImageView iconNav;
        private TextView textNav;

        private CardView arriveLayout;
        private ImageView iconArrive;
        private TextView textArrive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            navLayout = itemView.findViewById(R.id.nav_layout);
            iconNav = itemView.findViewById(R.id.imageView_navIcon);
            textNav = itemView.findViewById(R.id.textView_instructions_nav);

            arriveLayout = itemView.findViewById(R.id.arrive_layout);
            iconArrive = itemView.findViewById(R.id.imageView_navIcon_arrive);
            textArrive = itemView.findViewById(R.id.textView_instructions_arrive);
        }
    }
}
