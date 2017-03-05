package edu.wwu.avilatstudents.journey;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    private ViewGroup transitionContainer;
    private FrameLayout buddiesLayout;
    private FrameLayout journeysLayout;
    private FrameLayout settingsLayout;
    private FrameLayout visibleLayout;
    private ActionBar actionBar;
    private SearchView mainSearch;
    private Menu mainOptionsMenu;

    private FloatingActionButton buddiesFab;
    private FloatingActionButton journeysFab;
    private FloatingActionButton settingsFab;

    ListView buddiesList;
    ExpandableHeightGridView activeJourneyCards;
    ExpandableHeightGridView invitedJourneyCards;

    String[] testBuddies = {"Mark", "Brendan", "Michael", "Tyler"};
    String[] testActiveJourneys = {"Active Journey 1", "Active Journey 2", "Active Journey 3"};
    int[] testActiveJourneysProgress = {20, 65, 70};
    String[] testInvitedJourneys = {"Invited Journey 1", "Invited Journey 2", "Invited Journey 3"};
    int[] testInvitedJourneysProgress = {20, 65, 70};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the layout for this activity
        setContentView(R.layout.activity_main);

        // set toolbar as the activity's ActionBar and hide the title
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar_main));
        actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayShowTitleEnabled(false);
        mainSearch = (SearchView) findViewById(R.id.search_main);

        // link main navigation buttons
        buddiesFab = (FloatingActionButton) findViewById(R.id.buddies_fab);
        journeysFab = (FloatingActionButton) findViewById(R.id.journeys_fab);
        settingsFab = (FloatingActionButton) findViewById(R.id.settings_fab);

        // link the layouts (switched by the nav buttons)
        transitionContainer = (ViewGroup) findViewById(R.id.transition_container);
        buddiesLayout = (FrameLayout) findViewById(R.id.buddies_layout);
        journeysLayout = (FrameLayout) findViewById(R.id.journeys_layout);
        settingsLayout = (FrameLayout) findViewById(R.id.settings_layout);

        // set current layout to journey list view (and color nav button)
        visibleLayout = journeysLayout;
        setNavButtonColorSelected(journeysFab);


        // establish listener for navigation button clicks (switch visibility of layouts)
        View.OnClickListener navFabsOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(transitionContainer);
                visibleLayout.setVisibility(View.GONE);

                switch (view.getId()) {
                    case R.id.buddies_fab:
                        actionBar.show();
                        visibleLayout = buddiesLayout;
                        setNavButtonColorSelected(buddiesFab);
                        updateActionBar(R.id.buddies_fab);
                        break;
                    case R.id.journeys_fab:
                        actionBar.show();
                        visibleLayout = journeysLayout;
                        setNavButtonColorSelected(journeysFab);
                        updateActionBar(R.id.journeys_fab);
                        break;
                    case R.id.settings_fab:
                        actionBar.hide();
                        visibleLayout = settingsLayout;
                        setNavButtonColorSelected(settingsFab);

                }
                visibleLayout.setVisibility(View.VISIBLE);
                visibleLayout.requestFocus();
            }
        };
        buddiesFab.setOnClickListener(navFabsOnClickListener);
        journeysFab.setOnClickListener(navFabsOnClickListener);
        settingsFab.setOnClickListener(navFabsOnClickListener);

        // display data in testBuddies array in view_buddies
        buddiesList = (ListView) findViewById(R.id.buddies_list);
        BuddiesListAdapter bla = new BuddiesListAdapter(this, testBuddies);
        buddiesList.setAdapter(bla);

        activeJourneyCards = (ExpandableHeightGridView) findViewById(R.id.active_journey_cards);
        JourneyListAdapter jla1 = new JourneyListAdapter(this, testActiveJourneys, testActiveJourneysProgress);
        activeJourneyCards.setAdapter(jla1);
        activeJourneyCards.setExpanded(true);

        invitedJourneyCards = (ExpandableHeightGridView) findViewById(R.id.invited_journey_cards);
        JourneyListAdapter jla2 = new JourneyListAdapter(this, testInvitedJourneys, testInvitedJourneysProgress);
        invitedJourneyCards.setAdapter(jla2);
        activeJourneyCards.setExpanded(true);

        // Intent intent = getIntent();
        // if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        //     String query = intent.getStringExtra(SearchManager.QUERY);
        //     // doMySearch(query);
        // }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mainOptionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        updateActionBar(R.id.journeys_fab);
        mainSearch.clearFocus();

        return true;
    }

    // public void transitionToJourneyInfo(View view){
    //     View journeyTitleView = findViewById(R.id.journey_title);
    //     Intent intent = new Intent(this, JourneyActivity.class);
    //     Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
    //             this,
    //             journeyTitleView,
    //             journeyTitleView.getTransitionName())
    //             .toBundle();
    //     startActivity(intent, bundle);
    // }

    public void updateActionBar(int fabId) {
        switch (fabId) {
            case R.id.buddies_fab:
                // (CHANGE WHAT mainSearch ACTUALLY DOES)
                mainSearch.setQueryHint(getResources().getString(R.string.buddies_search_hint));
                showOption(R.id.add_buddy_item);
                hideOption(R.id.new_journey_item);
                hideOption(R.id.journey_hist_item);
                break;
            case R.id.journeys_fab:
                // (CHANGE WHAT mainSearch ACTUALLY DOES)
                mainSearch.setQueryHint(getResources().getString(R.string.journeys_search_hint));
                hideOption(R.id.add_buddy_item);
                showOption(R.id.new_journey_item);
                showOption(R.id.journey_hist_item);
                break;
            case R.id.settings_fab:
        }
    }

    private void hideOption(int optId) {
        MenuItem item = mainOptionsMenu.findItem(optId);
        item.setVisible(false);
    }

    private void showOption(int optId) {
        MenuItem item = mainOptionsMenu.findItem(optId);
        item.setVisible(true);
    }

    public void setNavButtonColorSelected(FloatingActionButton navButton) {
        // reset nav buttons to original color
        buddiesFab.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(MainActivity.this, R.color.mainNavBase)));
        journeysFab.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(MainActivity.this, R.color.mainNavBase)));
        settingsFab.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(MainActivity.this, R.color.mainNavBase)));
        // set specified nav button to selected color
        navButton.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(MainActivity.this, R.color.mainNavSelected)));
    }

}
