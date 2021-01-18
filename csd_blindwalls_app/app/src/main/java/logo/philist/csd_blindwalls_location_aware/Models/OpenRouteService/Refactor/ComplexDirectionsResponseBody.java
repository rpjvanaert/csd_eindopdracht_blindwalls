package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Refactor;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
@JsonDeserialize(using = logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Refactor.ComplexDirectionsResponseBodyDeserializer.class)
public class ComplexDirectionsResponseBody {
    private final List<GeoPoint> geoPoints = new ArrayList<>();
    private int distance;
}
