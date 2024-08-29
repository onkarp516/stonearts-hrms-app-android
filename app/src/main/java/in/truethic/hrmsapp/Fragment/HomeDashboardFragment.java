package in.truethic.hrmsapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import static in.truethic.hrmsapp.Utils.SessionManager.attId;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.truethic.hrmsapp.Activity.LoginActivity;
import in.truethic.hrmsapp.Activity.Map_Activity;
import in.truethic.hrmsapp.Activity.NotificationActivity;
import in.truethic.hrmsapp.Model.Break_Types_Model;
import in.truethic.hrmsapp.Model.List_of_Menu_Downtime_Model;
import in.truethic.hrmsapp.Model.List_of_Menu_Downtime_Response;
import in.truethic.hrmsapp.Model.Today_Break_Model;
import in.truethic.hrmsapp.Model.Today_Break_Response;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.DisplayDialogBox;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;
import in.truethic.hrmsapp.Utils.ShowToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeDashboardFragment extends Fragment {

    ImageView iv_logout,iv_notification,ib_main_circle;
    LinearLayout todayAttendanceDetails;
    boolean isVissible;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    String breakStatus="";
    List<Today_Break_Model> todayBreakModels;
    String checkInTime_h, checkOutTime_h, todayAttendance, attendanceId, checkInStatus_h, checkOutStatus_h, shiftToTime, currentTime, oldAttendanceDate, oldCheckInTime, oldCheckOutTime,totalTime,todayDt;
    TextView tv_break_start,tv_end_start,tv_checkIn,tv_checkOut,tv_total,tv_username,tv_today_dt,tv_designation,tv_done_for_the_day;
    ArrayList<List_of_Menu_Downtime_Model> mData;

    SessionManager sessionManager;


    public HomeDashboardFragment() {
        // Required empty public constructor
    }

    public static HomeDashboardFragment newInstance(String param1, String param2) {
        HomeDashboardFragment fragment = new HomeDashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        Log.e("IN-OUT Fragment==>>>>", "==============onResume of HomeFragment=================");
        getUpdatedStatus();
        Today_break();
        super.onResume();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_dashboard, container, false);
        sessionManager = new SessionManager(getContext());

        iv_logout = v.findViewById(R.id.iv_logout);
        iv_notification = v.findViewById(R.id.iv_notification);
        tv_designation = v.findViewById(R.id.tv_designation);
        tv_checkIn = v.findViewById(R.id.tv_checkIn);
        tv_designation = v.findViewById(R.id.tv_designation);
        tv_checkOut = v.findViewById(R.id.tv_checkOut);
        tv_username = v.findViewById(R.id.tv_username);
        tv_today_dt = v.findViewById(R.id.tv_today_dt);
        ib_main_circle = v.findViewById(R.id.ib_main_circle);
        todayAttendanceDetails = v.findViewById(R.id.todayAttendanceDetails);
        tv_done_for_the_day = v.findViewById(R.id.tv_done_for_the_day);
        mData = new ArrayList<>();
        todayBreakModels = new ArrayList<>();

        tv_username.setText(Globals.emp_username);
        Today_break();
        getUpdatedStatus();
        //sessionManager.setBooleanData(Globals.isLoggedIn, false);

        Log.e("emp_designation=>",Globals.emp_designation);

        tv_designation.setText(Globals.emp_designation);


        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NotificationActivity.class);
                startActivity(i);
            }
        });

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), LoginActivity.class);
                sessionManager.setBooleanData(Globals.isLoggedIn, false);
                startActivity(i);
            }
        });


        ib_main_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInStatus_h != null && checkOutStatus_h != null &&
                        checkInStatus_h.equalsIgnoreCase("true") && checkOutStatus_h.equalsIgnoreCase("true")) {
                    DisplayDialogBox.showDialogBox(getContext(),"You have done the punch-out","Punch-Out Alert");
                } else {
                    if (breakStatus.equalsIgnoreCase("in-progress")) {
                        DisplayDialogBox.showDialogBox(getContext(),"Break in-progress","Punch-Out Alert");
                    } else {
                        Intent i = new Intent(getContext(), Map_Activity.class);
                        startActivity(i);
                    }
                }
            }
        });
        return v;
    }

    private void Today_break() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("attendanceId", sessionManager.getStringData(attId));
        AppService appService = RetrofitInstance.getService();
        Call<Today_Break_Response> call = appService.todayBreak("Bearer "+sessionManager.getStringData(Globals.tokent),jsonObject);
        call.enqueue(new Callback<Today_Break_Response>() {
            @Override
            public void onResponse(Call<Today_Break_Response> call, Response<Today_Break_Response> response) {
                if (response.isSuccessful() && response != null) {
                    Log.e( "Break Types Res:", response.body().toString());
                    if (response.body().getResponseStatus() == 200) {

                        todayBreakModels.clear();
                        todayBreakModels.addAll(response.body().getToday_break());

                        for (Today_Break_Model model : todayBreakModels){
                            if(model.getBreakStatus().equalsIgnoreCase("in-progress"))
                            {
                                breakStatus = model.getBreakStatus();
                                Log.e("Break_Status===>>",breakStatus+"");
                            }
                        }
                    }
                    else
                    {
                        //DisplayDialogBox.showDialogBox(getContext(),"Pleaes Retry!","Break List Alert");
                        Log.e("Break Type List==>","Break Type list empty");
                    }
                }
                else {
                    Log.e( "Break Types Res:", "No Breaks");
                }
            }
            @Override
            public void onFailure(Call<Today_Break_Response> call, Throwable t) {
                Log.e( "Break Status:", t.toString());
            }
        });
    }
    private void getUpdatedStatus() {
        //-----------------------------------Attendance Status API Start Here-----------------------------------------------------------------
//        dialog.show();
        AppService appService1 = RetrofitInstance.getService();
        Call<JsonObject> call1 = appService1.attendance_status( "Bearer " + sessionManager.getStringData( Globals.tokent ) );
        call1.enqueue( new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.e( "Attendance Status:", response.body().toString() );
                    try {
                        JsonObject jsonObject = response.body().getAsJsonObject( "response" );

//---------------Global variable value setting to checkIn and checkOut Status for locking of task and downtime after shift end---------------------

                        Globals.checkInStatus_G = jsonObject.get( "checkInStatus" ).getAsString();
                        Globals.checkOutStatus_G = jsonObject.get( "checkOutStatus" ).getAsString();
                        attendanceId = jsonObject.get( "attendanceId" ).toString();
                        Log.e("Attendance_id========>>",attendanceId);
                        sessionManager.setStringData( attId, attendanceId );
//-------------------------------------------------------------------------------------------------------------------------------------------------

                        Log.e( "ATT:", response.body().toString() );

                        checkInStatus_h = jsonObject.get( "checkInStatus" ).getAsString();
                        checkOutStatus_h = jsonObject.get( "checkOutStatus" ).getAsString();
                        checkInTime_h = jsonObject.get( "checkInTime" ).getAsString();
                        checkOutTime_h = jsonObject.get( "checkOutTime" ).getAsString();
                        shiftToTime = jsonObject.get( "shiftToTime" ).getAsString();
                        currentTime = jsonObject.get( "currentTime" ).getAsString();
                        checkOutTime_h = jsonObject.get( "checkOutTime" ).getAsString();
                        todayAttendance = jsonObject.get( "todayAttendance" ).getAsString();
                        attendanceId = jsonObject.get( "attendanceId" ).getAsString();
                        oldAttendanceDate = jsonObject.get( "oldAttendanceDate" ).getAsString();
                        oldCheckInTime = jsonObject.get( "oldCheckInTime" ).getAsString();
                        oldCheckOutTime = jsonObject.get( "oldCheckOutTime" ).getAsString();
                        todayDt = jsonObject.get("currentDate").getAsString();
                        totalTime = jsonObject.get("totalTime").getAsString();
                        sessionManager.setStringData( attId, attendanceId );

                        Log.e("Attendance Id:",jsonObject.get( "attendanceId" )+"");
                        Log.d( "in_s", checkInStatus_h );
                        Log.d( "out_s", checkOutStatus_h );
                        Log.d( "Check_in_Time", checkInTime_h );
                        Log.d( "Check_out_Time", checkOutTime_h );
                        Log.e( "shift_in_T", shiftToTime );
                        Log.e( "cur_T", currentTime );
                        Log.e( "today_att_S", todayAttendance );
                        Log.e( "att_id", attendanceId );
                        Log.e( "old_date", oldAttendanceDate );
                        Log.e("Today========>",todayDt);

                        validateAttendance();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e( "Exception e: ", "exception in attendance API in In-out Fragment" + e );
                    }
                }else
                    Log.e("AttendStatusAPI", "attendance status api fails in Login page" );
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                dialog.dismiss();
                Log.d( "home_attendance api", "Api Fails while calling attendance status at In-out Fragment" );
            }
        } );
    }

    private void validateAttendance() {
        DateFormat originalFormat3 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat targetFormat3 = new SimpleDateFormat("dd-MM-yyyy");
        Date date3 = null;
        try {
            date3 = originalFormat3.parse(todayDt);
            String check_in_time = targetFormat3.format(date3);
            tv_today_dt.setText(check_in_time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if(checkInStatus_h.equalsIgnoreCase("false") && checkOutStatus_h.equalsIgnoreCase("false"))
        {
            Globals.canHePunchIn =true;
        }

        else if(checkInStatus_h.equalsIgnoreCase("true") && checkOutStatus_h.equalsIgnoreCase("false"))
        {
            Globals.canHePunchIn =false;
            ib_main_circle.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.check_out));

            DateFormat originalFormat5 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            DateFormat targetFormat5 = new SimpleDateFormat("hh:mm:ss aa");
            Date date5 = null;
            try {
                date5 = originalFormat5.parse(checkInTime_h);
                String check_in_time = targetFormat5.format(date5);
                tv_checkIn.setText(check_in_time);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            if(!checkOutTime_h.equalsIgnoreCase("")) {
                DateFormat originalFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                DateFormat targetFormat2 = new SimpleDateFormat("hh:mm:ss aa");
                Date date2 = null;
                try {
                    Log.e("chekOut============>>", checkOutTime_h);
                    date2 = originalFormat2.parse(checkOutTime_h);
                    String check_out_time = targetFormat2.format(date2);
                    tv_checkOut.setText(check_out_time);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                tv_checkOut.setText("00:00:00");
            }
            /*if(!totalTime.equalsIgnoreCase("0"))
            {
                DateFormat originalFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                DateFormat targetFormat2 = new SimpleDateFormat("hh:mm:ss aa");
                Date date2 = null;
                try {
                    Log.e("chekOut============>>", checkOutTime_h);
                    date2 = originalFormat2.parse(checkOutTime_h);
                    String check_out_time = targetFormat2.format(date2);
                    tv_checkOut.setText(check_out_time);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                tv_total.setText("00:00:00");
            }*/
        }
        else if (checkInStatus_h.equalsIgnoreCase("true") && checkOutStatus_h.equalsIgnoreCase("true"))
        {
            isVissible = true;
            todayAttendanceDetails.setVisibility(View.GONE);
            tv_done_for_the_day.setVisibility(View.VISIBLE);
            //Globals.canHePunchIn =false;
            ib_main_circle.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.done_for_the_day));


            DateFormat originalFormat5 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            DateFormat targetFormat5 = new SimpleDateFormat("hh:mm:ss aa");
            Date date5 = null;
            try {
                date5 = originalFormat5.parse(checkInTime_h);
                String check_in_time = targetFormat5.format(date5);
                tv_checkIn.setText(check_in_time);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            DateFormat originalFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            DateFormat targetFormat2 = new SimpleDateFormat("hh:mm:ss aa");
            Date date2 = null;
            try {
                Log.e("chekOut============>>",checkOutTime_h);
                date2 = originalFormat2.parse(checkOutTime_h);
                String check_out_time = targetFormat2.format(date2);
                tv_checkOut.setText(check_out_time);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            /*if(!totalTime.equalsIgnoreCase("0"))
            {
                DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                DateFormat targetFormat = new SimpleDateFormat("hh:mm:ss aa");
                Date date = null;
                try {
                    Log.e("chekOut============>>", totalTime);
                    date = originalFormat.parse(checkOutTime_h);
                    String check_out_time = targetFormat.format(date);
                    tv_checkOut.setText(check_out_time);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                tv_total.setText(totalTime);
            }
            else
            {
                tv_total.setText("00:00:00");
            }*/
        }
        /*else if (checkInStatus_h.equalsIgnoreCase("false") && checkOutStatus_h.equalsIgnoreCase("false")) {

        }*/
    }
}