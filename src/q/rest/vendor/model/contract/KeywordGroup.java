package q.rest.vendor.model.contract;

import q.rest.vendor.model.entity.Vendor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class KeywordGroup implements Serializable {
    private String keyword;
    private int count;
    private Date lastSearch;
    private List<Vendor> vendors;

    public List<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }

    public Date getLastSearch() {
        return lastSearch;
    }

    public void setLastSearch(Date lastSearch) {
        this.lastSearch = lastSearch;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
