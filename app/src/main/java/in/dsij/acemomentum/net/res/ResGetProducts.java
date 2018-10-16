package in.dsij.acemomentum.net.res;

import java.util.List;

/**
 * Created by Vikas on 10/10/2017.
 */

public class ResGetProducts {

    private List<ProductBean> Product;

    public List<ProductBean> getProduct() {
        return Product;
    }

    public void setProduct(List<ProductBean> Product) {
        this.Product = Product;
    }

    public static class ProductBean {
        /**
         * ProductName : ACE Momentum
         * ProductId : 202
         * PriorityId : 1
         * Subscribed : false
         * Trial : false
         * New : false
         */

        private String ProductName;
        private long ProductId;
        private int PriorityId;
        private boolean Subscribed;
        private boolean Trial;
        private boolean New;

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
    }
}
