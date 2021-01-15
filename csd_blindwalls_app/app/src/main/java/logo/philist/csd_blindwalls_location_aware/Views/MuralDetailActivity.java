package logo.philist.csd_blindwalls_location_aware.Views;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import logo.philist.csd_blindwalls_location_aware.Models.Language;
import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Mural;
import logo.philist.csd_blindwalls_location_aware.R;
import logo.philist.csd_blindwalls_location_aware.Views.Adapters.MuralImageListAdapter;

public class MuralDetailActivity extends Activity {
    public static final String TAG = MuralDetailActivity.class.getName();

    private Mural mural;

    private MaterialTextView mTextViewName;
    private MaterialTextView mTextViewAddress;
    private MaterialTextView mTextViewDate;
    private MaterialTextView mTextViewDescription;
    private MaterialTextView mTextViewCategory;
    private MaterialTextView mTextViewMaterial;
    private MaterialTextView mTextViewAuthor;

    private RecyclerView recyclerViewMuralImages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mural_detail_activity);

        mural = (Mural) getIntent().getSerializableExtra(TAG);

        mTextViewName = findViewById(R.id.mTextview_muralTitle);
        mTextViewAddress = findViewById(R.id.mTextview_muralsAddress);
        mTextViewDate = findViewById(R.id.mTextview_muralsDate);
        mTextViewDescription = findViewById(R.id.mTextview_description);
        mTextViewCategory = findViewById(R.id.mTextview_category);
        mTextViewMaterial = findViewById(R.id.mTextview_material);
        mTextViewAuthor = findViewById(R.id.mTextview_author);

        recyclerViewMuralImages = findViewById(R.id.recyclerview_muralImages);

        int lang = Language.getSystemLanguage();
        mTextViewName.setText(mural.getTitle(lang));
        mTextViewAddress.setText(mural.getAddress());
        mTextViewDate.setText(mural.getDate().toString());
        mTextViewDescription.setText(mural.getDescription(lang));
        mTextViewCategory.setText(String.format("%s%s", getResources().getString(R.string.category), mural.getCategory(lang)));
        mTextViewMaterial.setText(String.format("%s%s", getResources().getString(R.string.material), mural.getMaterial(lang)));
        mTextViewAuthor.setText(mural.getAuthor());

        recyclerViewMuralImages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMuralImages.setAdapter(new MuralImageListAdapter(this, mural.getImages()));

    }
}
