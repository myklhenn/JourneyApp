package edu.wwu.avilatstudents.journey;

/**
 * Created by myklhenn on 3/5/17.
 */

class JourneyListItem {

    private String name;
    private int progress;
    private int mapRes;

    JourneyListItem(String name, int progress) {
        this.name = name;
        this.progress = progress;
        this.mapRes = R.drawable.ic_near_me_black_24dp;
    }

    public JourneyListItem(String name, int progress, int mapRes) {
        this.name = name;
        this.progress = progress;
        this.mapRes = mapRes;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    int getProgress() {
        return progress;
    }

    void setProgress(int progress) {
        this.progress = progress;
    }

    public int getMapRes() {
        return mapRes;
    }

    public void setMapRes(int mapRes) {
        this.mapRes = mapRes;
    }
}
