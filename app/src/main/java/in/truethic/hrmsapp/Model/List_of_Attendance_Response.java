package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List_of_Attendance_Response {
    @SerializedName("response")
    @Expose
    private List_of_Attendance_Main_Model response = null;

    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public List_of_Attendance_Main_Model getResponse() {
        return response;
    }

    public void setResponse(List_of_Attendance_Main_Model response) {
        this.response = response;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }
}
