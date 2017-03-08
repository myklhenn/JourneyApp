package edu.wwu.avilatstudents.journey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewJourneyNameActivity extends AppCompatActivity {

    EditText journeyNameInput;
    Button nextStepBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journey_name);

        journeyNameInput = (EditText) findViewById(R.id.journey_name_textinput);
        nextStepBtn = (Button) findViewById(R.id.journey_name_next_btn);

        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newJourneyName = journeyNameInput.getText().toString();

                // check validity of journey name, display error and don't move on if invalid

                // if valid, send API call to create new journey with name as "newJourneyName"

                Intent buddies = new Intent(NewJourneyNameActivity.this,
                        NewJourneyBuddiesActivity.class);
                startActivity(buddies);
                // add journey title to database
                finish();
            }
        });
    }
}
