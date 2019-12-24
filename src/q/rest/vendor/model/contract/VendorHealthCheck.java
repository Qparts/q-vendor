package q.rest.vendor.model.contract;


import q.rest.vendor.model.entity.Vendor;

import java.io.Serializable;

public class VendorHealthCheck implements Serializable {
    private Vendor vendor;
    private int status;

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
