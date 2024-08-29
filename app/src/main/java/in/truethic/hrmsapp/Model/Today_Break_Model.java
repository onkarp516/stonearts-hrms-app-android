package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Today_Break_Model {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("breakName")
    @Expose
    private String breakName;
    @SerializedName("startTime")
    @Expose
    private String startTime;

    @SerializedName("endTime")
    @Expose
    private String endTime;

    @SerializedName("totalTime")
    @Expose
    private String totalTime;

    @SerializedName("breakStatus")
    @Expose
    private String breakStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBreakName() {
        return breakName;
    }

    public void setBreakName(String breakName) {
        this.breakName = breakName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getBreakStatus() {
        return breakStatus;
    }

    public void setBreakStatus(String breakStatus) {
        this.breakStatus = breakStatus;
    }
}
