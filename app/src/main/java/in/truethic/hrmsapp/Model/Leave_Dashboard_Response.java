package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Leave_Dashboard_Response {
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    @SerializedName("response")
    @Expose
    private List<Leave_Dashboard_Model> response = null;

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public List<Leave_Dashboard_Model> getResponse() {
        return response;
    }

    public void setResponse(List<Leave_Dashboard_Model> response) {
        this.response = response;
    }
}
