package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Break_Types_Model {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("breakName")
    @Expose
    private String breakName;

    /*public Break_Types_Model(Integer id, String breakName, String fromTime, String toTime, String createdDate) {
        this.id = id;
        this.breakName = breakName;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.createdDate = createdDate;
    }*/

    @SerializedName("fromTime")
    @Expose
    private String fromTime;

    @SerializedName("itoTimed")
    @Expose
    private String toTime;

    @SerializedName("createdDate")
    @Expose
    private String createdDate;

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

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
