package in.dsij.acemomentum.net.res;

/**
 * Created by Vikas on 12/19/2017.
 */

public class ResGetProductBrief {

    /**
     * ProductName : Value Pick
     * ProductId : 194
     * PriorityId : 8
     * Subscribed : true
     * Trial : false
     * New : true
     * Brief :
     * Link :
     */

    private String ProductName;
    private long ProductId;
    private int PriorityId;
    private boolean Subscribed;
    private boolean Trial;
    private boolean New;
    private String Brief;
    private String Link;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public long getProductId() {
        return ProductId;
    }

    public void setProductId(long ProductId) {
        this.ProductId = ProductId;
    }

    public int getPriorityId() {
        return PriorityId;
    }

    public void setPriorityId(int PriorityId) {
        this.PriorityId = PriorityId;
    }

    public boolean isSubscribed() {
        return Subscribed;
    }

    public void setSubscribed(boolean Subscribed) {
        this.Subscribed = Subscribed;
    }

    public boolean isTrial() {
        return Trial;
    }

    public void setTrial(boolean Trial) {
        this.Trial = Trial;
    }

    public boolean isNew() {
        return New;
    }

    public void setNew(boolean New) {
        this.New = New;
    }

    public String getBrief() {
        return Brief;
    }

    public void setBrief(String Brief) {
        this.Brief = Brief;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String Link) {
        this.Link = Link;
    }
}
