package in.truethic.hrmsapp.Fragment;

import static in.truethic.hrmsapp.Utils.SessionManager.attId;
import static in.truethic.hrmsapp.Utils.SessionManager.breakId;
import static in.truethic.hrmsapp.Utils.SessionManager.breakMasterId;
import static in.truethic.hrmsapp.Utils.SessionManager.remark;
import static in.truethic.hrmsapp.Utils.SessionManager.taskType;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.truethic.hrmsapp.Adapter.Break_List_Adapter;
import in.truethic.hrmsapp.Model.Break_Types_Model;
import in.truethic.hrmsapp.Model.Break_Types_Response;
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

public class BreakFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageView ib_main_circle;
    String checkInTime_h, checkOutTime_h, todayAttendance, attendanceId, checkInStatus_h, checkOutStatus_h, shiftToTime, currentTime, oldAttendanceDate, oldCheckInTime, oldCheckOutTime, totalTime, todayDt;
    ProgressDialog dialog;
    SessionManager sessionManager;
    RecyclerView rv_breakList;
    Break_List_Adapter break_list_adapter;
    ImageView base;
    TextView tv_end_start, tv_break_start, tv_total;
    String breakStatus = "";
    boolean punchIn_breakLock = false;
    boolean punchOut_breakLock = false;
    List<String> bList = new ArrayList<>();

    List<Break_Types_Model> breakTypesModels;
    List<Today_Break_Model> todayBreakModels;
    ArrayAdapter<String> adapter_break;
    int breakTypeId;
    private String mParam1;
    private String mParam2;

    public BreakFragment() {
        // Required empty public constructor
    }

    public static BreakFragment newInstance(String param1, String param2) {
        BreakFragment fragment = new BreakFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_break, container, false);
        sessionManager = new SessionManager(getContext());
        ib_main_circle = v.findViewById(R.id.ib_main_circle);

        tv_break_start = v.findViewById(R.id.tv_break_start);
        tv_end_start = v.findViewById(R.id.tv_end_start);
        tv_total = v.findViewById(R.id.tv_total);
        rv_breakList = v.findViewById(R.id.rv_breakList);

        breakTypesModels = new ArrayList<>();
        todayBreakModels = new ArrayList<>();
        break_list_adapter = new Break_List_Adapter(getContext(), todayBreakModels);

        getUpdatedStatus();
        Today_break();

        //------------------------Recycle View Inflated start Here for Display of break List------------------------------------------------------------------
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_breakList.setLayoutManager(layoutManager);
        rv_breakList.setAdapter(break_list_adapter);
        //-----------------------------------------------------------------------------------------------------------------------------------------------------

        ib_main_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("CurrentlyBreakStatus==>",breakStatus);

                if (breakStatus.equalsIgnoreCase("in-progress"))
                {
                    JsonObject jsonObject = new JsonObject();
                    AppService appService = RetrofitInstance.getService();
                    Log.e("breakId==BE==========>>", String.valueOf(sessionManager.getLongData("breakId")));
                    jsonObject.addProperty("breakId", sessionManager.getLongData("breakId"));
                    //jsonObject.addProperty("taskId", sessionManager.getLongData("breakId"));
                    Call<JsonObject> call = appService.EndTask("Bearer " + sessionManager.getStringData(Globals.tokent), jsonObject);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().get("responseStatus").getAsInt() == 200) {
                                    Today_break();
                                    ib_main_circle.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.break_start));
                                    ShowToast.show(getContext(), "Break End Successfully");
                                } else {
                                    // ShowToast.show(getContext(), "Response status is not 200");
                                    DisplayDialogBox.showDialogBox(getContext(), "Retry Again!", "Break Alert");
                                }
                            } else {
                                // ShowToast.show(getContext(), "Response body is null or unsuccessful");
                                DisplayDialogBox.showDialogBox(getContext(), "Retry Again or contact Admin!", "Break Alert");
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            dialog.dismiss();
                            ShowToast.show(getContext(), "End Downtime Fails....");
                        }
                    });
                }
                else {
                    if (punchIn_breakLock) {
                        DisplayDialogBox.showDialogBox(getContext(), "Please Do Check-In First", "Break Alert");
                    } else {
                        if (punchOut_breakLock) {
                            DisplayDialogBox.showDialogBox(getContext(), "Check-Out Done Already", "Break Alert");
                        } else {
                            selectBreakDailogbox();
                        }
                    }
                }
            }
        });

        return v;
    }

    private void selectBreakDailogbox() {
        break_list();
        final Dialog dialog1 = new Dialog(getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.history_row);
        dialog1.show();
        AppCompatButton btn_Close = dialog1.findViewById(R.id.btn_Close);
        AppCompatButton btn_submit = dialog1.findViewById(R.id.btn_submit);
        EditText edt_remark = dialog1.findViewById(R.id.edt_remark);

        AutoCompleteTextView av_text_view;
        av_text_view = dialog1.findViewById(R.id.av_text_view);
        ImageView close = dialog1.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        adapter_break = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, bList);
        av_text_view.setAdapter(adapter_break);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("breakMasterId", breakTypeId);
                Log.e("breakStart_BreakId==>", breakTypeId + "");
                jsonObject.addProperty("attendanceId", sessionManager.getStringData(attId));
                jsonObject.addProperty("remark", edt_remark.getText().toString());

                AppService appService = RetrofitInstance.getService();
                Call<JsonObject> call = appService.StartTask("Bearer " + sessionManager.getStringData(Globals.tokent), jsonObject);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                                dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().get("responseStatus").getAsInt() == 200) {
                                Log.d("setting time Successful", "" + response.body());

                                Log.e("breakId==============>>", response.body().get("breakId").getAsString());
                                sessionManager.setLongData("breakId", response.body().get("breakId").getAsLong());
                                ib_main_circle.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.break_end));
                                ShowToast.show(getContext(), "Break Started");
                                dialog1.dismiss();
//                                  downtime();
                                Today_break();
                            } else {
                                //ShowToast( "Response status is not 200" );
                                DisplayDialogBox.showDialogBox(getContext(), "Please Retry!", "Break Alert");
                            }
                        } else {
                            // ShowToast( "Response body is null or unsuccessful" );
                            DisplayDialogBox.showDialogBox(getContext(), "Please Retry!!", "Break Alert");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
//                                dialog.dismiss();
                        DisplayDialogBox.showDialogBox(getContext(), "Network Issue.. Please Retry!", "Server Alert");
                    }
                });


            }
        });
        av_text_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Break_Types_Model model = new Break_Types_Model();
                for (int j = 0; j < breakTypesModels.size(); j++) {
                    if (adapter_break.getItem(position).equalsIgnoreCase(breakTypesModels.get(j).getBreakName())) {
                        model = breakTypesModels.get(j);
                    }
                }
                breakTypeId = model.getId();
                //sessionManager.setIntData("masterBreakId",breakTypeId);
                Log.e("breakID==Adpter>>", breakTypeId + "");
            }
        });

        btn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
//                        ib_main_circle.setImageResource(R.drawable.break_end);
            }
        });
    }

    private void Today_break() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("attendanceId", sessionManager.getStringData(attId));

        AppService appService = RetrofitInstance.getService();
        Call<Today_Break_Response> call = appService.todayBreak("Bearer " + sessionManager.getStringData(Globals.tokent), jsonObject);
        call.enqueue(new Callback<Today_Break_Response>() {
            @Override
            public void onResponse(Call<Today_Break_Response> call, Response<Today_Break_Response> response) {
                if (response.isSuccessful() && response != null) {
                    Log.e("Break Types Res:", response.body().toString());
                    if (response.body().getResponseStatus() == 200) {
                        todayBreakModels.clear();
                        todayBreakModels.addAll(response.body().getToday_break());

                        for (Today_Break_Model model : todayBreakModels) {
                            if (model.getBreakStatus().equalsIgnoreCase("in-progress")) {
                                breakStatus = model.getBreakStatus();
                                tv_break_start.setText(model.getStartTime());
                                Log.e("Break_Status===>>", breakStatus + "");
                                ib_main_circle.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.break_end));
                                break;
                            }
                            else
                            {
                                breakStatus = model.getBreakStatus();
                            }
                        }
                        break_list_adapter.notifyDataSetChanged();
                    } else {
                        //DisplayDialogBox.showDialogBox(getContext(),"Pleaes Retry!","Break List Alert");
                        Log.e("Break Type List==>", "Break Type list empty");
                    }
                } else {
                    Log.e("Break Types Res:", "No Breaks");
                }
            }

            @Override
            public void onFailure(Call<Today_Break_Response> call, Throwable t) {
                Log.e("Break Status:", t.toString());
            }
        });
    }

    private void break_list() {
        AppService appService = RetrofitInstance.getService();
        JsonObject jsonObject = new JsonObject();
        Call<Break_Types_Response> call = appService.break_Types("Bearer " + sessionManager.getStringData(Globals.tokent), jsonObject);
        call.enqueue(new Callback<Break_Types_Response>() {
            @Override
            public void onResponse(Call<Break_Types_Response> call, Response<Break_Types_Response> response) {
                if (response.isSuccessful() && response != null) {
                    Log.e("Break Types Res:", response.body().toString());
                    if (response.body().getResponseStatus() == 200) {
                        breakTypesModels.clear();
                        breakTypesModels.addAll(response.body().getAll_break_type());
                        bList.clear();
                        for (Break_Types_Model model : breakTypesModels) {
                            bList.add(model.getBreakName());
                            Log.e("breakName", model.getBreakName());
                        }
                        adapter_break.notifyDataSetChanged();
                    } else {
                        //DisplayDialogBox.showDialogBox(getContext(),"Please Retry!","Break List Alert");
                        Log.e("Break Type List==>", "Break Type list empty");
                    }
                } else {
                    Log.e("Break Type List==>", "Break Type Response is empty or Null");
                }
            }

            @Override
            public void onFailure(Call<Break_Types_Response> call, Throwable t) {
                Log.e("Break Status:", t.toString());
            }
        });
    }

    private void getUpdatedStatus() {
        //-----------------------------------Attendance Status API Start Here-----------------------------------------------------------------
//        dialog.show();
        AppService appService1 = RetrofitInstance.getService();
        Call<JsonObject> call1 = appService1.attendance_status("Bearer " + sessionManager.getStringData(Globals.tokent));
        call1.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.e("Attendance Status:", response.body().toString());
                    try {
                        JsonObject jsonObject = response.body().getAsJsonObject("response");

//---------------Global variable value setting to checkIn and checkOut Status for locking of task and downtime after shift end---------------------

                        Globals.checkInStatus_G = jsonObject.get("checkInStatus").getAsString();
                        Globals.checkOutStatus_G = jsonObject.get("checkOutStatus").getAsString();
                        attendanceId = jsonObject.get("attendanceId").toString();
                        Log.e("Attendance_id========>>", attendanceId);
                        sessionManager.setStringData(attId, attendanceId);
//-------------------------------------------------------------------------------------------------------------------------------------------------

                        Log.e("ATT:", response.body().toString());

                        checkInStatus_h = jsonObject.get("checkInStatus").getAsString();
                        checkOutStatus_h = jsonObject.get("checkOutStatus").getAsString();
                        checkInTime_h = jsonObject.get("checkInTime").getAsString();
                        checkOutTime_h = jsonObject.get("checkOutTime").getAsString();
                        shiftToTime = jsonObject.get("shiftToTime").getAsString();
                        currentTime = jsonObject.get("currentTime").getAsString();
                        checkOutTime_h = jsonObject.get("checkOutTime").getAsString();
                        todayAttendance = jsonObject.get("todayAttendance").getAsString();
                        attendanceId = jsonObject.get("attendanceId").getAsString();
                        oldAttendanceDate = jsonObject.get("oldAttendanceDate").getAsString();
                        oldCheckInTime = jsonObject.get("oldCheckInTime").getAsString();
                        oldCheckOutTime = jsonObject.get("oldCheckOutTime").getAsString();
                        todayDt = jsonObject.get("currentDate").getAsString();
                        totalTime = jsonObject.get("totalTime").getAsString();
                        sessionManager.setStringData(attId, attendanceId);

                        Log.e("Attendance Id:", jsonObject.get("attendanceId") + "");
                        Log.d("in_s", checkInStatus_h);
                        Log.d("out_s", checkOutStatus_h);
                        Log.d("Check_in_Time", checkInTime_h);
                        Log.d("Check_out_Time", checkOutTime_h);
                        Log.e("shift_in_T", shiftToTime);
                        Log.e("cur_T", currentTime);
                        Log.e("today_att_S", todayAttendance);
                        Log.e("att_id", attendanceId);
                        Log.e("old_date", oldAttendanceDate);
                        Log.e("Today========>", todayDt);

                        validateAttendance();
                    } catch (Exception e) {
                        ShowToast.show(getContext(), "exception in attendance API in In-out Fragment" + e);
                        e.printStackTrace();
                    }
                } else
                    Log.e("AttendStatusAPI", "attendance status api fails in Login page");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                dialog.dismiss();
                Log.d("home_attendance api", "Api Fails while calling attendance status at In-out Fragment");
            }
        });
    }

    private void validateAttendance() {

        if (checkInStatus_h.equalsIgnoreCase("false") && checkOutStatus_h.equalsIgnoreCase("false")) {
            punchIn_breakLock = true;
        } else if (checkInStatus_h.equalsIgnoreCase("true") && checkOutStatus_h.equalsIgnoreCase("false")) {
            punchIn_breakLock = false;
            punchOut_breakLock = false;
        } else if (checkInStatus_h.equalsIgnoreCase("true") && checkOutStatus_h.equalsIgnoreCase("true")) {
            punchOut_breakLock = true;
        }
    }
}