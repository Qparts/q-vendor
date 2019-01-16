package q.rest.vendor.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="vnd_vendor_category")
@IdClass(VendorCategory.VendorCategoryPK.class)
public class VendorCategory implements Serializable {

    @Id
    @Column(name="vendor_id")
    private int vendorId;
    @Id
    @Column(name="category_id")
    private int categoryId;

    @Column(name="sales_percentage")
    private double percentage;

    @Column(name="created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name="created_by")
    private int createdBy;

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendorCategory that = (VendorCategory) o;
        return vendorId == that.vendorId &&
                categoryId == that.categoryId &&
                Double.compare(that.percentage, percentage) == 0 &&
                createdBy == that.createdBy &&
                Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendorId, categoryId, percentage, created, createdBy);
    }

    public static class VendorCategoryPK implements Serializable{
        private static final long serialVersionUID = 1L;
        protected int vendorId;
        protected int categoryId;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + vendorId;
            result = prime * result + categoryId;
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            VendorCategoryPK other = (VendorCategoryPK) obj;
            if (vendorId != other.vendorId)
                return false;
            if (categoryId != other.categoryId)
                return false;
            return true;
        }



    }
}
