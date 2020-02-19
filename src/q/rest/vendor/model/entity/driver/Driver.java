package q.rest.vendor.model.entity.driver;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Driver will be part of vendor until it is decided to move to a new service
 * this will happen when enough functions are available for driver application
 * In addition, a new application for drivers will be developed
 * Preferrably in android
 */
@Entity
@Table(name = "vnd_driver")
public class Driver implements Serializable {

    @Id
    @SequenceGenerator(name = "vnd_driver_id_seq_gen", sequenceName = "vnd_driver_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_driver_id_seq_gen")
    @Column(name="id")
    private int id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="email")
    private String email;
    @Column(name="mobile")
    private String mobile;
    @Column(name="status")
    private char status;
    @Column(name="created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Transient
    private List<DriverCity> driverCities;

    public List<DriverCity> getDriverCities() {
        return driverCities;
    }

    public void setDriverCities(List<DriverCity> driverCities) {
        this.driverCities = driverCities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
