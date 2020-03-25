package q.rest.vendor.model.entity.registration;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="vnd_contact_us")
public class ContactUs implements Serializable {
    @Id
    @SequenceGenerator(name = "vnd_contact_us_id_seq_gen", sequenceName = "vnd_contact_us_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_contact_us_id_seq_gen")
    @Column(name="id")
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="email")
    private String email;
    @Column(name="mobile")
    private String mobile;
    @Column(name="country_id")
    private int countryId;
    @Column(name="company_name")
    private String companyName;
    @Column(name="notes")
    private String notes;
    @Column(name="created")
    private Date created;
    @Column(name="status")
    private char status;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
