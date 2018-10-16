package in.dsij.acemomentum.net.res;

/**
 * Created by Vikas on 10/10/2017.
 */

public class ResGetCalls {

    /**
     * ID : 1876
     * Company : UPL Ltd.
     * ReccoDate : 1510185600
     * ReccoPrice : 743
     * TargetPrice : 945
     * CurrentMarketPrice : 772.9
     * TemplateID : 1
     * PriorityID : 1
     * ProductId : 194
     * ProductName : Value Pick
     */

    private long ID;
    private String Company;
    private long ReccoDate;
    private double ReccoPrice;
    private double TargetPrice;
    private double CurrentMarketPrice;
    private int TemplateID;
    private int PriorityID;
    private long ProductId;
    private String ProductName;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public long getReccoDate() {
        return ReccoDate;
    }

    public void setReccoDate(long ReccoDate) {
        this.ReccoDate = ReccoDate;
    }

    public double getReccoPrice() {
        return ReccoPrice;
    }

    public void setReccoPrice(double ReccoPrice) {
        this.ReccoPrice = ReccoPrice;
    }

    public double getTargetPrice() {
        return TargetPrice;
    }

    public void setTargetPrice(double TargetPrice) {
        this.TargetPrice = TargetPrice;
    }

    public double getCurrentMarketPrice() {
        return CurrentMarketPrice;
    }

    public void setCurrentMarketPrice(double CurrentMarketPrice) {
        this.CurrentMarketPrice = CurrentMarketPrice;
    }

    public int getTemplateID() {
        return TemplateID;
    }

    public void setTemplateID(int TemplateID) {
        this.TemplateID = TemplateID;
    }

    public int getPriorityID() {
        return PriorityID;
    }

    public void setPriorityID(int PriorityID) {
        this.PriorityID = PriorityID;
    }

    public long getProductId() {
        return ProductId;
    }

    public void setProductId(long ProductId) {
        this.ProductId = ProductId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }
}
