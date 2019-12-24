package q.rest.vendor.model.contract;


import q.rest.vendor.model.entity.VendorUser;

import java.io.Serializable;

public class VendorUserHolder implements Serializable{

	private static final long serialVersionUID = 1L;
	private VendorUser vendorUser;
	private String token;
	
	
	
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
