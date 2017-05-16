package edu.wwu.avilatstudents.journey;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static edu.wwu.avilatstudents.journey.R.id.end;
import static edu.wwu.avilatstudents.journey.R.id.map;
import static com.google.maps.android.PolyUtil.decode;

import com.google.android.gms.maps.model.PolylineOptions;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class JourneyActivity extends FragmentActivity implements OnMapReadyCallback {
    private static int nextPath = 0;
    RouteGenerator rg;
    TextView journeyTitleView;
    GoogleMap mMap;
    ViewGroup transition_start;
    DatabaseManager dm;
    SessionManager sessM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);
        transition_start = (ViewGroup) findViewById(R.id.frameLayoutFragment);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        journeyTitleView = (TextView) findViewById(R.id.journey_title);
        String journeyTitle = getIntent().getExtras().getString("journeyName");
        if (journeyTitle != null) {
            journeyTitleView.setText(journeyTitle);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        JSONObject UsersJourneys;
        UsersJourneys = dm.getJourneyInfo(sessM.getEmail(), sessM.getAuthentication(), "50");
        /*add start marker camera positioning*/
        try {
            LatLng startMarker = new LatLng(UsersJourneys.getDouble("start_latitude"), UsersJourneys.getDouble("start_longitude"));
            LatLng endMarker = new LatLng(UsersJourneys.getDouble("end_latitude"), UsersJourneys.getDouble("end_longitude"));
            mMap.addMarker(new MarkerOptions()
                    .position(startMarker)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_place_blue))
                    .title("Start of your journey"));
            mMap.addMarker(new MarkerOptions()
                    .position(endMarker)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_place_red))
                    .title("End of your journey"));

            /*draw the path again on new map*/
            String JsonUrl = rg.CreateUrl(UsersJourneys.getDouble("start_latitude"), UsersJourneys.getDouble("start_longitutde"),
                    UsersJourneys.getDouble("end_latitude"), UsersJourneys.getDouble("end_longitude"));
            new synchronizeTasks(JsonUrl).execute();
        } catch (JSONException e) {
        }
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
            progressDialog = new ProgressDialog(JourneyActivity.this);
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
                //stepDistance = Integer.toString(totalSteps);
                //startLocation = "" + ((JSONObject) routeLegs.get(0)).get("start_address");
                //endLocation = "" + ((JSONObject) routeLegs.get(0)).get("end_address");
                pathDistance.setText(distance);
                pathDistance.setVisibility(View.VISIBLE);
                PolylineOptions options = new PolylineOptions().width(12).color(ContextCompat.getColor(
                        JourneyActivity.this, R.color.journeyAccent)).geodesic(true);

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