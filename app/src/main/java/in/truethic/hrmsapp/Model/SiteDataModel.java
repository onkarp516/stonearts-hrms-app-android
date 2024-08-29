package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SiteDataModel {

    @SerializedName("teamId")
    @Expose
    private Integer teamId;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchLat() {
        return branchLat;
    }

    public void setBranchLat(String branchLat) {
        this.branchLat = branchLat;
    }

    public Double getBranchLong() {
        return branchLong;
    }

    public void setBranchLong(Double branchLong) {
        this.branchLong = branchLong;
    }

    public Double getBranchRadius() {
        return branchRadius;
    }

    public void setBranchRadius(Double branchRadius) {
        this.branchRadius = branchRadius;
    }

    @SerializedName("branchId")
    @Expose
    private Integer branchId;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("branchLat")
    @Expose
    private String branchLat;
    @SerializedName("branchLong")
    @Expose
    private Double branchLong;
    @SerializedName("branchRadius")
    @Expose
    private Double branchRadius;
}
