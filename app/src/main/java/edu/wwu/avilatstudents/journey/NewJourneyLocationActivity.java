package edu.wwu.avilatstudents.journey;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import static edu.wwu.avilatstudents.journey.R.id.map;
import static com.google.maps.android.PolyUtil.decode;

public class NewJourneyLocationActivity extends FragmentActivity implements OnMapReadyCallback {
    private static int nextPath = 0;
    private GoogleMap mMap;
    ViewGroup transition_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journey_location);
        transition_start = (ViewGroup) findViewById(R.id.frameLayoutFragment);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<LatLng> start_end_markers = new ArrayList<>();
        SetMarkers(start_end_markers);
    }
    public void SetMarkers(final ArrayList<LatLng> start_end_markers) {
        final TextView beginningJ = (TextView) findViewById(R.id.chooseOne);
        final TextView endingJ = (TextView) findViewById(R.id.chooseTwo);
        final Button backBtn = (Button) findViewById(R.id.backbutton);
        final Button pathBtn = (Button) findViewById(R.id.pathbutton);
        final Button finishBtn = (Button) findViewById(R.id.finishbutton);
        final TextView pathDistance = (TextView) findViewById(R.id.distance);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start_end_markers.size() == 1) {
                    mMap.clear();
                    start_end_markers.clear();
                    backBtn.setVisibility(View.GONE);
                    beginningJ.setVisibility(View.VISIBLE);
                    endingJ.setVisibility(View.GONE);
                } else {
                    mMap.clear();
                    start_end_markers.remove(1);
                    pathDistance.setVisibility(View.GONE);
                    endingJ.setVisibility(View.VISIBLE);
                    finishBtn.setVisibility(View.GONE);
                    pathBtn.setVisibility(View.GONE);
                    mMap.addMarker(new MarkerOptions()
                            .position(start_end_markers.get(0))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.begin_point_official))
                            .title("Start of your journey")
                    );
                }
            }
        });
        pathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start_end_markers.size() == 2) {
                    mMap.clear();
                    finishBtn.setVisibility(View.VISIBLE);
                    mMap.addMarker(new MarkerOptions()
                            .position(start_end_markers.get(0))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.begin_point_official))
                            .title("Start of your journey")
                    );
                    mMap.addMarker(new MarkerOptions()
                            .position(start_end_markers.get(1))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_point_official))
                            .title("End of your journey")
                    );
                    String jsonString = CreateUrl(start_end_markers.get(0).longitude, start_end_markers.get(0).latitude, start_end_markers.get(1).longitude, start_end_markers.get(1).latitude);
                    new synchronizeTasks(jsonString).execute();
                }
            }
        });
        /*
        finishBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
             }
        });
        */
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (start_end_markers.isEmpty()) {
                    start_end_markers.add(point);
                    mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.begin_point_official))
                            .title("Start of your journey")
                    );
                    backBtn.setVisibility(View.VISIBLE);
                    beginningJ.setVisibility(View.GONE);
                    endingJ.setVisibility(View.VISIBLE);
                } else if (start_end_markers.size() == 1) {
                    start_end_markers.add(point);
                    mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_point_official))
                            .title("End of your journey")
                    );
                    endingJ.setVisibility(View.GONE);
                    backBtn.setVisibility(View.VISIBLE);
                    pathBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }
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

    private class synchronizeTasks extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;
        String url;
        synchronizeTasks(String urlPass){
            url = urlPass;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewJourneyLocationActivity.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            JSONPARSER jParser = new JSONPARSER();
            return jParser.getJSONFromUrl(url);
        }
        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            progressDialog.dismiss();
            if (result!=null) {
                drawPath(result);
            }
        }
    }

    /*
    private class synchronizeDrawing extends AsyncTask<Void, Void, List<LatLng>> {
        synchronizeDrawing(String urlPass){

        }

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected List<LatLng> doInBackground(Void... params) {
            List<LatLng> polyList =
            return polyList;
        }
        @Override
        protected void onPostExecute(List<LatLng> result) {

        }
    }
    */

    private void drawPath(String result) {

        JSONArray routeArray;
        JSONArray routeLegs;
        JSONArray routeSteps;
        List<LatLng> list;
        String distance;
        final TextView pathDistance = (TextView) findViewById(R.id.distance);
        try {

            /*
             route data in json objects/arrays
             */
            final JSONObject json = new JSONObject(result);
            routeArray = json.getJSONArray("routes");
            int allRoutes = routeArray.length();
            System.out.println(allRoutes);
            if (allRoutes == 0) {
                distance = "No valid routes available";
                pathDistance.setText(distance);
                pathDistance.setVisibility(View.VISIBLE);
            } else {
                if (nextPath == allRoutes) {
                    nextPath = 0;
                }
                System.out.println(nextPath);
                routeLegs = ((JSONObject) routeArray.get(nextPath)).getJSONArray("legs");
                distance = "Path Distance is " + ((JSONObject) ((JSONObject) routeLegs.get(0)).get("distance")).get("text");
                pathDistance.setText(distance);
                pathDistance.setVisibility(View.VISIBLE);
                PolylineOptions options = new PolylineOptions().width(12).color(Color.rgb(255, 102, 178)).geodesic(true);

                // shit performance right now
                routeSteps = ((JSONObject) routeLegs.get(0)).getJSONArray("steps");
                for (int k = 0; k < routeSteps.length(); k++) {
                    String polyline = (String) ((JSONObject) ((JSONObject) routeSteps.get(k)).get("polyline")).get("points");

                    list = decode(polyline);
                    for (int z = 0; z < list.size() - 1; z++) {
                        LatLng currentPoint = list.get(z);
                        options.add(currentPoint);
                        LatLng nextPoint = list.get(z + 1);
                        options.add(nextPoint);
                    }
                }
                mMap.addPolyline(options);
                nextPath++;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*
    private void SendJSONDatabase(double start_lat, double start_lng, double end_lat, double end_lng)
    {
        String token_authentication = "";
        String title = "";
        double start_latitude = 0.0;
        double start_longitude = 0.0;
        double end_latitude = 0.0;
        double end_longitude = 0.0;
        String start_location = "";
        String end_location = "";
    }
    */
}
