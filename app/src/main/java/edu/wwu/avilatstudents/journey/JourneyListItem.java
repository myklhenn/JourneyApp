package edu.wwu.avilatstudents.journey;

/**
 * Created by myklhenn on 3/5/17.
 */

class JourneyListItem {

    private String name;
    private int progress;

    JourneyListItem(String name, int progress) {
        this.name = name;
        this.progress = progress;
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

}
