package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Data;

public class NavigationSummary {

    private final double distance;
    private final double duration;

    public NavigationSummary(double distance, double duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public double getDuration() {
        return duration;
    }
}
