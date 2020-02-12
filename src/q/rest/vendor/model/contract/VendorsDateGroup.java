package q.rest.vendor.model.contract;

import q.rest.vendor.model.entity.Vendor;

import java.util.Date;
import java.util.List;

public class VendorsDateGroup {
    private Date date;
    private List<Vendor> dailyVendors;
    private List<Vendor> totalVendors;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Vendor> getDailyVendors() {
        return dailyVendors;
    }

    public void setDailyVendors(List<Vendor> dailyVendors) {
        this.dailyVendors = dailyVendors;
    }

    public List<Vendor> getTotalVendors() {
        return totalVendors;
    }

    public void setTotalVendors(List<Vendor> totalVendors) {
        this.totalVendors = totalVendors;
    }
}
