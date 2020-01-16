package q.rest.vendor.model.contract;

import q.rest.vendor.model.entity.SignupRequest;
import q.rest.vendor.model.entity.Subscription;

import java.io.Serializable;

public class SignupHolder implements Serializable {
    private SignupRequest signupRequest;
    private Subscription subscription;
    private int existingVendorId;
    private boolean newVendor;

    public SignupRequest getSignupRequest() {
        return signupRequest;
    }

    public void setSignupRequest(SignupRequest signupRequest) {
        this.signupRequest = signupRequest;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
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
