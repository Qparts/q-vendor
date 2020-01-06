package q.rest.vendor.model.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name="vnd_upload_request")
@Entity
public class VendorUploadRequest {

    @Id
    @SequenceGenerator(name = "vnd_upload_request_id_seq_gen", sequenceName = "vnd_upload_request_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_upload_request_id_seq_gen")
    @Column(name="id")
    private int id;
    @Column(name="vendor_id")
    private int vendorId;
    @Column(name="vendor_user_id")
    private int vendorUserId;
    @Column(name="status")
    private char status;//R = requested, C = compelted, E = error
    @Column(name="created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name="completed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completed;

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

    public Date getCompleted() {
        return completed;
    }

    public void setCompleted(Date completed) {
        this.completed = completed;
    }
}
