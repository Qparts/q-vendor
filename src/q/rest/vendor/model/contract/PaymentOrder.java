package q.rest.vendor.model.contract;

import java.io.Serializable;
import java.util.Date;

public class PaymentOrder implements Serializable {
    private long id;
    private char salesType;//S = subscription , Q = quotation , C = cart
    private char status;
    private char paymentMethod;//V = card , W = wire
    private String gateway;//TAP , Moyassar , Wire
    private long customerId;// if sales type is Q or C
    private int vendorId; // if sales type is S
    private int vendorUserId;// if sales type is S
    private long quotationId;
    private long cartId;//if sales type is C
    private int planId;// if sales type is S
    private int optionDuration;// if sales type is S
    private int optionDurationActual;
    private int optionId;
    private Integer promoId;
    private double baseAmount;
    private double planDiscount;// if sales type is S
    private double promoDiscount;
    private double vatPercentage;
    private Date planStartDate;
    private Date planEndDate;
    private Date created;
    private int countryId;
    private String gatewayTransactionId;
    private String gatewayMessage;
    private String gatewayStatus;
    private String gatewayResponseCode;
    private String currency;
    private String paymentUrl;//for 3d secure
    private int wireBankId;
    private Date wireApprovedDate;
    private int wireApprovedBy;
    private String extension;
    private String mimeType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public char getSalesType() {
        return salesType;
    }

    public void setSalesType(char salesType) {
        this.salesType = salesType;
    }

    public char getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(char paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getVendorUserId() {
        return vendorUserId;
    }

    public void setVendorUserId(int vendorUserId) {
        this.vendorUserId = vendorUserId;
    }

    public long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(long quotationId) {
        this.quotationId = quotationId;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getOptionDuration() {
        return optionDuration;
    }

    public void setOptionDuration(int optionDuration) {
        this.optionDuration = optionDuration;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(double baseAmount) {
        this.baseAmount = baseAmount;
    }

    public double getPlanDiscount() {
        return planDiscount;
    }

    public void setPlanDiscount(double planDiscount) {
        this.planDiscount = planDiscount;
    }

    public double getPromoDiscount() {
        return promoDiscount;
    }

    public void setPromoDiscount(double promoDiscount) {
        this.promoDiscount = promoDiscount;
    }

    public double getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(double vatPercentage) {
        this.vatPercentage = vatPercentage;
    }

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
    }

    public String getGatewayMessage() {
        return gatewayMessage;
    }

    public void setGatewayMessage(String gatewayMessage) {
        this.gatewayMessage = gatewayMessage;
    }

    public String getGatewayStatus() {
        return gatewayStatus;
    }

    public void setGatewayStatus(String gatewayStatus) {
        this.gatewayStatus = gatewayStatus;
    }

    public String getGatewayResponseCode() {
        return gatewayResponseCode;
    }

    public void setGatewayResponseCode(String gatewayResponseCode) {
        this.gatewayResponseCode = gatewayResponseCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public int getWireBankId() {
        return wireBankId;
    }

    public void setWireBankId(int wireBankId) {
        this.wireBankId = wireBankId;
    }

    public Date getWireApprovedDate() {
        return wireApprovedDate;
    }

    public void setWireApprovedDate(Date wireApprovedDate) {
        this.wireApprovedDate = wireApprovedDate;
    }

    public int getWireApprovedBy() {
        return wireApprovedBy;
    }

    public void setWireApprovedBy(int wireApprovedBy) {
        this.wireApprovedBy = wireApprovedBy;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public int getOptionDurationActual() {
        return optionDurationActual;
    }

    public void setOptionDurationActual(int optionDurationActual) {
        this.optionDurationActual = optionDurationActual;
    }
}
