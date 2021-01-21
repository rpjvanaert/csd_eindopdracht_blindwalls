package logo.philist.csd_blindwalls_location_aware.ViewModels.Blindwalls;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.BlindwallsRepository;
import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Mural;
import logo.philist.csd_blindwalls_location_aware.Models.Blindwalls.Route;
import logo.philist.csd_blindwalls_location_aware.Models.UserNotifier;
import logo.philist.csd_blindwalls_location_aware.Views.RouteListActivity;

public class RoutesViewModel extends AndroidViewModel {

    private BlindwallsRepository repos;

    private LiveData<List<Mural>> murals;
    private LiveData<List<Route>> routes;

    public RoutesViewModel(@NonNull Application application) {
        super(application);

        repos = BlindwallsRepository.getInstance(application);

        murals = repos.getMurals();

        routes = repos.getRoutes();
    }

    public LiveData<List<Mural>> getMurals() {
        return murals;
    }

    public LiveData<List<Route>> getRoutes() {
        return routes;
    }

    public void refresh(UserNotifier userNotifier){
        repos.requestMurals(userNotifier);
        repos.requestRoutes(userNotifier);
    }
}