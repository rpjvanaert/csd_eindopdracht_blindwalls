package logo.philist.csd_blindwalls_location_aware.Models.OpenRouteService.Refactor;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;

public class ComplexDirectionsResponseBodyDeserializer extends JsonDeserializer<ComplexDirectionsResponseBody> {
    /**
     * @see <a href="https://github.com/GIScience/openrouteservice-docs#geometry-decoding">source</a>
     */
    private static JSONArray decodeGeometry(String encodedGeometry, boolean inclElevation) {
        JSONArray geometry = new JSONArray();
        int len = encodedGeometry.length();
        int index = 0;
        int lat = 0;
        int lng = 0;
        int ele = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedGeometry.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedGeometry.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);


            if (inclElevation) {
                result = 1;
                shift = 0;
                do {
                    b = encodedGeometry.charAt(index++) - 63 - 1;
                    result += b << shift;
                    shift += 5;
                } while (b >= 0x1f);
                ele += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);
            }

            JSONArray location = new JSONArray();
            try {
                location.put(lat / 1E5);
                location.put(lng / 1E5);
                if (inclElevation) {
                    location.put((float) (ele / 100));
                }
                geometry.put(location);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return geometry;
    }

    @Override
    public ComplexDirectionsResponseBody deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode tree = p.getCodec().readTree(p);
        ComplexDirectionsResponseBody responseBody = new ComplexDirectionsResponseBody();
        for (JsonNode node : tree.get("routes")) {
            responseBody.setDistance(node.get("summary").get("distance").asInt());
            String encodedGeometry = node.get("geometry").asText();
            JSONArray geometryArray = decodeGeometry(encodedGeometry, false);
            for (int i = 0; i < geometryArray.length(); i++) {
                try {
                    JSONArray coordinates = geometryArray.getJSONArray(i);
                    responseBody.getGeoPoints().add(new GeoPoint(coordinates.getDouble(0), coordinates.getDouble(1)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseBody;
    }

}
