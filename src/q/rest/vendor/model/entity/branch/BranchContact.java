package q.rest.vendor.model.entity.branch;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="vnd_branch_contact")
public class BranchContact implements Serializable {
    @Id
    @SequenceGenerator(name = "vnd_branch_contact_id_seq_gen", sequenceName = "vnd_branch_contact_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_branch_contact_id_seq_gen")
    @Column(name="id")
    private int id;
    @Column(name="vendor_id")
    private int vendorId;
    @Column(name="branch_id")
    private int branchId;
    @Column(name="created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="created_by")
    private int createdBy;
    @Column(name="created_by_vendor")
    private int createdeByVendor;
    @Column(name="phone")
    private String phone;
    @Column(name="status")
    private char status;
    @Column(name="email")
    private String email;
    @Column(name="notes")
    private String notes;

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

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getCreatedeByVendor() {
        return createdeByVendor;
    }

    public void setCreatedeByVendor(int createdeByVendor) {
        this.createdeByVendor = createdeByVendor;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
