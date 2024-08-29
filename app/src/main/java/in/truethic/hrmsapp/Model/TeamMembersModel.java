package in.truethic.hrmsapp.Model;

public class TeamMembersModel {

    String employeeName,empployeeId,teamId;
    boolean isChecked;

    public TeamMembersModel(String employeeName, String empployeeId, String teamId, boolean isChecked) {
        this.employeeName = employeeName;
        this.empployeeId = empployeeId;

        this.teamId=teamId;
        this.isChecked=isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmpployeeId() {
        return empployeeId;
    }

    public void setEmpployeeId(String empployeeId) {
        this.empployeeId = empployeeId;
    }


    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
