package q.rest.vendor.model.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name="vnd_courier")
@Entity
public class Courier implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "vnd_courier_id_seq_gen", sequenceName = "vnd_courier_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_courier_id_seq_gen")
    @Column(name="id")
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="name_ar")
    private String nameAr;
    @Column(name="trackable")
    private boolean trackable;
    @Column(name="customer_status")
    private char customerStatus;
    @Column(name="internal_status")
    private char internalStatus;
    @Column(name="created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name="created_by")
    private int createdBy;
    @Column(name="track_link")
    private String trackLink;


    public String getTrackLink() {
        return trackLink;
    }
    public void setTrackLink(String trackLink) {
        this.trackLink = trackLink;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNameAr() {
        return nameAr;
    }
    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }
    public boolean isTrackable() {
        return trackable;
    }
    public void setTrackable(boolean trackable) {
        this.trackable = trackable;
    }
    public char getCustomerStatus() {
        return customerStatus;
    }
    public void setCustomerStatus(char customerStatus) {
        this.customerStatus = customerStatus;
    }
    public char getInternalStatus() {
        return internalStatus;
    }
    public void setInternalStatus(char internalStatus) {
        this.internalStatus = internalStatus;
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
