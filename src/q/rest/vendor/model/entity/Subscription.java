package q.rest.vendor.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name="vnd_subscription")
public class Subscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "vnd_subscription_id_seq_gen", sequenceName = "vnd_subscription_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_subscription_id_seq_gen")
    @Column(name="id")
    private int id;

    @Column(name="vendor_id")
    private int vendorId;

    @Column(name="status")
    private char status;

    @Column(name="created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name="expire")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expire;

    @Column(name="vendor_user_id")
    private long vendorUserId;


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

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public long getVendorUserId() {
        return vendorUserId;
    }

    public void setVendorUserId(long vendorUserId) {
        this.vendorUserId = vendorUserId;
    }
}
