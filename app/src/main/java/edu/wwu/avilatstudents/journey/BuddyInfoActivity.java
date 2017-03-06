package edu.wwu.avilatstudents.journey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BuddyInfoActivity extends AppCompatActivity {

    TextView buddyInfoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy_info);

        buddyInfoName = (TextView) findViewById(R.id.buddy_info_name);
        String buddyName = getIntent().getExtras().getString("buddyName");
        if (buddyName != null)
            buddyInfoName.setText(buddyName);
    }
}
