package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class Navigation {

    private NavigationSummary summary;

    private List<NavigationSegment> segments;

    private List<GeoPoint> geometry;

    public Navigation(NavigationSummary summary) {
        this.geometry = new ArrayList<>();
        this.segments = new ArrayList<>();

        this.summary = summary;
    }

    public Navigation() {
        this(null);
    }

    public NavigationSummary getSummary() {
        return summary;
    }

    public void setSummary(NavigationSummary summary) {
        this.summary = summary;
    }

    public List<NavigationSegment> getSegments() {
        return segments;
    }

    public void addSegment(NavigationSegment segment){
        this.segments.add(segment);
    }

    public List<GeoPoint> getGeometry() {
        return geometry;
    }

    public void addGeometry(GeoPoint geoPoint){
        this.geometry.add(geoPoint);
    }
}
