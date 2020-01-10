package q.rest.vendor.model.contract;


import q.rest.vendor.model.entity.user.Activity;
import q.rest.vendor.model.entity.user.Role;
import q.rest.vendor.model.entity.user.VendorUser;

import java.io.Serializable;
import java.util.List;

public class VendorUserHolder implements Serializable{

	private static final long serialVersionUID = 1L;
	private VendorUser vendorUser;
	private List<Role> roles;
	private List<Activity> activities;
	private String token;

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public VendorUser getVendorUser() {
		return vendorUser;
	}

	public void setVendorUser(VendorUser vendorUser) {
		this.vendorUser = vendorUser;
	}
}
