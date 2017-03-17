package edu.wwu.avilatstudents.journey;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;


public class JourneyActivity extends FragmentActivity implements OnMapReadyCallback {

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
        // check whether this is all journeys or specific
        UsersJourneys = dm.getJourneys(sessM.getEmail(), sessM.getAuthentication());
        // add start marker camera positioning
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

        } catch (JSONException e)
        {
        }
    }
}