package q.rest.vendor.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="vnd_vendor_price_policy")
@IdClass(VendorPricePolicy.VendorPricePolicyPK.class)
public class VendorPricePolicy implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="vendor_id")
	private int vendorId;
	
	@Id
	@Column(name="target_vendor_id")
	private int targetVendorId;

	@Column(name="policy_id")
	private int pricePolicyId;

	@Column(name="created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column(name="created_by")
	private int createdBy;

	@Transient
	private PricePolicy pricePolicy;

	public PricePolicy getPricePolicy() {
		return pricePolicy;
	}

	public void setPricePolicy(PricePolicy pricePolicy) {
		this.pricePolicy = pricePolicy;
	}

	public int getVendorId() {
		return vendorId;
	}

	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	public int getTargetVendorId() {
		return targetVendorId;
	}

	public void setTargetVendorId(int targetVendorId) {
		this.targetVendorId = targetVendorId;
	}

	public int getPricePolicyId() {
		return pricePolicyId;
	}

	public void setPricePolicyId(int pricePolicyId) {
		this.pricePolicyId = pricePolicyId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}



	public static class VendorPricePolicyPK implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected int vendorId;
		protected int targetVendorId;
		public VendorPricePolicyPK() {}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			VendorPricePolicyPK that = (VendorPricePolicyPK) o;
			return vendorId == that.vendorId &&
					targetVendorId == that.targetVendorId;
		}

		@Override
		public int hashCode() {
			return Objects.hash(vendorId, targetVendorId);
		}
	}
	
}
