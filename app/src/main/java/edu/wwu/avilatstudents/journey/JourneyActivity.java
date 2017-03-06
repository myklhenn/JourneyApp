package edu.wwu.avilatstudents.journey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JourneyActivity extends AppCompatActivity {

    TextView journeyTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);

        journeyTitleView = (TextView) findViewById(R.id.journey_title);
        String journeyTitle = getIntent().getExtras().getString("journeyName");
        if (journeyTitle != null)
            journeyTitleView.setText(journeyTitle);
    }
}
