package q.rest.vendor.model.entity.plan;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="vnd_plan_referral")
public class PlanReferral implements Serializable {
    @Id
    @SequenceGenerator(name = "vnd_plan_referral_id_seq_gen", sequenceName = "vnd_plan_referral_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_plan_referral_id_seq_gen")
    @Column(name="id")
    private int id;
    @Column(name="vendor_id")
    private int vendorId;
    @Column(name="target_vendor")
    private int targetVendorId;
    @Column(name="invitation_code")
    private String invitationCode;
    @Column( name="status")
    private char status;//A = available, U = used, P = pending paid subscription
    @Column( name="subscription_id")
    private int subscriptionId;
    @Column( name="created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column( name="created_by")
    private int createdBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
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
