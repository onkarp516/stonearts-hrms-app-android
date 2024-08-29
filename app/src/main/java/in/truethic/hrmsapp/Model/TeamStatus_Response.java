package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeamStatus_Response {

    @SerializedName("response")
    @Expose
    private List<TeamStatus_Model> response;

    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public List<TeamStatus_Model> getResponse() {
        return response;
    }

    public void setResponse(List<TeamStatus_Model> response) {
        this.response = response;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }


}
