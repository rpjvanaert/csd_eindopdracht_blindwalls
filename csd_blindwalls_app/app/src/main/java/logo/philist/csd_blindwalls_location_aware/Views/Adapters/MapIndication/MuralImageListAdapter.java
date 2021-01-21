package logo.philist.csd_blindwalls_location_aware.Views.Adapters.MapIndication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import logo.philist.csd_blindwalls_location_aware.R;

public class MuralImageListAdapter extends RecyclerView.Adapter<MuralImageListAdapter.ImageViewHolder> {

    private Context context;
    private List<String> imageUrls;

    public MuralImageListAdapter(Context context, List<String> imageUrls){
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View imageView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.image_item_mural, parent, false);
        ImageViewHolder viewHolder = new ImageViewHolder(imageView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(context)
                .load(imageUrls.get(position))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview_mural);
        }
    }
}
