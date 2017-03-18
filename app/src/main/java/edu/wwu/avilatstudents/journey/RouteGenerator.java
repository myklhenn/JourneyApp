package edu.wwu.avilatstudents.journey;

/**
 * Created by brendanbaalke on 3/17/17.
 */

public class RouteGenerator {
    public String CreateUrl(double sourceLng, double sourceLat, double destinationLng, double destinationLat) {
        StringBuilder jsonString = new StringBuilder();

        jsonString.append("https://maps.googleapis.com/maps/api/directions/json");
        jsonString.append("?origin=");// source
        jsonString.append(Double.toString(sourceLat));
        jsonString.append(",");
        jsonString.append(Double.toString(sourceLng));
        jsonString.append("&destination=");// destination
        jsonString.append(Double.toString(destinationLat));
        jsonString.append(",");
        jsonString.append(Double.toString(destinationLng));
        // change to walking later
        jsonString.append("&sensor=false&mode="+"walking"+"&alternatives=true&language="+"en");
        return jsonString.toString();
    }
}
