package edu.wwu.avilatstudents.journey;

/**
 * Created by myklhenn on 2/28/17.
 */

public class JourneyCard {

    private String name;
    private int progress;

    public JourneyCard(String name, int progress) {
        this.name = name;
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
