package in.dsij.acemomentum.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Vikas on 10/6/2017.
 */

public class DbProduct extends RealmObject {
    public static final String PRODUCT_NAME = "ProductName";
    public static final String PRODUCT_ID = "ProductId";
    public static final String PRIORITY_ID = "PriorityId";
    public static final String SUBSCRIBED = "Subscribed";
    public static final String TRIAL = "Trial";
    public static final String NEW = "New";

    @PrimaryKey
    private long ProductId;
    private String ProductName;
    private int PriorityId;
    private boolean Subscribed;
    private boolean Trial;
    private boolean New;

    public long getProductId() {
        return ProductId;
    }

    public DbProduct setProductId(long productId) {
        ProductId = productId;
        return this;
    }

    public String getProductName() {
        return ProductName;
    }

    public DbProduct setProductName(String productName) {
        ProductName = productName;
        return this;
    }

    public int getPriorityId() {
        return PriorityId;
    }

    public DbProduct setPriorityId(int priorityId) {
        PriorityId = priorityId;
        return this;
    }

    public boolean isSubscribed() {
        return Subscribed;
    }

    public DbProduct setSubscribed(boolean subscribed) {
        Subscribed = subscribed;
        return this;
    }

    public boolean isTrial() {
        return Trial;
    }

    public DbProduct setTrial(boolean trial) {
        Trial = trial;
        return this;
    }

    public boolean isNew() {
        return New;
    }

    public DbProduct setNew(boolean aNew) {
        New = aNew;
        return this;
    }
}
