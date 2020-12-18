package logo.philist.csd_blindwalls_location_aware.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import logo.philist.csd_blindwalls_location_aware.Models.BlindwallsRepository;
import logo.philist.csd_blindwalls_location_aware.Models.Mural;

public class MuralsViewModel extends AndroidViewModel {

    private BlindwallsRepository repos;

    private LiveData<List<Mural>> murals;

    public MuralsViewModel(@NonNull Application application) {
        super(application);

        repos = BlindwallsRepository.getInstance();

        repos.requestMurals();
        murals = repos.getMurals();
    }

    public LiveData<List<Mural>> getMurals() {
        return murals;
    }
}