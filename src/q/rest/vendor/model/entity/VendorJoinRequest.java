package q.rest.vendor.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="vnd_join_request")
public class VendorJoinRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "vnd_join_request_id_seq_gen", sequenceName = "vnd_join_request_id_seq", initialValue=1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_join_request_id_seq_gen")
	@Column(name="id")
	private int id;
	@Column(name="company_name")
	private String companyName;
	@Column(name="work_activity")
	private String workActivity;
	@Column(name="car_models")
	private String carModel;
	@Column(name="parts_type")
	private String partsType;// A = Genuine, B = Non-Genuine, C = All
	@Column(name="main_branch_location")
	private String mainBranchLocation;
	@Column(name="number_of_branches")
	private String numberOfBranch;
	@Column(name="delivery_service")
	private boolean deliveryService;
	@Column(name="name_of_manager")
	private String nameOfManger;
	@Column(name="phone_number")
	private String phoneNumber;
	@Column(name="email")
	private String email;
	@Column(name="archived")
	private boolean archived;
	@Column(name="created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getWorkActivity() {
		return workActivity;
	}

	public void setWorkActivity(String workActivity) {
		this.workActivity = workActivity;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public String getMainBranchLocation() {
		return mainBranchLocation;
	}

	public void setMainBranchLocation(String mainBranchLocation) {
		this.mainBranchLocation = mainBranchLocation;
	}

	public String getNumberOfBranch() {
		return numberOfBranch;
	}

	public void setNumberOfBranch(String numberOfBranch) {
		this.numberOfBranch = numberOfBranch;
	}

	public boolean isDeliveryService() {
		return deliveryService;
	}

	public void setDeliveryService(boolean deliveryService) {
		this.deliveryService = deliveryService;
	}

	public String getNameOfManger() {
		return nameOfManger;
	}

	public void setNameOfManger(String nameOfManger) {
		this.nameOfManger = nameOfManger;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPartsType() {
		return partsType;
	}

	public void setPartsType(String partsType) {
		this.partsType = partsType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
