package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class List_of_Advance_Pay_Response
{
    @SerializedName("response")
    @Expose
    private List<List_of_Advance_Pay_Model> response = null;

    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public List<List_of_Advance_Pay_Model> getResponse() {
        return response;
    }

    public void setResponse(List<List_of_Advance_Pay_Model> response) {
        this.response = response;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

}
