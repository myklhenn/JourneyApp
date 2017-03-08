package edu.wwu.avilatstudents.journey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class NewJourneyBuddiesActivity extends AppCompatActivity {

    SearchView searchBuddies;
    ListView buddiesList;
    Button nextStepBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journey_buddies);

        searchBuddies = (SearchView) findViewById(R.id.search_buddies);
        buddiesList = (ListView) findViewById(R.id.choose_buddies_list);
        nextStepBtn = (Button) findViewById(R.id.journey_buddies_next_btn);
        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add buddies and send to database as well as next activity

                Intent nextStep = new Intent(NewJourneyBuddiesActivity.this,
                        NewJourneyLocationActivity.class);
                // add buddies to database
                startActivity(nextStep);
                finish();
            }
        });
    }
}
