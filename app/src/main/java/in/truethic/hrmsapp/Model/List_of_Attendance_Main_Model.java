package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class List_of_Attendance_Main_Model {
    @SerializedName("list")
    @Expose
    private List<List_of_Attendance_Model> list = null;

    @SerializedName("totalDays")
    @Expose
    private Integer totalDays;
    @SerializedName("pDays")
    @Expose
    private Integer pDays;
    @SerializedName("lDays")
    @Expose
    private Integer lDays;

    public List<List_of_Attendance_Model> getList() {
        return list;
    }

    public void setList(List<List_of_Attendance_Model> list) {
        this.list = list;
    }

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    public Integer getpDays() {
        return pDays;
    }

    public void setpDays(Integer pDays) {
        this.pDays = pDays;
    }

    public Integer getlDays() {
        return lDays;
    }

    public void setlDays(Integer lDays) {
        this.lDays = lDays;
    }
}
