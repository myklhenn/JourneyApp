package edu.wwu.avilatstudents.journey;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.google.maps.android.PolyUtil.decode;
import static edu.wwu.avilatstudents.journey.R.id.map;

//import com.google.android.gms.maps.model.Polyline;

public class NewJourneyLocationActivity extends FragmentActivity implements OnMapReadyCallback {
    static String stepDistance;
    static String startLocation;
    static String endLocation;
    private static int nextPath = 0;
    private GoogleMap mMap;
    ViewGroup transition_start;
    ProgressBar journeyCreationProgress;
    AppCompatButton backBtn;
    AppCompatButton pathBtn;
    AppCompatButton finishBtn;
    AppCompatButton removeStartButton;
    AppCompatButton removeEndButton;
    DatabaseManager dbm;
    SessionManager sessM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journey_location);
        transition_start = (ViewGroup) findViewById(R.id.relativeLayoutFragment);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        dbm = new DatabaseManager(getApplicationContext());
        sessM = new SessionManager(getApplicationContext());
        // animate progress bar from 50 to 75 percent
        journeyCreationProgress = (ProgressBar) findViewById(R.id.journey_creation_progress);
        ProgressBarAnimation anim = new ProgressBarAnimation(journeyCreationProgress, 50, 75);
        anim.setDuration(1000);
        journeyCreationProgress.startAnimation(anim);
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
        backBtn = (AppCompatButton) findViewById(R.id.backbutton);
        removeStartButton = (AppCompatButton) findViewById(R.id.removeStartButton);
        removeEndButton = (AppCompatButton) findViewById(R.id.removeEndButton);
        pathBtn = (AppCompatButton) findViewById(R.id.pathbutton);
        finishBtn = (AppCompatButton) findViewById(R.id.finishbutton);
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
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_place_blue))
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
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_place_blue))
                            .title("Start of your journey")
                    );
                    mMap.addMarker(new MarkerOptions()
                            .position(start_end_markers.get(1))
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_place_red))
                            .title("End of your journey")
                    );
                    String jsonString = CreateUrl(start_end_markers.get(0).longitude, start_end_markers.get(0).latitude, start_end_markers.get(1).longitude, start_end_markers.get(1).latitude);
                    new synchronizeTasks(jsonString).execute();
                }
                pathBtn.setText("Next Route");
            }
        });
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getIntent().getExtras();
                String id = bundle.getString("journeyId");
                String journeyName = bundle.getString("journeyTitle");
                dbm.finalizeTravelers(sessM.getEmail(),sessM.getAuthentication(), id);
                /*put all journey info into database*/
                dbm.updateJourney(sessM.getEmail(),sessM.getAuthentication(), id, journeyName, Double.toString(start_end_markers.get(0).latitude),
                        Double.toString(start_end_markers.get(0).longitude), Double.toString(start_end_markers.get(1).latitude), Double.toString(start_end_markers.get(1).longitude), startLocation, endLocation, stepDistance);

                finish();
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (start_end_markers.isEmpty()) {
                    start_end_markers.add(point);
                    mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_place_blue))
                            .title("Start of your journey")
                    );
                    backBtn.setVisibility(View.VISIBLE);
                    beginningJ.setVisibility(View.GONE);
                    endingJ.setVisibility(View.VISIBLE);
                } else if (start_end_markers.size() == 1) {
                    start_end_markers.add(point);
                    mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_place_red))
                            .title("End of your journey")
                    );
                    endingJ.setVisibility(View.GONE);
                    backBtn.setVisibility(View.VISIBLE);

                    pathBtn.setText("Find Route");
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
            JSONParser jParser = new JSONParser();
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

    private void drawPath(String result) {

        JSONArray routeArray;
        JSONArray routeLegs;
        JSONArray routeSteps;
        List<LatLng> list;
        String distance;
        final TextView pathDistance = (TextView) findViewById(R.id.distance);
        try {
            final JSONObject json = new JSONObject(result);
            routeArray = json.getJSONArray("routes");
            int allRoutes = routeArray.length();
            if (allRoutes == 0) {
                distance = "No valid routes available";
                pathDistance.setText(distance);
                pathDistance.setVisibility(View.VISIBLE);
            } else {
                if (nextPath == allRoutes) {
                    nextPath = 0;
                }

                Log.d("route number:", Integer.toString(allRoutes));

                routeLegs = ((JSONObject) routeArray.get(nextPath)).getJSONArray("legs");
                distance = "Your journey's distance: " + ((JSONObject) ((JSONObject) routeLegs.get(0)).get("distance")).get("text");
                String miles = "" + ((JSONObject) ((JSONObject) routeLegs.get(0)).get("distance")).get("text");
                String[] splitForSteps = miles.split("\\s+");
                int totalSteps = Integer.valueOf(splitForSteps[0]) * 2000;
                stepDistance = Integer.toString(totalSteps);
                startLocation = "" + ((JSONObject) routeLegs.get(0)).get("start_address");
                endLocation = "" + ((JSONObject) routeLegs.get(0)).get("end_address");
                pathDistance.setText(distance);
                pathDistance.setVisibility(View.VISIBLE);
                PolylineOptions options = new PolylineOptions().width(12).color(ContextCompat.getColor(
                        NewJourneyLocationActivity.this, R.color.journeyAccent)).geodesic(true);

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
}
