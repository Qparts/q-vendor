package q.rest.vendor.model.entity.plan;

import q.rest.vendor.model.entity.user.Role;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="vnd_plan")
public class Plan implements Serializable {

    @Id
    @SequenceGenerator(name = "vnd_plan_id_seq_gen", sequenceName = "vnd_plan_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_plan_id_seq_gen")
    @Column(name="id")
    private int id;
    @Column(name="price")
    private double price;
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
    @Transient
    private List<PlanOption> planOptions;
    @Transient
    private List<Role> roles;
    @Transient
    private List<PlanOffer> planOffers;

    public List<PlanOffer> getPlanOffers() {
        return planOffers;
    }

    public void setPlanOffers(List<PlanOffer> planOffers) {
        this.planOffers = planOffers;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<PlanOption> getPlanOptions() {
        return planOptions;
    }

    public void setPlanOptions(List<PlanOption> planOptions) {
        this.planOptions = planOptions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
}
