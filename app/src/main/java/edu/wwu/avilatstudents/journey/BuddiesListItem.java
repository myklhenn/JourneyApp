package edu.wwu.avilatstudents.journey;

/**
 * Created by myklhenn on 3/5/17.
 */

class BuddiesListItem {

    private String name;
    private int iconRes;

    BuddiesListItem(String name) {
        this.name = name;
        this.iconRes = R.drawable.ic_person_black_24dp;
    }

    BuddiesListItem(String name, int iconRes) {
        this.name = name;
        this.iconRes = iconRes;
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

}
