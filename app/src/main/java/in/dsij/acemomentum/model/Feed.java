package in.dsij.acemomentum.model;

/**
 * Created by Vikas on 8/17/2017.
 */

public class Feed {

    private String headline;
    private String summery;
    private String time;
    private boolean latest;

    public Feed(String headline, String summery, String time, boolean latest) {
        this.headline = headline;
        this.summery = summery;
        this.time = time;
        this.latest=latest;
    }

    public String getHeadline() {
        return headline;
    }

    public Feed setHeadline(String headline) {
        this.headline = headline;
        return this;
    }

    public String getSummery() {
        return summery;
    }

    public Feed setSummery(String summery) {
        this.summery = summery;
        return this;
    }

    public String getTime() {
        return time;
    }

    public Feed setTime(String time) {
        this.time = time;
        return this;
    }

    public boolean isLatest() {
        return latest;
    }

    public Feed setLatest(boolean latest) {
        this.latest = latest;
        return this;
    }
}
