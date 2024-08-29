package in.truethic.hrmsapp.Model;

public class FinalTeamAttendaceModel {

    String employeeId;
    String teamId;

    public FinalTeamAttendaceModel(String employeeId, String teamId) {
        this.employeeId = employeeId;
        this.teamId = teamId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
