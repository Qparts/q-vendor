package q.rest.vendor.model.entity.plan;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="vnd_plan_subscription")
public class PlanSubscription implements Serializable {

    @Id
    @SequenceGenerator(name = "vnd_plan_subscription_id_seq_gen", sequenceName = "vnd_plan_subscription_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_plan_subscription_id_seq_gen")
    @Column(name="id")
    private int id;
    @Column(name="plan_id")
    private int planId;
    @Column(name="option_id")
    private int optionId;
    @Column(name="vendor_id")
    private int vendorId;
    @Column(name="start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name="end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name="status")
    private char status;//  A = active, E = expired , F = future
    @Column(name="created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name="created_by")
    private int createdBy;
    @Column(name="sales_id")
    private Long salesId;

    public Long getSalesId() {
        return salesId;
    }

    public void setSalesId(Long salesId) {
        this.salesId = salesId;
    }

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

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
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

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
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
}
