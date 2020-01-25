package q.rest.vendor.model.entity.plan;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="vnd_plan_offer")
public class PlanOffer implements Serializable {

    @Id
    @SequenceGenerator(name = "vnd_plan_offer_id_seq_gen", sequenceName = "vnd_plan_offer_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_plan_offer_id_seq_gen")
    @Column(name="id")
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="name_ar")
    private String nameAr;
    @Column(name="plan_id")
    private int planId;
    @JoinColumn(name="option_id")
    @ManyToOne
    private PlanOption planOption;
    @Column(name="offer_type")
    private char offerType; //--D = discount, S = free usage if discount add discount percentage, if free usage add duration days
    @Column(name="discount_percentage")
    private double discountPercentage;
    @Column(name="duration_days")
    private int durationDays;
    @Column(name="invitations")
    private int invitations;

    public int getInvitations() {
        return invitations;
    }

    public void setInvitations(int invitations) {
        this.invitations = invitations;
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

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public PlanOption getPlanOption() {
        return planOption;
    }

    public void setPlanOption(PlanOption planOption) {
        this.planOption = planOption;
    }

    public char getOfferType() {
        return offerType;
    }

    public void setOfferType(char offerType) {
        this.offerType = offerType;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }
}
