package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceDetail_Break_List_Model {
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("totalTime")
    @Expose
    private String totalTime;

    @SerializedName("id")
    @Expose
    private Integer brakId;

    @SerializedName("breakName")
    @Expose
    private String breakName;

    @SerializedName("breakStatus")
    @Expose
    private String breakStatus;

    public Integer getBrakId() {
        return brakId;
    }

    public String getBreakName() {
        return breakName;
    }

    public void setBrakId(Integer brakId) {
        this.brakId = brakId;
    }

    public void setBreakName(String breakName) {
        this.breakName = breakName;
    }

    public void setBreakStatus(String breakStatus) {
        this.breakStatus = breakStatus;
    }

    public String getBreakStatus() {
        return breakStatus;
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
}
