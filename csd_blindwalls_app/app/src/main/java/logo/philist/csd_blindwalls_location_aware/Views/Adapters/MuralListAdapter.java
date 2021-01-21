package logo.philist.csd_blindwalls_location_aware.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import logo.philist.csd_blindwalls_location_aware.Models.Language;
import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Mural;
import logo.philist.csd_blindwalls_location_aware.R;

public class MuralListAdapter extends RecyclerView.Adapter<MuralListAdapter.ViewHolder> implements Observer<List<Mural>> {

    private List<Mural> murals;
    private Context context;
    private OnItemClickListener clickListener;

    public MuralListAdapter(Context context, OnItemClickListener clickListener){
        this.murals = new ArrayList<>();
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemviewMural = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_mural, parent, false);
        return new ViewHolder(itemviewMural);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mural mural = murals.get(position);

        Glide.with(context)
                .load(mural.getImages().get(0))
                .into(holder.imagePreview);

        int language = Language.getSystemLanguage();
        holder.mTextviewNameMural.setText(mural.getTitle(language));
        holder.mTextviewAddressMural.setText(mural.getAddress());
    }

    @Override
    public int getItemCount() {
        return murals.size();
    }

    @Override
    public void onChanged(List<Mural> murals) {
        this.murals = murals;
        notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imagePreview;
        private MaterialTextView mTextviewNameMural;
        private MaterialTextView mTextviewAddressMural;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            imagePreview = itemView.findViewById(R.id.imageView_previewMural);
            mTextviewNameMural = itemView.findViewById(R.id.mTextview_nameMural);
            mTextviewAddressMural = itemView.findViewById(R.id.mTextview_addressMural);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            clickListener.onItemClick(clickedPosition);
        }
    }
}
