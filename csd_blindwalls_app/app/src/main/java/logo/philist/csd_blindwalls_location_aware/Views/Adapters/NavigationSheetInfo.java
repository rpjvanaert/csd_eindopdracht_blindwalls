package logo.philist.csd_blindwalls_location_aware.Views.Adapters;

public class NavigationSheetInfo {

    private String text;
    private int type;

    public NavigationSheetInfo(String text, int type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }
}
