package q.rest.vendor.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "vnd_access_token")
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "vnd_access_token_id_seq_gen", sequenceName = "vnd_access_token_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vnd_access_token_id_seq_gen")
    @Column(name="id")
    private long id;

    @Column(name="vendor_user_id")
    private long vendorUserId;

    @Column(name="token_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "token_expire")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expire;

    @Column(name = "token_value")
    private String token;

    @JoinColumn(name = "app_code", referencedColumnName = "app_code")
    @ManyToOne
    private WebApp webApp;

    @Column(name = "token_status")
    private char status;// this is the token status, K = Killed, A = Active

    public AccessToken(long username, Date created) {
        this.vendorUserId = username;
        this.created = created;
    }

    public AccessToken() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVendorUserId() {
        return vendorUserId;
    }

    public void setVendorUserId(long vendorUserId) {
        this.vendorUserId = vendorUserId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setWebApp(WebApp webApp) {
        this.webApp = webApp;
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

    public WebApp getWebApp() {
        return webApp;
    }

    public void setAppCode(WebApp appCode) {
        this.webApp = appCode;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessToken that = (AccessToken) o;
        return id == that.id &&
                vendorUserId == that.vendorUserId &&
                status == that.status &&
                Objects.equals(created, that.created) &&
                Objects.equals(expire, that.expire) &&
                Objects.equals(token, that.token) &&
                Objects.equals(webApp, that.webApp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vendorUserId, created, expire, token, webApp, status);
    }
}
