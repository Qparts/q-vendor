package q.rest.vendor.model.entity.plan;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="vnd_plan_option")
public class PlanOption implements Serializable {

    @Id
    @SequenceGenerator(name = "vnd_plan_option_id_seq_gen", sequenceName = "vnd_plan_option_id_seq", initialValue=1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vnd_plan_option_id_seq_gen")
    @Column(name="id")
    private int id;
    @Column(name="plan_id")
    private int planId;
    @Column(name="duration_days")
    private int duration;
    @Column(name="calculation_days")
    private int calculationDays;

    public int getCalculationDays() {
        return calculationDays;
    }

    public void setCalculationDays(int calculationDays) {
        this.calculationDays = calculationDays;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
