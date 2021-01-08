package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService;

public class NavigationStep {

    private final double distance;
    private final double duration;
    private final int type;
    private final String instruction;
    private final String name;

    public NavigationStep(double distance, double duration, int type, String instruction, String name) {
        this.distance = distance;
        this.duration = duration;
        this.type = type;
        this.instruction = instruction;
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public double getDuration() {
        return duration;
    }

    public int getType() {
        return type;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getName() {
        return name;
    }
}
