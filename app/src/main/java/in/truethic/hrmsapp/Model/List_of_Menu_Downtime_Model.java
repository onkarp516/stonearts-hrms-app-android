package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List_of_Menu_Downtime_Model
{
    @SerializedName("taskId")
    @Expose
    private Integer taskId;
    @SerializedName("taskType")
    @Expose
    private Integer taskType;
    @SerializedName("taskMasterStatus")
    @Expose
    private String taskMasterStatus;
    @SerializedName("workBreakName")
    @Expose
    private String workBreakName;
    @SerializedName("workBreakPaid")
    @Expose
    private String workBreakPaid;
    @SerializedName("workDone")
    @Expose
    private Boolean workDone;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("totalBreakTime")
    @Expose
    private Float totalBreakTime;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("totalBreak")
    @Expose
    private String totalBreak;
    @SerializedName("isBreakInTask")
    @Expose
    private Boolean isBreakInTask;

    public String getTotalBreak() {
        return totalBreak;
    }

    public void setTotalBreak(String totalBreak) {
        this.totalBreak = totalBreak;
    }

    public Float getTotalBreakTime() {
        return totalBreakTime;
    }

    public void setTotalBreakTime(Float totalBreakTime) {
        this.totalBreakTime = totalBreakTime;
    }

    public Boolean getBreakInTask() {
        return isBreakInTask;
    }

    public void setBreakInTask(Boolean breakInTask) {
        isBreakInTask = breakInTask;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getTaskMasterStatus() {
        return taskMasterStatus;
    }

    public void setTaskMasterStatus(String taskMasterStatus) {
        this.taskMasterStatus = taskMasterStatus;
    }

    public String getWorkBreakName() {
        return workBreakName;
    }

    public void setWorkBreakName(String workBreakName) {
        this.workBreakName = workBreakName;
    }

    public String getWorkBreakPaid() {
        return workBreakPaid;
    }

    public void setWorkBreakPaid(String workBreakPaid) {
        this.workBreakPaid = workBreakPaid;
    }

    public Boolean getWorkDone() {
        return workDone;
    }

    public void setWorkDone(Boolean workDone) {
        this.workDone = workDone;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
