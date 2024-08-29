package in.truethic.hrmsapp.Retrofit;

import com.google.gson.JsonObject;

import in.truethic.hrmsapp.Model.AttendanceDetail_Response;
import in.truethic.hrmsapp.Model.Break_Types_Response;
import in.truethic.hrmsapp.Model.LeaveStatusResponse;
import in.truethic.hrmsapp.Model.Leave_Dashboard_Response;
import in.truethic.hrmsapp.Model.List_of_Advance_Pay_Response;
import in.truethic.hrmsapp.Model.List_of_Attendance_Response;
import in.truethic.hrmsapp.Model.List_of_Leave_Response;
import in.truethic.hrmsapp.Model.List_of_Menu_Downtime_Response;
import in.truethic.hrmsapp.Model.Site_List_Response;
import in.truethic.hrmsapp.Model.TeamStatus_Response;
import in.truethic.hrmsapp.Model.Today_Break_Response;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AppService {

    @POST("/addEmployeeDeviceId")
    Call<JsonObject> RegisterCustomer(@Body JsonObject jsonObject);
    @GET("/getVersionCode")
    Call<JsonObject> getAppVersion();
    @POST("/mLogin")
    Call<JsonObject> loginUser(@Body JsonObject jsonObject);
    @GET("/mobile/checkAttendanceStatus")
    Call<JsonObject>  attendance_status(@Header("Authorization") String authorization);
    @POST("/listOfBreak")
    Call<Break_Types_Response> break_Types(@Header("Authorization") String authorization, @Body JsonObject jsonObject);
    @POST("/mobile/checkMobileNoExists")
    Call<JsonObject> mobileNumberValidation(@Body JsonObject jsonObject);
    @POST("/mobile/forgetPassword")
    Call<JsonObject> forgetPassword(@Body JsonObject jsonObject);
    @Multipart
    @POST("/mobile/saveAttendance")
    Call<JsonObject> saveAttendance(@Header("Authorization") String authorization, @Part("attendanceStatus") RequestBody status, @Part MultipartBody.Part file);
    @POST("/mobile/fetchEmployeeSettingTimes")
    Call<List_of_Menu_Downtime_Response> List_of_Menu_Downtime1(@Header("Authorization") String authorization, @Body JsonObject jsonObject);
    @POST("/mobile/startBreak")
    Call<JsonObject> StartTask(@Header("Authorization") String authorization,@Body JsonObject jsonObject);
    @POST("/mobile/endBreak")
    Call<JsonObject> EndTask(@Header("Authorization") String authorization,@Body JsonObject jsonObject);

    @POST("/mobile/getSingleDayAttendanceDetails")
    Call<JsonObject> todayAttendance(@Header("Authorization") String authorization,@Body JsonObject jsonObject);

    @POST("/getTodaysBreaks")
    Call<Today_Break_Response> todayBreak(@Header("Authorization") String authorization, @Body JsonObject jsonObject);
    @POST("/mobile/getAttendanceList")
        //Call<JsonObject> CurrentMonth(@Header("Authorization") String authorization,@Body JsonObject jsonObject);
    Call<List_of_Attendance_Response> CurrentMonth(@Header("Authorization") String authorization, @Body JsonObject jsonObject);
    @GET("/mobile/leaveMaster/listForSelection")
    Call<List_of_Leave_Response> Type_of_Leave(@Header("Authorization") String authorization);
    @POST("/mobile/applyLeave")
    Call<JsonObject> Leave_Info_Submit(@Header("Authorization") String authorization,@Body JsonObject jsonObject);
    @POST("/getCurrentMonthPayslip")
    Call<JsonObject>getSalarySheet(@Header("Authorization") String authorization,@Body JsonObject jsonObject);
    @GET("/mobile/leavesDashboard")
    Call<Leave_Dashboard_Response> LeaveDashboard(@Header("Authorization") String authorization);
    @POST("/mobile/getSingleDayAttendanceDetails")
    Call<AttendanceDetail_Response> attendanceDetail(@Header("Authorization") String authorization, @Body JsonObject jsonObject);
    @POST("/mobile/checkLeaveAvailability")
    Call<JsonObject>getLeaveAvailability(@Header("Authorization") String authorization,@Body JsonObject jsonObject);
    @POST("/mobile/getEmployeePersonalInfo")
    Call<JsonObject> getEmployeePersonalInfo(@Header("Authorization") String authorization, @Body JsonObject jsonObject);

    @GET("/mobile/advancePaymentDashboard")
    Call<JsonObject> getEmployeeAdvancePayment(@Header("Authorization") String authorization);

    @POST("/mobile/createAdvancePaymentRequest")
    Call<JsonObject> createAdvancePaymentRequest(@Header("Authorization") String authorization, @Body JsonObject jsonObject);

    @GET("/mobile/listOfAdvancePayment")
    Call<List_of_Advance_Pay_Response> AdvancePaymentList(@Header("Authorization") String authorization);

    @POST("/mobile/getEmpLeaveStatus")
    Call<LeaveStatusResponse> getAppliedLeaveStatus(@Header("Authorization") String authorization, @Body JsonObject jsonObject);

    @POST("/getTeamLeaderSites")
    Call<Site_List_Response> getSiteData(@Header("Authorization") String authorization, @Header("Content-Type") String content_type);


    @POST("/getAttendanceMembersList")
    Call<JsonObject> getMembersList(@Header("Authorization") String authorization, @Header("Content-Type") String content_type, @Body JsonObject jsonObject);

    @GET("/mobile/checkPunchtime")
    Call<JsonObject> getCheckPunchtime(@Header("Authorization") String authorization);

    @Multipart
    @POST("/mobile/saveTeamAttendance")
    Call<JsonObject> saveTeamAttendance(@Header("Authorization") String authorization, @Part("attendanceStatus") RequestBody attType, @Part MultipartBody.Part file, @Part("members") RequestBody members, @Part("latitude") RequestBody lattitude, @Part("longitude") RequestBody longitude, @Part("siteId") RequestBody siteid, @Part("teamId") RequestBody teamid);

    @POST("/mobile/getTeamAttStatusList")
        //Call<JsonObject> CurrentMonth(@Header("Authorization") String authorization,@Body JsonObject jsonObject);
    Call<TeamStatus_Response> getTeamAttStatusList(@Header("Authorization") String authorization, @Body JsonObject jsonObject);
}
