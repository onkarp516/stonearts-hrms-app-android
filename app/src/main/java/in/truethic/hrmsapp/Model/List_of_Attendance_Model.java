package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List_of_Attendance_Model {

    @SerializedName("attendanceDate")
    @Expose
    private String attendanceDate;

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    @SerializedName("attendanceStatus")
    @Expose
    private String attendanceStatus;
    @SerializedName("shiftName")
    @Expose
    private String shiftName;

    @SerializedName("attendanceId")
    @Expose
    private Integer attendanceId;
    @SerializedName("checkInTime")
    @Expose
    private String checkInTime;
    @SerializedName("checkOutTime")
    @Expose
    private String checkOutTime;
    @SerializedName("totalTime")
    @Expose
    private String totalTime;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("workingHours")
    @Expose
    private Double workingHours;

    @SerializedName("finalDaySalaryType")
    @Expose
    private String finalDaySalaryType;

    @SerializedName("finalDaySalary")
    @Expose
    private Double finalDaySalary;

    @SerializedName("firstTaskStartTime")
    @Expose
    private String firstTaskStartTime;

    @SerializedName("lastTaskEndTime")
    @Expose
    private String lastTaskEndTime;

    @SerializedName("lunchTimeInMin")
    @Expose
    private String lunchTimeInMin;


    public String getFirstTaskStartTime() {
        return firstTaskStartTime;
    }

    public void setFirstTaskStartTime(String firstTaskStartTime) {
        this.firstTaskStartTime = firstTaskStartTime;
    }

    public String getLastTaskEndTime() {
        return lastTaskEndTime;
    }

    public void setLastTaskEndTime(String lastTaskEndTime) {
        this.lastTaskEndTime = lastTaskEndTime;
    }

    public String getLunchTimeInMin() {
        return lunchTimeInMin;
    }

    public void setLunchTimeInMin(String lunchTimeInMin) {
        this.lunchTimeInMin = lunchTimeInMin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Double workingHours) {
        this.workingHours = workingHours;
    }

    public String getFinalDaySalaryType() {
        return finalDaySalaryType;
    }

    public void setFinalDaySalaryType(String finalDaySalaryType) {
        this.finalDaySalaryType = finalDaySalaryType;
    }

    public Double getFinalDaySalary() {
        return finalDaySalary;
    }

    public void setFinalDaySalary(Double finalDaySalary) {
        this.finalDaySalary = finalDaySalary;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }


    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

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


}
