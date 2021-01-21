package logo.philist.csd_blindwalls_location_aware.Views.Adapters;

import android.content.Context;
import android.util.Log;
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

import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Mural;
import logo.philist.csd_blindwalls_location_aware.R;

public class NavigationStepsAdapter extends RecyclerView.Adapter<NavigationStepsAdapter.ViewHolder> {

    private List<NavigationSheetInfo> sheetInfo;
    private Context context;

    public NavigationStepsAdapter(List<NavigationSheetInfo> sheetInfo) {
        this.sheetInfo = sheetInfo;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.nav_step_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NavigationSheetInfo sheetInfo = this.sheetInfo.get(position);

        Log.i(NavigationStepsAdapter.class.getName(), "sheet info: " + sheetInfo.getText() + " @@@ " + sheetInfo.getType());

        holder.textNav.setText(sheetInfo.getText());
        if (sheetInfo.getType() == Mural.TYPE){
            holder.navLayout.setBackgroundColor(context.getColor(R.color.colorPrimary));
            holder.textNav.setTextColor(context.getColor(R.color.colorWhite));
            holder.iconNav.setImageResource(R.drawable.ic_img);
        } else {
            holder.navLayout.setBackgroundColor(context.getColor(R.color.colorWhite));
            holder.textNav.setTextColor(context.getColor(R.color.colorPrimary));
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            navLayout = itemView.findViewById(R.id.nav_layout);
            iconNav = itemView.findViewById(R.id.imageView_navIcon_nav);
            textNav = itemView.findViewById(R.id.textView_instructions_nav);
        }
    }
}
