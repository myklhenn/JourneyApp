package edu.wwu.avilatstudents.journey;

/**
 * Created by myklhenn on 3/5/17.
 */

class BuddiesListSelectableItem {

    private String name;
    private int iconRes;
    private boolean selected;

    BuddiesListSelectableItem(String name) {
        this.name = name;
        this.iconRes = R.drawable.ic_person_black_24dp;
        this.selected = false;
    }

    BuddiesListSelectableItem(String name, int iconRes) {
        this.name = name;
        this.iconRes = iconRes;
        this.selected = false;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    int getIconRes() {
        return iconRes;
    }

    void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
