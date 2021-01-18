package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Refactor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;

public class SimpleDirectionsResponseBodyDeserializer extends JsonDeserializer<SimpleDirectionsResponseBody> {

    @Override
    public SimpleDirectionsResponseBody deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode tree = p.getCodec().readTree(p);
        SimpleDirectionsResponseBody responseBody = new SimpleDirectionsResponseBody();

        for (JsonNode node : tree.get("features")) {
            JsonNode geometry = node.get("geometry");
            for (JsonNode coordinate : geometry.get("coordinates")) {
                double longitude = coordinate.get(0).asDouble();
                double latitude = coordinate.get(1).asDouble();
                GeoPoint geoPoint = new GeoPoint(latitude, longitude);
                responseBody.getGeoPoints().add(geoPoint);
            }
        }


        return responseBody;
    }
}
