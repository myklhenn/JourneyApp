package edu.wwu.avilatstudents.journey;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    /*private SectionsPagerAdapter sectionsPagerAdapter;
    private MainContentViewPager viewPager;
    TabLayout tabLayout;*/

    ViewGroup transitionContainer;
    FrameLayout buddiesLayout;
    FrameLayout journeysLayout;
    FrameLayout settingsLayout;
    FrameLayout visibleLayout;

    FloatingActionButton buddiesFab;
    FloatingActionButton journeysFab;
    FloatingActionButton settingsFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transitionContainer = (ViewGroup) findViewById(R.id.transition_container);
        buddiesLayout = (FrameLayout) findViewById(R.id.buddies_layout);
        journeysLayout = (FrameLayout) findViewById(R.id.journeys_layout);
        settingsLayout = (FrameLayout) findViewById(R.id.settings_layout);
        visibleLayout = journeysLayout;

        buddiesFab = (FloatingActionButton) findViewById(R.id.buddies_fab);
        journeysFab = (FloatingActionButton) findViewById(R.id.journeys_fab);
        settingsFab = (FloatingActionButton) findViewById(R.id.settings_fab);


        View.OnClickListener navFabsOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(transitionContainer);
                visibleLayout.setVisibility(View.GONE);
                switch(view.getId()){
                    case R.id.buddies_fab:
                        buddiesLayout.setVisibility(View.VISIBLE);
                        visibleLayout = buddiesLayout;
                        break;
                    case R.id.journeys_fab:
                        journeysLayout.setVisibility(View.VISIBLE);
                        visibleLayout = journeysLayout;
                        break;
                    case R.id.settings_fab:
                        settingsLayout.setVisibility(View.VISIBLE);
                        visibleLayout = settingsLayout;
                }
            }
        };

        buddiesFab.setOnClickListener(navFabsOnClickListener);
        journeysFab.setOnClickListener(navFabsOnClickListener);
        settingsFab.setOnClickListener(navFabsOnClickListener);
    }

    public void transitionToJourneyInfo(View view){
        View journeyTitleView = findViewById(R.id.journey_title);
        Intent intent = new Intent(this, JourneyInfo.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                this,
                journeyTitleView,
                journeyTitleView.getTransitionName())
                .toBundle();
        startActivity(intent, bundle);
    }
}
