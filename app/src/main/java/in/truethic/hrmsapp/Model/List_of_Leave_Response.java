package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class List_of_Leave_Response {

    @SerializedName("response")
    @Expose
    private List<List_of_Leave_model> response;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public List<List_of_Leave_model> getResponse() {
        return response;
    }

    public void setResponse(List<List_of_Leave_model> response) {
        this.response = response;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }
}
