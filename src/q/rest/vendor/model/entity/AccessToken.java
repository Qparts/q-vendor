package q.rest.vendor.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((created == null) ? 0 : created.hashCode());
        result = prime * result + (int) (vendorUserId ^ (vendorUserId >>> 32));
        result = prime * result + ((expire == null) ? 0 : expire.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + status;
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        result = prime * result + ((webApp == null) ? 0 : webApp.hashCode());
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
        AccessToken other = (AccessToken) obj;
        if (created == null) {
            if (other.created != null)
                return false;
        } else if (!created.equals(other.created))
            return false;
        if (vendorUserId != other.vendorUserId)
            return false;
        if (expire == null) {
            if (other.expire != null)
                return false;
        } else if (!expire.equals(other.expire))
            return false;
        if (id != other.id)
            return false;
        if (status != other.status)
            return false;
        if (token == null) {
            if (other.token != null)
                return false;
        } else if (!token.equals(other.token))
            return false;
        if (webApp == null) {
            if (other.webApp != null)
                return false;
        } else if (!webApp.equals(other.webApp))
            return false;
        return true;
    }



}
