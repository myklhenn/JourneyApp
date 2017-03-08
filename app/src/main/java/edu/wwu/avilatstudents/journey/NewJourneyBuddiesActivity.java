package edu.wwu.avilatstudents.journey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class NewJourneyBuddiesActivity extends AppCompatActivity {

    SearchView searchBuddies;
    ListView buddiesList;
    Button nextStepBtn;
    ProgressBar journeyCreationProgress;

    ArrayList<BuddiesListSelectableItem> testBuddies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journey_buddies);

        searchBuddies = (SearchView) findViewById(R.id.search_buddies);
        nextStepBtn = (Button) findViewById(R.id.journey_buddies_next_btn);
        prepareBuddiesList();
        // animate progress bar from 25 to 50 percent
        journeyCreationProgress = (ProgressBar) findViewById(R.id.journey_creation_progress);
        ProgressBarAnimation anim = new ProgressBarAnimation(journeyCreationProgress, 25, 50);
        anim.setDuration(1000);
        journeyCreationProgress.startAnimation(anim);

        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add buddies and send to database as well as next activity
                Bundle bundle = getIntent().getExtras();
                Intent nextStep = new Intent(NewJourneyBuddiesActivity.this,
                        NewJourneyLocationActivity.class);
                nextStep.putExtra("journeyId", bundle.getString("jID"));
                nextStep.putExtra("journeyTitle", bundle.getString("titleN"));
                // add buddies to database
                startActivity(nextStep);
                finish();
            }
        });

        journeyCreationProgress.setProgress(50);
    }

    private void prepareBuddiesList() {
        testBuddies.add(new BuddiesListSelectableItem("Mark"));
        testBuddies.add(new BuddiesListSelectableItem("Brendan"));
        testBuddies.add(new BuddiesListSelectableItem("Michael"));
        testBuddies.add(new BuddiesListSelectableItem("Tyler"));
        buddiesList = (ListView) findViewById(R.id.select_buddies_list);
        final BuddiesListSelectableItemAdapter adptr = new BuddiesListSelectableItemAdapter(this, testBuddies);
        buddiesList.setAdapter(adptr);
        buddiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                BuddiesListSelectableItem buddy = (BuddiesListSelectableItem) parent.getItemAtPosition(pos);
                buddy.setSelected(!buddy.isSelected());
                adptr.notifyDataSetChanged();
            }
        });
    }
}
