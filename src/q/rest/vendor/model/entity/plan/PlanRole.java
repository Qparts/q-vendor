package q.rest.vendor.model.entity.plan;

import q.rest.vendor.model.entity.user.Role;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "vnd_plan_role")
@IdClass(PlanRole.PlanRolePK.class)
public class PlanRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne
    private Role role;

    @Id
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    @ManyToOne
    private Plan plan;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public static class PlanRolePK implements Serializable {
        private static final long serialVersionUID = 1L;
        protected int role;
        protected int plan;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PlanRolePK that = (PlanRolePK) o;
            return role == that.role &&
                    plan == that.plan;
        }

        @Override
        public int hashCode() {
            return Objects.hash(role, plan);
        }
    }
}
