package edu.wwu.avilatstudents.journey;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import static edu.wwu.avilatstudents.journey.R.id.map;
import static com.google.maps.android.PolyUtil.decode;

import com.google.android.gms.maps.model.PolylineOptions;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class JourneyActivity extends FragmentActivity implements OnMapReadyCallback {

    TextView journeyTitleView;
    GoogleMap mMap;
    ViewGroup transition_start;

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
    }
}