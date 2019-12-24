package q.rest.vendor.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="vnd_signup_request")
public class SignupRequest implements Serializable {

    @Id
    @SequenceGenerator(name = "vnd_signup_request_id_seq_gen", sequenceName = "vnd_signup_request_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_signup_request_id_seq_gen")
    @Column(name="id")
    private int id;
    //vendor
    @Column(name = "vendor_name")
    private String vendorName;
    @Column(name = "vendor_name_ar")
    private String vendorNameAr;
    @Column(name = "vendor_type")
    private char vendorType;//V = viewer, I = Integrated
    @Column(name = "promo_code")
    private String promoCode;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "email")
    private String email;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column
    private char status;//N = new , A = Approved, D = declined
    @Column(name="notes")
    private String notes;
    @Column(name="country_id")
    private int countryId;
    @Column(name="city_id")
    private int cityId;

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorNameAr() {
        return vendorNameAr;
    }

    public void setVendorNameAr(String vendorNameAr) {
        this.vendorNameAr = vendorNameAr;
    }

    public char getVendorType() {
        return vendorType;
    }

    public void setVendorType(char vendorType) {
        this.vendorType = vendorType;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
}
