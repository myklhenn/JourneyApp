package edu.wwu.avilatstudents.journey;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by myklhenn on 2/27/17.
 */

public class BuddiesListAdapter extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] buddies;

    public BuddiesListAdapter(Activity context, String[] buddies) {
        super(context, R.layout.item_buddy, buddies);
        this.context = context;
        this.buddies = buddies;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_buddy, null, true);
        TextView buddyName = (TextView) rowView.findViewById(R.id.buddy_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.buddy_icon);
        buddyName.setText(buddies[pos]);
        imageView.setImageResource(R.drawable.ic_person_black_24dp);

        return rowView;
    }
}
