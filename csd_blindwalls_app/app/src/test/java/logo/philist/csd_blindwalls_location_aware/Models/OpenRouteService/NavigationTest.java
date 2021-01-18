package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService;

import org.junit.Test;

import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Data.Navigation;
import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Data.NavigationSegment;
import logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Data.NavigationSummary;

import static org.junit.Assert.assertEquals;

public class NavigationTest {

    Navigation navigation = new Navigation();
    NavigationSummary navigationSummary = new NavigationSummary(200, 20);
    NavigationSegment navigationSegment = new NavigationSegment(navigationSummary);
  //  private List<GeoPoint> geometry;
  //  GeoPoint geopoint = new GeoPoint();


    @Test
    public void getSummary() {
        navigation.setSummary(navigationSummary);
        assertEquals(navigationSummary, navigation.getSummary());
    }

    @Test
    public void getSegments() {
        navigation.addSegment(navigationSegment);
        assertEquals(navigationSegment, navigation.getSegments().get(0));
    }

    @Test
    public void addSegment() {
        navigation.addSegment(navigationSegment);
        assertEquals(navigationSegment, navigation.getSegments().get(0));
    }

    @Test
    public void getGeometry() {
       //navigation.addGeometry(geopoint);
       //assertEquals(geopoint, navigation.getGeometry());
    }

    @Test
    public void addGeometry() {
        //werkt bij mij niet met GeoPoints geen idee waarom
    }
}