package q.rest.vendor.model.entity.plan;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="vnd_plan_promotion")
public class PlanPromotion implements Serializable {

    @Id
    @SequenceGenerator(name = "vnd_plan_promotion_id_seq_gen", sequenceName = "vnd_plan_promotion_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_plan_promotion_id_seq_gen")
    @Column(name="id")
    private int id;

    @Column(name="plan_id")
    private int planId;

    @Column(name="option_id")
    private int optionId;

    @Column(name="status")
    private char status;

    @Column(name="discount_percentage")
    private double discountPercentage;

    @Column(name="promo_code")
    private String promoCode;

    @Column(name="start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name="end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "unique_to_vendor")
    private boolean vendorUnique;

    @Column(name="reusable")
    private boolean reusable;

    @Column(name="vendor_id")
    private int vendorId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isVendorUnique() {
        return vendorUnique;
    }

    public void setVendorUnique(boolean vendorUnique) {
        this.vendorUnique = vendorUnique;
    }

    public boolean isReusable() {
        return reusable;
    }

    public void setReusable(boolean reusable) {
        this.reusable = reusable;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }
}
