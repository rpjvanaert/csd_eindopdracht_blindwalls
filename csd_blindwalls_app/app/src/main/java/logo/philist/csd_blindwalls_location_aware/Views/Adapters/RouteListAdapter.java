package logo.philist.csd_blindwalls_location_aware.Views.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import logo.philist.csd_blindwalls_location_aware.Models.Language;
import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Route;
import logo.philist.csd_blindwalls_location_aware.R;
import logo.philist.csd_blindwalls_location_aware.Views.Interfaces.OnItemClickListener;

import static android.content.ContentValues.TAG;

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.ViewHolder> implements Observer<List<Route>> {

    private List<Route> routes;
    private OnItemClickListener clickListener;

    public RouteListAdapter(OnItemClickListener clickListener){
        this.routes = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @Override
    public void onChanged(List<Route> routes) {
        this.routes = routes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemviewRoute = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_route, parent, false);
        return new ViewHolder((itemviewRoute));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Route route = routes.get(position);

        int language = Language.getSystemLanguage();
        holder.mTextViewName.setText(route.getName());
        holder.mTextViewDistance.setText(route.getDistance());
        holder.mTextViewTimeAndType.setText(route.getTime() + " " + route.getType());

    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MaterialTextView mTextViewName;
        private MaterialTextView mTextViewDistance;
        private MaterialTextView mTextViewTimeAndType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTextViewName = itemView.findViewById(R.id.mTextview_routeName);
            mTextViewDistance = itemView.findViewById(R.id.mTextview_distance);
            mTextViewTimeAndType = itemView.findViewById(R.id.mTextview_timeAndType);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            int clickedPosition = getAdapterPosition();
            clickListener.onItemClick(clickedPosition);
        }
    }
}
