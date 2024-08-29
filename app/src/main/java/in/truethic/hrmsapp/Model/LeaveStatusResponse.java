package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaveStatusResponse {

    @SerializedName("response")
    @Expose
    private List<LeaveStatusModal> response = null;

    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public List<LeaveStatusModal> getResponse() {
        return response;
    }

    public void setResponse(List<LeaveStatusModal> response) {
        this.response = response;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }
}
