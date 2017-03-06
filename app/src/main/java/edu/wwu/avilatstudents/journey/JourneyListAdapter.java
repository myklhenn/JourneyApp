package edu.wwu.avilatstudents.journey;

/**
 * Created by myklhenn on 2/28/17.
 */

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

class JourneyListAdapter extends ArrayAdapter<JourneyCard> {

    private final Activity context;
    private final JourneyCard[] journeys;

    JourneyListAdapter(Activity context, JourneyCard[] journeys) {
        super(context, R.layout.item_journey, journeys);
        this.context = context;
        this.journeys = journeys;
    }

    @NonNull
    @Override
    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder journeyCard;

        if (view == null) {
            LayoutInflater inf = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.item_journey, null);
            // cache view fields into the holder
            journeyCard = new ViewHolder();
            journeyCard.journeyName = (TextView) view.findViewById(R.id.journey_card_name);
            journeyCard.journeyProgress = (ProgressBar) view.findViewById(R.id.journey_card_progress);
            journeyCard.journeyMap = (ImageView) view.findViewById(R.id.journey_card_map);
            // associate the holder with the view for later lookup
            view.setTag(journeyCard);
        } else {
            journeyCard = (ViewHolder) view.getTag();
        }

        journeyCard.journeyName.setText(this.journeys[pos].getName());
        journeyCard.journeyProgress.setProgress(this.journeys[pos].getProgress());

        return view;
    }

    @Override
    public JourneyCard getItem(int pos) {
        return this.journeys[pos];
    }
}

class ViewHolder {
    TextView journeyName;
    ProgressBar journeyProgress;
    ImageView journeyMap;
}