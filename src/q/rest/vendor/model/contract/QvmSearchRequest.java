package q.rest.vendor.model.contract;

import java.util.List;

public class QvmSearchRequest {
    private String query;
    private List<QvmVendorCredentials> vendorCreds;


    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<QvmVendorCredentials> getVendorCreds() {
        return vendorCreds;
    }

    public void setVendorCreds(List<QvmVendorCredentials> vendorCreds) {
        this.vendorCreds = vendorCreds;
    }
}
