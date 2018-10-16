package in.dsij.acemomentum.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Vikas on 10/10/2017.
 */

public class DbCall extends RealmObject {

    public static final String _ID = "ID";
    public static final String COMPANY = "Company";
    public static final String RECCO_DATE = "ReccoDate";
    public static final String RECCO_PRICE = "ReccoPrice";
    public static final String TARGET_PRICE = "TargetPrice";
    public static final String CURRENT_MARKET_PRICE = "CurrentMarketPrice";
    public static final String TEMPLATE_ID = "TemplateId";
    public static final String PRIORITY_ID = "PriorityID";
    public static final String PRODUCT_ID = "ProductId";
    public static final String PRODUCT_NAME = "ProductName";
    public static final String _CMP = "CMP";

    @PrimaryKey
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

    public DbCall setID(long ID) {
        this.ID = ID;
        return this;
    }

    public String getCompany() {
        return Company;
    }

    public DbCall setCompany(String company) {
        Company = company;
        return this;
    }

    public long getReccoDate() {
        return ReccoDate;
    }

    public DbCall setReccoDate(long reccoDate) {
        ReccoDate = reccoDate;
        return this;
    }

    public double getReccoPrice() {
        return ReccoPrice;
    }

    public DbCall setReccoPrice(double reccoPrice) {
        ReccoPrice = reccoPrice;
        return this;
    }

    public double getTargetPrice() {
        return TargetPrice;
    }

    public DbCall setTargetPrice(double targetPrice) {
        TargetPrice = targetPrice;
        return this;
    }

    public double getCurrentMarketPrice() {
        return CurrentMarketPrice;
    }

    public DbCall setCurrentMarketPrice(double currentMarketPrice) {
        CurrentMarketPrice = currentMarketPrice;
        return this;
    }

    public int getTemplateID() {
        return TemplateID;
    }

    public DbCall setTemplateID(int templateID) {
        TemplateID = templateID;
        return this;
    }

    public int getPriority() {
        return PriorityID;
    }

    public DbCall setPriority(int priority) {
        PriorityID = priority;
        return this;
    }

    public long getProductId() {
        return ProductId;
    }

    public DbCall setProductId(long productId) {
        ProductId = productId;
        return this;
    }

    public String getProductName() {
        return ProductName;
    }

    public DbCall setProductName(String productName) {
        ProductName = productName;
        return this;
    }
}
