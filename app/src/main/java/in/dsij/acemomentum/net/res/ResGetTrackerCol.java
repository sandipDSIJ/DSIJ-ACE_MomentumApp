package in.dsij.acemomentum.net.res;

import java.util.List;

/**
 * Created by Vikas on 12/19/2017.
 */

public class ResGetTrackerCol {

    /**
     * TrackerCalls : [{"RecoId":1761,"CompanyName":"Visaka Industries Ltd.","RecommendationPrice":542.85,"RecommendationDate":1503532800,"Operation":"B","ExitDate":1507287515,"CompanyCode":11530004,"ExitPrice":707.5,"ColumnName":"Tiny Treasure","ColumnId":195,"PercReturn":30.33},{"RecoId":1558,"CompanyName":"Pondy Oxides & Chemicals Ltd.","RecommendationPrice":375.1,"RecommendationDate":1490227200,"Operation":"B","ExitDate":1507112520,"CompanyCode":12080072,"ExitPrice":511,"ColumnName":"Tiny Treasure","ColumnId":195,"PercReturn":36.23},{"RecoId":1485,"CompanyName":"Kalyani Steels Ltd.","RecommendationPrice":335,"RecommendationDate":1485302400,"Operation":"B","ExitDate":1497525170,"CompanyCode":15600003,"ExitPrice":446.45,"ColumnName":"Tiny Treasure","ColumnId":195,"PercReturn":33.27},{"RecoId":1445,"CompanyName":"Intrasoft Technologies Ltd.","RecommendationPrice":414.7,"RecommendationDate":1482969600,"Operation":"B","ExitDate":1504175630,"CompanyCode":17040371,"ExitPrice":543.85,"ColumnName":"Tiny Treasure","ColumnId":195,"PercReturn":31.14},{"RecoId":1393,"CompanyName":"Kiri Industries Ltd.","RecommendationPrice":275.8,"RecommendationDate":1479945600,"Operation":"B","ExitDate":1504194289,"CompanyCode":16090072,"ExitPrice":368.1,"ColumnName":"Tiny Treasure","ColumnId":195,"PercReturn":33.47},{"RecoId":1357,"CompanyName":"Cupid Ltd.","RecommendationPrice":315,"RecommendationDate":1477526400,"Operation":"B","ExitDate":1509126314,"CompanyCode":12530006,"ExitPrice":268.05,"ColumnName":"Tiny Treasure","ColumnId":195,"PercReturn":-14.9},{"RecoId":1295,"CompanyName":"Talbros Automotive Components Ltd.","RecommendationPrice":155.6,"RecommendationDate":1474502400,"Operation":"B","ExitDate":1497262239,"CompanyCode":10610008,"ExitPrice":204,"ColumnName":"Tiny Treasure","ColumnId":195,"PercReturn":31.11},{"RecoId":1249,"CompanyName":"Jayant Agro Organics Ltd.","RecommendationPrice":341.25,"RecommendationDate":1472083200,"Operation":"B","ExitDate":1479298270,"CompanyCode":11070071,"ExitPrice":449.85,"ColumnName":"Tiny Treasure","ColumnId":195,"PercReturn":31.82},{"RecoId":1217,"CompanyName":"Bhageria Industries Ltd.","RecommendationPrice":218.15,"RecommendationDate":1469664000,"Operation":"B","ExitDate":1471426295,"CompanyCode":16090049,"ExitPrice":307,"ColumnName":"Tiny Treasure","ColumnId":195,"PercReturn":40.73},{"RecoId":1180,"CompanyName":"The Byke Hospitality Ltd.","RecommendationPrice":159.1,"RecommendationDate":1466640000,"Operation":"B","ExitDate":1491917562,"CompanyCode":14010091,"ExitPrice":196.95,"ColumnName":"Tiny Treasure","ColumnId":195,"PercReturn":23.79},{"RecoId":1140,"CompanyName":"Mold-Tek Technologies Ltd.","RecommendationPrice":45.25,"RecommendationDate":1464220800,"Operation":"B","ExitDate":1495189093,"CompanyCode":14590049,"ExitPrice":54.2,"ColumnName":"Tiny Treasure","ColumnId":195,"PercReturn":19.78},{"RecoId":421,"CompanyName":"Nandan Denim Ltd.","RecommendationPrice":135.5,"RecommendationDate":1461801600,"Operation":"B","ExitDate":1486664588,"CompanyCode":16110004,"ExitPrice":122.15,"ColumnName":"Tiny Treasure","ColumnId":195,"PercReturn":-9.85}]
     * ColumnName : Tiny Treasure
     * ColumnId : 195
     */

    private String ColumnName;
    private long ColumnId;
    private List<TrackerCallsBean> TrackerCalls;

    public String getColumnName() {
        return ColumnName;
    }

    public void setColumnName(String ColumnName) {
        this.ColumnName = ColumnName;
    }

    public long getColumnId() {
        return ColumnId;
    }

    public void setColumnId(long ColumnId) {
        this.ColumnId = ColumnId;
    }

    public List<TrackerCallsBean> getTrackerCalls() {
        return TrackerCalls;
    }

    public void setTrackerCalls(List<TrackerCallsBean> TrackerCalls) {
        this.TrackerCalls = TrackerCalls;
    }

    public static class TrackerCallsBean {
        /**
         * RecoId : 1761
         * CompanyName : Visaka Industries Ltd.
         * RecommendationPrice : 542.85
         * RecommendationDate : 1503532800
         * Operation : B
         * ExitDate : 1507287515
         * CompanyCode : 11530004
         * ExitPrice : 707.5
         * ColumnName : Tiny Treasure
         * ColumnId : 195
         * PercReturn : 30.33
         */

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

        public void setRecoId(long RecoId) {
            this.RecoId = RecoId;
        }

        public String getCompanyName() {
            return CompanyName;
        }

        public void setCompanyName(String CompanyName) {
            this.CompanyName = CompanyName;
        }

        public double getRecommendationPrice() {
            return RecommendationPrice;
        }

        public void setRecommendationPrice(double RecommendationPrice) {
            this.RecommendationPrice = RecommendationPrice;
        }

        public long getRecommendationDate() {
            return RecommendationDate;
        }

        public void setRecommendationDate(long RecommendationDate) {
            this.RecommendationDate = RecommendationDate;
        }

        public String getOperation() {
            return Operation;
        }

        public void setOperation(String Operation) {
            this.Operation = Operation;
        }

        public long getExitDate() {
            return ExitDate;
        }

        public void setExitDate(long ExitDate) {
            this.ExitDate = ExitDate;
        }

        public double getCompanyCode() {
            return CompanyCode;
        }

        public void setCompanyCode(double CompanyCode) {
            this.CompanyCode = CompanyCode;
        }

        public double getExitPrice() {
            return ExitPrice;
        }

        public void setExitPrice(double ExitPrice) {
            this.ExitPrice = ExitPrice;
        }

        public String getColumnName() {
            return ColumnName;
        }

        public void setColumnName(String ColumnName) {
            this.ColumnName = ColumnName;
        }

        public long getColumnId() {
            return ColumnId;
        }

        public void setColumnId(long ColumnId) {
            this.ColumnId = ColumnId;
        }

        public double getPercReturn() {
            return PercReturn;
        }

        public void setPercReturn(double PercReturn) {
            this.PercReturn = PercReturn;
        }
    }
}
