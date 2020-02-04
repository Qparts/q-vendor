package q.rest.vendor.model.contract.qvm;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class QvmObject implements Serializable {
    private char source;//L = live integration, U = stock upload, Q = Q Parts
    private Integer vendorId;
    private String partNumber;
    private String brandPartNumber;
    private Object qpartsProducts;
    private String brand;
    private Double retailPrice;
    private Double wholesalesPrice;
    private Boolean available;
    private Date lastUpdate;
    private List<QvmAvailability> availability;
    private Double specialOfferPrice;
    private Date offerEnd;

    public Date getOfferEnd() {
        return offerEnd;
    }

    public void setOfferEnd(Date offerEnd) {
        this.offerEnd = offerEnd;
    }

    public Double getSpecialOfferPrice() {
        return specialOfferPrice;
    }

    public void setSpecialOfferPrice(Double specialOfferPrice) {
        this.specialOfferPrice = specialOfferPrice;
    }


    public char getSource() {
        return source;
    }

    public void setSource(char source) {
        this.source = source;
    }

    public String getBrandPartNumber() {
        return brandPartNumber;
    }

    public void setBrandPartNumber(String brandPartNumber) {
        this.brandPartNumber = brandPartNumber;
    }

    public Object getQpartsProducts() {
        return qpartsProducts;
    }

    public void setQpartsProducts(Object qpartsProducts) {
        this.qpartsProducts = qpartsProducts;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getWholesalesPrice() {
        return wholesalesPrice;
    }

    public void setWholesalesPrice(Double wholesalesPrice) {
        this.wholesalesPrice = wholesalesPrice;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public List<QvmAvailability> getAvailability() {
        return availability;
    }

    public void setAvailability(List<QvmAvailability> availability) {
        this.availability = availability;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
