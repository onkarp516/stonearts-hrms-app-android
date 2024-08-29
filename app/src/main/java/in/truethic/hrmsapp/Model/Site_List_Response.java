package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Site_List_Response {


    @SerializedName("response")
    @Expose
    private List<SiteDataModel> response;

    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;




    public List<SiteDataModel> getResponse() {
        return response;
    }

    public void setResponse(List<SiteDataModel> response) {
        this.response = response;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }




}
