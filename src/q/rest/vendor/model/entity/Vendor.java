package q.rest.vendor.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="vnd_vendor")
public class Vendor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "vnd_vendor_id_seq_gen", sequenceName = "vnd_vendor_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_vendor_id_seq_gen")
    @Column(name="id")
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="name_ar")
    private String nameAr;
    @Column(name="status")
    private char status;
    @Column(name="created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name="created_by")
    private int createdBy;
    @Column(name="city_id")
    private int cityId;
    @Transient
    private List<VendorCategory> vendorCategories;
    @Transient
    private List<VendorContact> vendorContacts;

    public List<VendorContact> getVendorContacts() {
        return vendorContacts;
    }

    public void setVendorContacts(List<VendorContact> vendorContacts) {
        this.vendorContacts = vendorContacts;
    }

    public Vendor(){
        this.vendorCategories = new ArrayList<>();
    }

    public List<VendorCategory> getVendorCategories() {
        return vendorCategories;
    }

    public void setVendorCategories(List<VendorCategory> vendorCategories) {
        this.vendorCategories = vendorCategories;
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

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}