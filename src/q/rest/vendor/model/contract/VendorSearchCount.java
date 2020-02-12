package q.rest.vendor.model.contract;

import q.rest.vendor.model.entity.Vendor;

public class VendorSearchCount {
    private Vendor vendor;
    private int count;


    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
