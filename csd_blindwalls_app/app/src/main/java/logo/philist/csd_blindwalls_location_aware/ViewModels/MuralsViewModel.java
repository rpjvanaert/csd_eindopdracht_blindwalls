package logo.philist.csd_blindwalls_location_aware.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import logo.philist.csd_blindwalls_location_aware.Models.BlindwallsRepository;
import logo.philist.csd_blindwalls_location_aware.Models.Mural;
import logo.philist.csd_blindwalls_location_aware.Models.Route;

public class MuralsViewModel extends AndroidViewModel {

    private BlindwallsRepository repos;

    private LiveData<List<Mural>> murals;
    private LiveData<List<Route>> routes;

    public MuralsViewModel(@NonNull Application application) {
        super(application);

        repos = BlindwallsRepository.getInstance();

        repos.requestMurals();
        murals = repos.getMurals();

        repos.requestRoutes();
        routes = repos.getRoutes();
    }

    public LiveData<List<Mural>> getMurals() {
        return murals;
    }

    public LiveData<List<Route>> getRoutes() {
        return routes;
    }
}
