package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceDetail_Response {
    @SerializedName("response")
    @Expose
    private AttendanceDetail_Info response;

    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;


    public AttendanceDetail_Info getResponse() {
        return response;
    }

    public void setResponse(AttendanceDetail_Info response) {
        this.response = response;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }
}
