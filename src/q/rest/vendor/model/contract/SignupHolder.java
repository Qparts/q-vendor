package q.rest.vendor.model.contract;

import q.rest.vendor.model.entity.registration.SignupRequest;

import java.io.Serializable;

public class SignupHolder implements Serializable {
    private SignupRequest signupRequest;
    private int planId;
    private int optionId;
    private int existingVendorId;
    private boolean newVendor;
    private int createdBy;

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public SignupRequest getSignupRequest() {
        return signupRequest;
    }

    public void setSignupRequest(SignupRequest signupRequest) {
        this.signupRequest = signupRequest;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getExistingVendorId() {
        return existingVendorId;
    }

    public void setExistingVendorId(int existingVendorId) {
        this.existingVendorId = existingVendorId;
    }

    public boolean isNewVendor() {
        return newVendor;
    }

    public void setNewVendor(boolean newVendor) {
        this.newVendor = newVendor;
    }
}
