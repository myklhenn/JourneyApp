package edu.wwu.avilatstudents.journey;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by myklhenn on 2/27/17.
 */

class BuddiesListItemAdapter extends ArrayAdapter<BuddiesListItem>{

    private final Activity context;
    private final BuddiesListItem[] buddies;

    BuddiesListItemAdapter(Activity context, BuddiesListItem[] buddies) {
        super(context, R.layout.item_buddy, buddies);
        this.context = context;
        this.buddies = buddies;
    }

    @NonNull
    @Override
    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        //LayoutInflater inflater = context.getLayoutInflater();
        BuddiesListItemViewHolder buddyItem;

        if (view == null) {
            LayoutInflater inf = (LayoutInflater) this.context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.item_buddy, null);
            // cache view fields into the holder
            buddyItem = new BuddiesListItemViewHolder();
            buddyItem.buddyName = (TextView) view.findViewById(R.id.buddy_name);
            buddyItem.buddyIcon = (ImageView) view.findViewById(R.id.buddy_icon);
            // associate the holder with the view for later lookup
            view.setTag(buddyItem);
        } else {
            buddyItem = (BuddiesListItemViewHolder) view.getTag();
        }

        buddyItem.buddyName.setText(this.buddies[pos].getName());
        buddyItem.buddyIcon.setImageResource(this.buddies[pos].getIconRes());

        return view;
    }

    @Override
    public BuddiesListItem getItem(int pos) {
        return this.buddies[pos];
    }
}

class BuddiesListItemViewHolder {
    TextView buddyName;
    ImageView buddyIcon;
}
