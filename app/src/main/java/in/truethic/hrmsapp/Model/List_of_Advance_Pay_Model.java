package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List_of_Advance_Pay_Model
{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("dateOfRequest")
    @Expose
    private String dateOfRequest;
    @SerializedName("requestAmount")
    @Expose
    private Integer requestAmount;
    @SerializedName("reason")
    @Expose
    private Object reason;
    @SerializedName("paymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("paidDate")
    @Expose
    private String paidDate;
    @SerializedName("paidAmount")
    @Expose
    private Object paidAmount;
    @SerializedName("noOfInstallments")
    @Expose
    private Integer noofInstallments;
    @SerializedName("installmentAmount")
    @Expose
    private Object installmentAmount;
    @SerializedName("remark")
    @Expose
    private Object remark;
    @SerializedName("approvedBy")
    @Expose
    private Object approvedBy;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateOfRequest() {
        return dateOfRequest;
    }

    public void setDateOfRequest(String dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    public Integer getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(Integer requestAmount) {
        this.requestAmount = requestAmount;
    }

    public Object getReason() {
        return reason;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public Object getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Object paidAmount) {
        this.paidAmount = paidAmount;
    }
    public Object getNoofInstallments() {
        return noofInstallments;
    }
    public void setNoofInstallments(Integer noofInstallments) {
        this.noofInstallments = noofInstallments;
    }
    public Object getInstallmentAmount() {
        return installmentAmount;
    }
    public void setInstallmentAmount(Object installmentAmount) {
        this.installmentAmount = installmentAmount;
    }
    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public Object getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Object approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
