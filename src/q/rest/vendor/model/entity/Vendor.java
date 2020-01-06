package q.rest.vendor.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private Integer cityId;
    @Column(name="note")
    private String notes;
    @Column(name="integration_secret_code")
    private String integrationSecretCode;
    @Column(name="integration_type")
    private Character integrationType;
    @Column(name="endpoint_address")
    private String endpointAddress;
    @Column(name = "endpoint_health_check_address")
    @JsonIgnore
    private String healthCheckAddress;
    @Transient
    private List<VendorCategory> vendorCategories;
    @Transient
    private List<VendorContact> vendorContacts;
    @Transient
    private List<Branch> branches;

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public List<VendorContact> getVendorContacts() {
        return vendorContacts;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIntegrationSecretCode() {
        return integrationSecretCode;
    }

    public void setIntegrationSecretCode(String integrationSecretCode) {
        this.integrationSecretCode = integrationSecretCode;
    }

    public Character getIntegrationType() {
        return integrationType;
    }

    public void setIntegrationType(Character integrationType) {
        this.integrationType = integrationType;
    }

    public String getEndpointAddress() {
        return endpointAddress;
    }

    public void setEndpointAddress(String endpointAddress) {
        this.endpointAddress = endpointAddress;
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

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getHealthCheckAddress() {
        return healthCheckAddress;
    }

    public void setHealthCheckAddress(String healthCheckAddress) {
        this.healthCheckAddress = healthCheckAddress;
    }
}