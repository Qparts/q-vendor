package q.rest.vendor.model.contract.qvm;

import q.rest.vendor.model.entity.Branch;

public class QvmBranch {
    private Branch localBranch;
    private int qBranchId;
    private int qCityId;
    private String branchName;
    private String branchNameAr;
    private String branchId;
    private String cityName;
    private String cityNameAr;

    public QvmBranch() {
    }

    public Branch getLocalBranch() {
        return localBranch;
    }

    public void setLocalBranch(Branch localBranch) {
        this.localBranch = localBranch;
    }

    public QvmBranch(String branchName, String branchId, String cityName) {
        this.branchName = branchName;
        this.branchId = branchId;
        this.cityName = cityName;
    }

    public String getBranchNameAr() {
        return branchNameAr;
    }

    public void setBranchNameAr(String branchNameAr) {
        this.branchNameAr = branchNameAr;
    }

    public String getCityNameAr() {
        return cityNameAr;
    }

    public void setCityNameAr(String cityNameAr) {
        this.cityNameAr = cityNameAr;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getqBranchId() {
        return qBranchId;
    }

    public void setqBranchId(int qBranchId) {
        this.qBranchId = qBranchId;
    }

    public int getqCityId() {
        return qCityId;
    }

    public void setqCityId(int qCityId) {
        this.qCityId = qCityId;
    }

    @Override
    public String toString() {
        return "QvmBranch{" +
                "qBranchId=" + qBranchId +
                ", qCityId=" + qCityId +
                ", branchName='" + branchName + '\'' +
                ", branchNameAr='" + branchNameAr + '\'' +
                ", branchId='" + branchId + '\'' +
                ", cityName='" + cityName + '\'' +
                ", cityNameAr='" + cityNameAr + '\'' +
                '}';
    }
}
