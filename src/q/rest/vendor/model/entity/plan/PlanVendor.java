package q.rest.vendor.model.entity.plan;

import q.rest.vendor.model.entity.Vendor;
import q.rest.vendor.model.entity.user.Role;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "vnd_plan_vendor")
@IdClass(PlanVendor.PlanVendorPK.class)
public class PlanVendor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JoinColumn(name = "vendor_id", referencedColumnName = "id")
    @ManyToOne
    private Vendor vendor;

    @Id
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    @ManyToOne
    private Plan plan;

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public static class PlanVendorPK implements Serializable {
        private static final long serialVersionUID = 1L;
        protected int vendor;
        protected int plan;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PlanVendorPK that = (PlanVendorPK) o;
            return vendor == that.vendor &&
                    plan == that.plan;
        }

        @Override
        public int hashCode() {
            return Objects.hash(vendor, plan);
        }
    }
}
