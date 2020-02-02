package q.rest.vendor.model.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="vnd_user")
public class VendorUser implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "vnd_user_id_seq_gen", sequenceName = "vnd_user_id_seq", initialValue=1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_user_id_seq_gen")
	@Column(name="id")
	private int id;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
		
	@JsonIgnore
	@Column(name="password")
	private String password;
	
	@Column(name="status")
	private char status;//A = account created, V = verified

	@Column(name="vendor_id")
	private int vendorId;

	@Column(name="email")
	private String email;

	@Column(name="mobile")
	private String mobile;

	@Column(name="notes")
	private String notes;

	@Column(name="created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column(name="created_by")
	private int createdBy;




	public int getVendorId() {
		return vendorId;
	}

	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPassword() {
		return password;
	}

	public char getStatus() {
		return status;
	}



	public void setId(int id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		VendorUser user = (VendorUser) o;
		return id == user.id &&
				status == user.status &&
				vendorId == user.vendorId &&
				createdBy == user.createdBy &&
				Objects.equals(firstName, user.firstName) &&
				Objects.equals(lastName, user.lastName) &&
				Objects.equals(password, user.password) &&
				Objects.equals(email, user.email) &&
				Objects.equals(mobile, user.mobile) &&
				Objects.equals(notes, user.notes) &&
				Objects.equals(created, user.created);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, password, status, vendorId, email, mobile, notes, created, createdBy);
	}
}
