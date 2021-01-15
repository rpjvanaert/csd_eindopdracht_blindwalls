package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService;

import org.junit.Test;

import static org.junit.Assert.*;

public class NavigationSummaryTest {

    NavigationSummary navigationSummary = new NavigationSummary(50, 20);

    @Test
    public void getDistance() {
        assertEquals(50, navigationSummary.getDistance(), 2);
    }

    @Test
    public void getDuration() {
        assertEquals(20, navigationSummary.getDuration(), 2);
    }
}