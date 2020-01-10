package q.rest.vendor.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name="vnd_price_policy")
@Entity
public class PricePolicy implements Serializable {

    @Id
    @SequenceGenerator(name = "vnd_price_policy_id_seq_gen", sequenceName = "vnd_price_policy_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_price_policy_id_seq_gen")
    @Column(name="id")
    private int id;

    @Column(name = "vendor_id")
    private int vendorId;

    @Column(name = "policy_name")
    private String policyName;

    @Column(name = "factor")
    private double factor;

    @Column(name = "created_by")
    private int createdBy;

    @Column(name="created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;


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

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
