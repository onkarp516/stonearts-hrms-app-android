package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Today_Break_Response {
    @SerializedName("response")
    @Expose

    private List<Today_Break_Model> today_break = null;
    public List<Today_Break_Model> getToday_break(){
        return today_break;
    }

    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public void setToday_break(List<Today_Break_Model> today_break){
        this.today_break = today_break;
    }
    public Integer getResponseStatus(){return responseStatus;}
    public void setResponseStatus(Integer responseStatus) {this.responseStatus = responseStatus;}
}
