package edu.wwu.avilatstudents.journey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class NewJourneyNameActivity extends AppCompatActivity {
    DatabaseManager dbm;
    SessionManager sessM;

    private EditText journeyNameInput;
    private Button nextStepBtn;
    private ProgressBar journeyCreationProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journey_name);
        dbm = new DatabaseManager(getApplicationContext());
        sessM = new SessionManager(getApplicationContext());
        journeyNameInput = (EditText) findViewById(R.id.journey_name_textinput);
        nextStepBtn = (Button) findViewById(R.id.journey_name_next_btn);
        // animate progress bar from 0 to 25 percent
        journeyCreationProgress = (ProgressBar) findViewById(R.id.journey_creation_progress);
        ProgressBarAnimation anim = new ProgressBarAnimation(journeyCreationProgress, 0, 25);
        anim.setDuration(1000);
        journeyCreationProgress.startAnimation(anim);

        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newJourneyName = journeyNameInput.getText().toString();

                // TODO check validity of journey name, display error and don't move on if invalid
                // TODO if valid, send API call to create new journey with name as "newJourneyName"

                Intent buddies = new Intent(NewJourneyNameActivity.this,
                        NewJourneyBuddiesActivity.class);
                String id = dbm.createJourney(sessM.getEmail(), sessM.getAuthentication(), newJourneyName);
                buddies.putExtra("jID", id);
                buddies.putExtra("titleN", newJourneyName);
                startActivity(buddies);
                // add journey title to database

                finish();
            }
        });
    }
}