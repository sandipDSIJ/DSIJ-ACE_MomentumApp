package in.dsij.acemomentum.db;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by vikas on 2/1/2018.
 */

public class DbTrackerColumn extends RealmObject {
    public static final String TRACKER_CALLS = "TrackerCalls";
    public static final String COLUMN_NAME = "ColumnName";
    public static final String COLUMN_ID = "ColumnId";

    @PrimaryKey
    private long ColumnId;
    private RealmList<DbTrackerRow> TrackerCalls;
    private String ColumnName;

    public RealmList<DbTrackerRow> getTrackerCalls() {
        return TrackerCalls;
    }

    public DbTrackerColumn setTrackerCalls(RealmList<DbTrackerRow> trackerCalls) {
        TrackerCalls = trackerCalls;
        return this;
    }

    public String getColumnName() {
        return ColumnName;
    }

    public DbTrackerColumn setColumnName(String columnName) {
        ColumnName = columnName;
        return this;
    }

    public long getColumnId() {
        return ColumnId;
    }

    public DbTrackerColumn setColumnId(long columnId) {
        ColumnId = columnId;
        return this;
    }
}
