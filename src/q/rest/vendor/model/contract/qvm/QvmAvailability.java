package q.rest.vendor.model.contract.qvm;

import java.util.Date;

public class QvmAvailability {
    private QvmBranch branch;
    private Integer quantity;
    private Date offerEnd;

    public QvmAvailability() {
    }

    public QvmAvailability(String name, String id, String cityName, Integer quantity) {
        this.branch = new QvmBranch(name, id, cityName);
        this.quantity = quantity;
    }

    public QvmBranch getBranch() {
        return branch;
    }

    public Date getOfferEnd() {
        return offerEnd;
    }

    public void setOfferEnd(Date offerEnd) {
        this.offerEnd = offerEnd;
    }

    public void setBranch(QvmBranch branch) {
        this.branch = branch;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
