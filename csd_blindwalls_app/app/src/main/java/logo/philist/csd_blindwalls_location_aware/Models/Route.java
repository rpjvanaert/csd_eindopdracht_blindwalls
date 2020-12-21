package logo.philist.csd_blindwalls_location_aware.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Route  implements Serializable {

    private String name;

    private String time;

    private String distance;

    private String type;

    private List<Integer> muralsId;

    public Route(String name, String time, String distance, String type) {
        this.name = name;
        this.time = time;
        this.distance = distance;
        this.type = type;
        this.muralsId = new ArrayList<>();
    }

    public void setMuralId(int index, int muralId){
        this.muralsId.add(index, muralId);
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDistance() {
        return distance;
    }

    public String getType(){
        return type;
    }

    public List<Integer> getMuralsId() {
        return muralsId;
    }

    public List<Mural> getMurals(List<Mural> murals){
        List<Mural> routeMurals = new ArrayList<>();

        for (int id : this.muralsId){
            for (Mural eachMural : murals){
                if (eachMural.getId() == id){
                    routeMurals.add(eachMural);
                    break;
                }
            }
        }

        return routeMurals;
    }
}
