package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Refactor;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DirectionsRequestBody {
    @JsonProperty("instructions")
    private static final boolean instructions = false;
    @NonNull
    @JsonProperty("coordinates")
    private double[][] coordinates;
}
