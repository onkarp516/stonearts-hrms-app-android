package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Break_Types_Response {
    @SerializedName("response")
    @Expose

    private List<Break_Types_Model> all_break_type = null;
    public List<Break_Types_Model> getAll_break_type(){return all_break_type;}

    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public void setAll_break_type(List<Break_Types_Model> all_break_type){
        this.all_break_type = all_break_type;
    }
    public Integer getResponseStatus() {return responseStatus;}

    public void setResponseStatus(Integer responseStatus) {this.responseStatus = responseStatus;}
}
