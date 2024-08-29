package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List_of_AdvancePayment_Model {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("dateOfRequest")
    @Expose
    private String dateOfRequest;

    @SerializedName("requestAmount")
    @Expose
    private Integer requestAmount;

    @SerializedName("paymentStatus")
    @Expose
    private String paymentStatus;


    @SerializedName("isInstallment")
    @Expose
    private Boolean isInstallment;

    @SerializedName("paidDate")
    @Expose
    private String paidDate;

    @SerializedName("paidAmount")
    @Expose
    private Integer paidAmount;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDateOfRequest(String dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    public void setRequestAmount(Integer requestAmount) {
        this.requestAmount = requestAmount;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setInstallment(Boolean installment) {
        isInstallment = installment;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Integer getId() {
        return id;
    }

    public String getDateOfRequest() {
        return dateOfRequest;
    }

    public Integer getRequestAmount() {
        return requestAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public Boolean getInstallment() {
        return isInstallment;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }


}
