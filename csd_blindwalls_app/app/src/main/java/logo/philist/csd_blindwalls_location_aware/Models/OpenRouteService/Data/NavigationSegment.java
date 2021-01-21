package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Data;

import java.util.ArrayList;
import java.util.List;

public class NavigationSegment {

    private final NavigationSummary summary;
    private final List<NavigationStep> steps;

    public NavigationSegment(NavigationSummary summary) {
        this.summary = summary;
        this.steps = new ArrayList<>();
    }

    public void addStep(NavigationStep step){
        this.steps.add(step);
    }

    public NavigationSummary getSummary() {
        return summary;
    }

    public List<NavigationStep> getSteps() {
        return steps;
    }

    public List<NavigationSheetInfo> getSheetInfo() {
        List<NavigationSheetInfo> sheetInfo = new ArrayList<>();

        for (NavigationStep step : this.steps){
            sheetInfo.add(step.getSheetInfo());
        }
        return sheetInfo;
    }
}
