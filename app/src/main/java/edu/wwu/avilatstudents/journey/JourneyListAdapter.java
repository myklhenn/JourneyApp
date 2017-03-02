package edu.wwu.avilatstudents.journey;

/**
 * Created by myklhenn on 2/28/17.
 */

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by myklhenn on 2/27/17.
 */

class JourneyListAdapter extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] names;
    private final int[] progresses;

    JourneyListAdapter(Activity context, String[] names, int[] progresses) {
        super(context, R.layout.item_journey, names, progresses);
        this.context = context;
        this.names = names;
        this.progresses = progresses;
    }

    @NonNull
    @Override
    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View journeyCard = inflater.inflate(R.layout.item_journey, null, true);
        TextView journeyName = (TextView) journeyCard.findViewById(R.id.journey_card_name);
        ProgressBar journeyProgress = (ProgressBar) journeyCard.findViewById(R.id.journey);
        buddyName.setText(buddies[pos]);
        imageView.setImageResource(R.drawable.ic_person_black_24dp);

        return journeyCard;
    }
}
