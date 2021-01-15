package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService;

import org.junit.Test;

import static org.junit.Assert.*;

public class NavigationStepTest {

    private NavigationStep navigationstep = new NavigationStep(100, 20, 2, "Rechtdoor", "Step3");

    @Test
    public void getDistance() {
        assertEquals(100, navigationstep.getDistance(), 2);

    }

    @Test
    public void getDuration() {
        assertEquals(20, navigationstep.getDuration(), 2);
    }

    @Test
    public void getType() {
        assertEquals(2, navigationstep.getType());
    }

    @Test
    public void getInstruction() {
        assertEquals("Rechtdoor", navigationstep.getInstruction());
    }

    @Test
    public void getName() {
        assertEquals("Step3", navigationstep.getName());
    }
}