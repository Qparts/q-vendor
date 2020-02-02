package q.rest.vendor.model.entity.registration;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="vnd_email_verification")
public class EmailVerification implements Serializable {

    @Id
    @SequenceGenerator(name = "vnd_email_verification_id_seq_gen", sequenceName = "vnd_email_verification_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_email_verification_id_seq_gen")
    @Column(name="id")
    private int id;
    //vendor
    @Column(name = "vendor_id")
    private int vendorId;
    @Column(name = "vendor_user_id")
    private int vendorUserId;
    @Column(name = "email")
    private String email;
    @Column(name="created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name="expire")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expire;
    @Column(name="token")
    private String token;
    @Column(name="status")
    private char status;//R = requested , V = Verified, E = expire

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

    public int getVendorUserId() {
        return vendorUserId;
    }

    public void setVendorUserId(int vendorUserId) {
        this.vendorUserId = vendorUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
}
