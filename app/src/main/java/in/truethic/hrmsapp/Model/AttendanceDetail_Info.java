package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceDetail_Info {

    @SerializedName("attendanceDate")
    @Expose
    private String attendanceDate;

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    @SerializedName("checkInTime")
    @Expose
    private String checkInTime;
    @SerializedName("checkOutTime")
    @Expose
    private String checkOutTime;
    @SerializedName("totalTime")
    @Expose
    private String totalTime;

    @SerializedName("breakArray")
    @Expose
    private List<AttendanceDetail_Break_List_Model> breakList;

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }


    public List<AttendanceDetail_Break_List_Model> getBreakList() {
        return breakList;
    }

    public void setBreakList(List<AttendanceDetail_Break_List_Model> breakList) {
        this.breakList = breakList;
    }
}
