package in.dsij.acemomentum.net.res;

import java.util.List;

/**
 * Created by Vikas on 12/19/2017.
 */

public class ResGetDownloadables {

    /**
     * Reason : {"Title":"Reason","Tag":"","PDFLink":"~/productattachment/ValueProductFiles/ReasonsAccelya_Kale_Solutions_Ltd4132017.pdf","WebLink":""}
     * Report : [{"Title":"","Tag":"","Date":1497343366,"Review":"~/productattachment/ValueProductFiles/ReviewAccelya_Kale_Solutions_Ltd692017.pdf","WebLink":""}]
     */

    private ReasonBean Reason;
    private List<ReportBean> Report;

    public ReasonBean getReason() {
        return Reason;
    }

    public void setReason(ReasonBean Reason) {
        this.Reason = Reason;
    }

    public List<ReportBean> getReport() {
        return Report;
    }

    public void setReport(List<ReportBean> Report) {
        this.Report = Report;
    }

    public static class ReasonBean {
        /**
         * Title : Reason
         * Tag :
         * PDFLink : ~/productattachment/ValueProductFiles/ReasonsAccelya_Kale_Solutions_Ltd4132017.pdf
         * WebLink :
         */

        private String Title;
        private String Tag;
        private String PDFLink;
        private String WebLink;
        private String subscriptionNo;

        public String getSubscriptionNo() {
            return subscriptionNo;
        }

        public void setSubscriptionNo(String subscriptionNo) {
            this.subscriptionNo = subscriptionNo;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getTag() {
            return Tag;
        }

        public void setTag(String Tag) {
            this.Tag = Tag;
        }

        public String getPDFLink() {
            return PDFLink;
        }

        public void setPDFLink(String PDFLink) {
            this.PDFLink = PDFLink;
        }

        public String getWebLink() {
            return WebLink;
        }

        public void setWebLink(String WebLink) {
            this.WebLink = WebLink;
        }
    }

    public static class ReportBean {
        /**
         * Title :
         * Tag :
         * Date : 1497343366
         * Review : ~/productattachment/ValueProductFiles/ReviewAccelya_Kale_Solutions_Ltd692017.pdf
         * WebLink :
         */

        private String Title;
        private String Tag;
        private long Date;
        private String Review;
        private String WebLink;
        private String subscriptionNo;

        public String getSubscriptionNo() {
            return subscriptionNo;
        }

        public void setSubscriptionNo(String subscriptionNo) {
            this.subscriptionNo = subscriptionNo;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getTag() {
            return Tag;
        }

        public void setTag(String Tag) {
            this.Tag = Tag;
        }

        public long getDate() {
            return Date;
        }

        public void setDate(long Date) {
            this.Date = Date;
        }

        public String getReview() {
            return Review;
        }

        public void setReview(String Review) {
            this.Review = Review;
        }

        public String getWebLink() {
            return WebLink;
        }

        public void setWebLink(String WebLink) {
            this.WebLink = WebLink;
        }
    }
}
