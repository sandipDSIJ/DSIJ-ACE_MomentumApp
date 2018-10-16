package in.dsij.acemomentum.net.res;

import java.util.List;

/**
 * Created by Vikas on 10/10/2017.
 */
/*
*
*
* Not used in this app
*
*
* */
public class ResGetLiveCalls {

    private List<LiveCallBean> LiveCall;

    public List<LiveCallBean> getLiveCall() {
        return LiveCall;
    }

    public void setLiveCall(List<LiveCallBean> LiveCall) {
        this.LiveCall = LiveCall;
    }

    public static class LiveCallBean {
        /**
         * Id : 60158
         * Headline :
         * Description : POP BTST - Book Profit in GODREJCP at 989.15.
         * Time : 1507630690
         * TemplateId : 1
         * PriorityId : 1
         * ProductId : 148
         * ProductName : Pop BTST
         */

        private long Id;
        private String Headline;
        private String Description;
        private long Time;
        private int TemplateId;
        private int PriorityId;
        private int ProductId;
        private String ProductName;

        public long getId() {
            return Id;
        }

        public void setId(long Id) {
            this.Id = Id;
        }

        public String getHeadline() {
            return Headline;
        }

        public void setHeadline(String Headline) {
            this.Headline = Headline;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public long getTime() {
            return Time;
        }

        public void setTime(long Time) {
            this.Time = Time;
        }

        public int getTemplateId() {
            return TemplateId;
        }

        public void setTemplateId(int TemplateId) {
            this.TemplateId = TemplateId;
        }

        public int getPriorityId() {
            return PriorityId;
        }

        public void setPriorityId(int PriorityId) {
            this.PriorityId = PriorityId;
        }

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int ProductId) {
            this.ProductId = ProductId;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }
    }
}
