package logo.philist.csd_blindwalls_location_aware.ViewModels.Blindwalls;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.BlindwallsRepository;
import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Mural;
import logo.philist.csd_blindwalls_location_aware.Models.UserNotifier;

public class MuralsViewModel extends AndroidViewModel {

    private BlindwallsRepository repos;

    private LiveData<List<Mural>> murals;

    public MuralsViewModel(@NonNull Application application) {
        super(application);

        repos = BlindwallsRepository.getInstance(application);

        murals = repos.getMurals();
    }

    public LiveData<List<Mural>> getMurals() {
        return murals;
    }

    public void refresh(UserNotifier userNotifier){
        repos.requestMurals(userNotifier);
    }
}
