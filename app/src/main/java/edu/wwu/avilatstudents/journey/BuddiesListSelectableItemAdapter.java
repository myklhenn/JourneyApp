package edu.wwu.avilatstudents.journey;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by myklhenn on 3/5/17.
 */

class BuddiesListSelectableItemAdapter extends ArrayAdapter<BuddiesListSelectableItem> {

    private final Activity context;
    private final ArrayList<BuddiesListSelectableItem> buddies;

    BuddiesListSelectableItemAdapter(Activity context, ArrayList<BuddiesListSelectableItem> buddies) {
        super(context, R.layout.item_buddy, buddies);
        this.context = context;
        this.buddies = buddies;
    }

    @NonNull
    @Override
    public View getView(final int pos, View view, @NonNull ViewGroup parent) {
        BuddiesListSelectableItemViewHolder buddyItem;

        if (view == null) {
            LayoutInflater inf = (LayoutInflater) this.context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.item_buddy_selectable, null);
            // cache view fields into the holder
            buddyItem = new BuddiesListSelectableItemViewHolder();
            buddyItem.buddyName = (TextView) view.findViewById(R.id.buddy_selectable_name);
            buddyItem.buddyIcon = (ImageView) view.findViewById(R.id.buddy_selectable_icon);
            buddyItem.selectedStateIcon = (ImageView) view.findViewById(R.id.buddy_selected_state);
            buddyItem.itemContainer = (RelativeLayout) view.findViewById(R.id.buddy_selectable_container);
            // associate the holder with the view for later lookup
            view.setTag(buddyItem);
        } else {
            buddyItem = (BuddiesListSelectableItemViewHolder) view.getTag();
        }

        buddyItem.buddyName.setText(this.buddies.get(pos).getName());
        buddyItem.buddyIcon.setImageResource(this.buddies.get(pos).getIconRes());
        if (this.buddies.get(pos).isSelected()) {
            buddyItem.selectedStateIcon.setImageResource(R.drawable.ic_done_black_24dp);
            buddyItem.itemContainer.setBackgroundColor(ContextCompat.getColor(
                    this.getContext(), R.color.itemBkgTransparentSelected));
        } else {
            buddyItem.selectedStateIcon.setImageResource(R.drawable.ic_add_black_24dp);
            buddyItem.itemContainer.setBackgroundColor(ContextCompat.getColor(
                    this.getContext(), R.color.itemBkgTransparent));
        }
        
        return view;
    }

    @Override
    public BuddiesListSelectableItem getItem(int pos) {
        return this.buddies.get(pos);
    }
}

class BuddiesListSelectableItemViewHolder {
    TextView buddyName;
    ImageView buddyIcon;
    ImageView selectedStateIcon;
    RelativeLayout itemContainer;
}
