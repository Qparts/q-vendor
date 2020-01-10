package q.rest.vendor.model.entity.user;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "vnd_user_role")
@IdClass(VendorUserRole.VendorUserRolePK.class)
public class VendorUserRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	@ManyToOne
	private Role role;

	@Id
	@JoinColumn(name = "vendor_user_id", referencedColumnName = "id")
	@ManyToOne
	private VendorUser vendorUser;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public VendorUser getVendorUser() {
		return vendorUser;
	}

	public void setView(VendorUser user) {
		this.vendorUser =vendorUser;
	}

    public void setVendorUser(VendorUser vendorUser) {
        this.vendorUser = vendorUser;
    }

    public static class VendorUserRolePK implements Serializable {
		private static final long serialVersionUID = 1L;
		protected int role;
		protected int vendorUser;


		public VendorUserRolePK() {
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			VendorUserRolePK that = (VendorUserRolePK) o;
			return role == that.role &&
					vendorUser == that.vendorUser;
		}

		@Override
		public int hashCode() {
			return Objects.hash(role, vendorUser);
		}
	}
}
