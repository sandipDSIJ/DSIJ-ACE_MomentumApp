package in.dsij.acemomentum.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by vikas on 1/17/2018.
 */

public class DbTrackerRow extends RealmObject {

    public static final String RECO_ID = "RecoId";
    public static final String COMPANY_NAME = "CompanyName";
    public static final String RECOMMENDATION_PRICE = "RecommendationPrice";
    public static final String RECOMMENDATION_DATE = "RecommendationDate";
    public static final String OPERATION = "Operation";
    public static final String EXIT_DATE = "ExitDate";
    public static final String COMPANY_CODE = "CompanyCode";
    public static final String EXIT_PRICE = "ExitPrice";
    public static final String COLUMN_NAME = "ColumnName";
    public static final String COLUMN_ID = "ColumnId";
    public static final String PERC_RETURN = "PercReturn";

    @PrimaryKey
    private long RecoId;
    private String CompanyName;
    private double RecommendationPrice;
    private long RecommendationDate;
    private String Operation;
    private long ExitDate;
    private double CompanyCode;
    private double ExitPrice;
    private String ColumnName;
    private long ColumnId;
    private double PercReturn;

    public long getRecoId() {
        return RecoId;
    }

    public DbTrackerRow setRecoId(long recoId) {
        RecoId = recoId;
        return this;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public DbTrackerRow setCompanyName(String companyName) {
        CompanyName = companyName;
        return this;
    }

    public double getRecommendationPrice() {
        return RecommendationPrice;
    }

    public DbTrackerRow setRecommendationPrice(double recommendationPrice) {
        RecommendationPrice = recommendationPrice;
        return this;
    }

    public long getRecommendationDate() {
        return RecommendationDate;
    }

    public DbTrackerRow setRecommendationDate(long recommendationDate) {
        RecommendationDate = recommendationDate;
        return this;
    }

    public String getOperation() {
        return Operation;
    }

    public DbTrackerRow setOperation(String operation) {
        Operation = operation;
        return this;
    }

    public long getExitDate() {
        return ExitDate;
    }

    public DbTrackerRow setExitDate(long exitDate) {
        ExitDate = exitDate;
        return this;
    }

    public double getCompanyCode() {
        return CompanyCode;
    }

    public DbTrackerRow setCompanyCode(double companyCode) {
        CompanyCode = companyCode;
        return this;
    }

    public double getExitPrice() {
        return ExitPrice;
    }

    public DbTrackerRow setExitPrice(double exitPrice) {
        ExitPrice = exitPrice;
        return this;
    }

    public String getColumnName() {
        return ColumnName;
    }

    public DbTrackerRow setColumnName(String columnName) {
        ColumnName = columnName;
        return this;
    }

    public long getColumnId() {
        return ColumnId;
    }

    public DbTrackerRow setColumnId(long columnId) {
        ColumnId = columnId;
        return this;
    }

    public double getPercReturn() {
        return PercReturn;
    }

    public DbTrackerRow setPercReturn(double percReturn) {
        PercReturn = percReturn;
        return this;
    }
}
