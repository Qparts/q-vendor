package q.rest.vendor.model.contract;

import q.rest.vendor.model.entity.*;
import q.rest.vendor.model.entity.plan.PlanReferral;
import q.rest.vendor.model.entity.plan.PlanSubscription;
import q.rest.vendor.model.entity.user.AccessToken;

import java.io.Serializable;
import java.util.List;

public class VendorHolder implements Serializable {
    private Vendor vendor;
    private List<VendorUserHolder> vendorUsers;
    private List<QvmSearchKeyword> keywords;
    private List<VendorPricePolicy> vendorPolicies;
    private List<PlanSubscription> planSubscriptions;
    private List<AccessToken> accessTokens;
    private List<PlanReferral> referrals;
    private List<Branch> branches;

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public List<VendorPricePolicy> getVendorPolicies() {
        return vendorPolicies;
    }

    public void setVendorPolicies(List<VendorPricePolicy> vendorPolicies) {
        this.vendorPolicies = vendorPolicies;
    }

    public List<VendorUserHolder> getVendorUsers() {
        return vendorUsers;
    }

    public void setVendorUsers(List<VendorUserHolder> vendorUsers) {
        this.vendorUsers = vendorUsers;
    }

    public List<QvmSearchKeyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<QvmSearchKeyword> keywords) {
        this.keywords = keywords;
    }


    public List<PlanSubscription> getPlanSubscriptions() {
        return planSubscriptions;
    }

    public void setPlanSubscriptions(List<PlanSubscription> planSubscriptions) {
        this.planSubscriptions = planSubscriptions;
    }

    public List<AccessToken> getAccessTokens() {
        return accessTokens;
    }

    public void setAccessTokens(List<AccessToken> accessTokens) {
        this.accessTokens = accessTokens;
    }

    public List<PlanReferral> getReferrals() {
        return referrals;
    }

    public void setReferrals(List<PlanReferral> referrals) {
        this.referrals = referrals;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }
}
