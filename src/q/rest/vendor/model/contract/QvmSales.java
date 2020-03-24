package q.rest.vendor.model.contract;

import java.io.Serializable;
import java.util.Date;

public class QvmSales implements Serializable {
    private long id;
    private Date created;
    private char status;
    private PaymentOrder paymentOrder;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public PaymentOrder getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(PaymentOrder order) {
        this.paymentOrder = order;
    }
}
