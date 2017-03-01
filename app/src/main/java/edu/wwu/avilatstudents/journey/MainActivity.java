package edu.wwu.avilatstudents.journey;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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

    String[] testBuddies = {"Mark", "Brendan", "Michael", "Tyler"};

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
        mainSearch.setQueryHint(getResources().getString(R.string.journeys_search_hint));
        visibleLayout.requestFocus();

        // establish listener for navigation button clicks (switch visibility of layouts)
        View.OnClickListener navFabsOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(transitionContainer);
                visibleLayout.setVisibility(View.GONE);
                actionBar.show();
                // reset nav buttons to base color (selected color applied appropriately in switch)
                resetNavButtonColor();

                switch (view.getId()) {
                    case R.id.buddies_fab:
                        setNavButtonColorSelected(buddiesFab);
                        updateActionBar(R.id.buddies_fab);
                        buddiesLayout.setVisibility(View.VISIBLE);
                        visibleLayout = buddiesLayout;
                        visibleLayout.requestFocus();
                        break;
                    case R.id.journeys_fab:
                        setNavButtonColorSelected(journeysFab);
                        updateActionBar(R.id.journeys_fab);
                        journeysLayout.setVisibility(View.VISIBLE);
                        visibleLayout = journeysLayout;
                        visibleLayout.requestFocus();
                        break;
                    case R.id.settings_fab:
                        setNavButtonColorSelected(settingsFab);
                        actionBar.hide();
                        settingsLayout.setVisibility(View.VISIBLE);
                        visibleLayout = settingsLayout;
                        visibleLayout.requestFocus();
                }
            }
        };
        buddiesFab.setOnClickListener(navFabsOnClickListener);
        journeysFab.setOnClickListener(navFabsOnClickListener);
        settingsFab.setOnClickListener(navFabsOnClickListener);

        // display data in testBuddies array in view_buddies
        buddiesList = (ListView) findViewById(R.id.buddies_list);
        BuddiesListAdapter bla = new BuddiesListAdapter(this, testBuddies);
        buddiesList.setAdapter(bla);

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
        // inflate menu to add items to action bar if it is present
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buddies, menu);

        return true;
    }

    private void updateOptionsMenu() {

    }

    public void transitionToJourneyInfo(View view){
        View journeyTitleView = findViewById(R.id.journey_title);
        Intent intent = new Intent(this, JourneyActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                this,
                journeyTitleView,
                journeyTitleView.getTransitionName())
                .toBundle();
        startActivity(intent, bundle);
    }

    public void updateActionBar(int fabId) {
        switch (fabId) {
            case R.id.buddies_fab:
                mainSearch.setQueryHint(getResources().getString(R.string.buddies_search_hint));
                // (CHANGE WHAT mainSearch ACTUALLY DOES)
                break;
            case R.id.journeys_fab:
                mainSearch.setQueryHint(getResources().getString(R.string.journeys_search_hint));
                // (CHANGE WHAT mainSearch ACTUALLY DOES)
                break;
            case R.id.settings_fab:
        }



        if (mainOptionsMenu != null)
            onPrepareOptionsMenu(mainOptionsMenu);
    }

    public void resetNavButtonColor() {
        buddiesFab.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(MainActivity.this, R.color.mainNavBase)));
        journeysFab.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(MainActivity.this, R.color.mainNavBase)));
        settingsFab.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(MainActivity.this, R.color.mainNavBase)));
    }

    public void setNavButtonColorSelected(FloatingActionButton navButton) {
        navButton.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(MainActivity.this, R.color.mainNavSelected)));
    }

}
