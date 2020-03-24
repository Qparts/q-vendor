package q.rest.vendor.model.entity.registration;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name="vnd_password_reset")
@Entity
public class PasswordReset implements Serializable {

    @Id
    @SequenceGenerator(name = "vnd_password_reset_id_seq_gen", sequenceName = "vnd_password_reset_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vnd_password_reset_id_seq_gen")
    @Column(name="id")
    private long id;
    @Column(name="vendor_user_id")
    private int vendorUserId;
    @Column(name="token")
    private String token;
    @Column(name="created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name="expire")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expire;
    @Column(name="status")
    private char status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVendorUserId() {
        return vendorUserId;
    }

    public void setVendorUserId(int vendorUserId) {
        this.vendorUserId = vendorUserId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
}
