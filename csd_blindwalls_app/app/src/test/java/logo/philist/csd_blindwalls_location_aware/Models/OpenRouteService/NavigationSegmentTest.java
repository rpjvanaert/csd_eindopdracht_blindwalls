package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class NavigationSegmentTest {

    NavigationSummary navigationSummary = new NavigationSummary(200, 40);
    NavigationSegment navigationSegment = new NavigationSegment(navigationSummary);
    NavigationStep navigationStep = new NavigationStep(20, 20, 2, "Rechtdoor", "Step2");


    ArrayList steps = new ArrayList<NavigationStep>();

    @Test
    public void addStep() {

    }

    @Test
    public void getSummary() {
        assertEquals(navigationSummary, navigationSegment.getSummary());

    }

    @Test
    public void getSteps() {
        navigationSegment.addStep(navigationStep);
        assertEquals(navigationStep, navigationSegment.getSteps());
    }
}