package in.truethic.hrmsapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import in.truethic.hrmsapp.Model.AttendanceDetail_Break_List_Model;
import in.truethic.hrmsapp.Model.AttendanceDetail_Response;
import in.truethic.hrmsapp.Model.List_of_Attendance_Model;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.DisplayDialogBox;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Attendance_List_Adapter extends RecyclerView.Adapter<Attendance_List_Adapter.Attendance_List_Holder> {
    Context context;
    List<List_of_Attendance_Model> aData;

    public
    Attendance_List_Adapter(Context context, List<List_of_Attendance_Model> aData) {
        this.context = context;
        this.aData = aData;
    }

    @NonNull
    @Override
    public Attendance_List_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.attendance_row,parent,false );
        Attendance_List_Holder viewHolder = new Attendance_List_Holder( view );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Attendance_List_Holder holder, int position) {
        holder.bindOrder(aData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return aData.size();
    }

    public class Attendance_List_Holder extends RecyclerView.ViewHolder {

        TextView tv_checkIn_date,tv_checkIn,tv_checkOut,tv_day,tv_total;
        LinearLayout ll_today_punch_dt;
        SessionManager sessionManager;

        Integer BreakID;
        ArrayList<AttendanceDetail_Break_List_Model> attendanceDetail_break_list_models = new ArrayList<>();
        public Attendance_List_Holder(@NonNull View itemView) {
            super(itemView);
            tv_checkIn_date = itemView.findViewById(R.id.tv_checkIn_date);
            tv_checkIn = itemView.findViewById(R.id.tv_checkIn);
            tv_checkOut = itemView.findViewById(R.id.tv_checkOut);
            tv_day = itemView.findViewById(R.id.tv_day);
            ll_today_punch_dt = itemView.findViewById(R.id.ll_today_punch_dt1);
            sessionManager = new SessionManager(context);
        }

        public void bindOrder(List_of_Attendance_Model list_of_attendance_model, int position) {
            try {
                String attendance_status = list_of_attendance_model.getAttendanceStatus();
                String check_in = list_of_attendance_model.getCheckInTime();
                String check_out = list_of_attendance_model.getCheckOutTime();
                String attend_dt = list_of_attendance_model.getAttendanceDate();
                BreakID = list_of_attendance_model.getAttendanceId();


                ll_today_punch_dt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(list_of_attendance_model.getAttendanceStatus().equalsIgnoreCase("A"))
                        {
                            DisplayDialogBox.showDialogBox(context,"You had on Leave","Attendance Alert");
                        }
                        else if(list_of_attendance_model.getAttendanceStatus().equalsIgnoreCase("PL"))
                        {
                            DisplayDialogBox.showDialogBox(context,"You had on Paid Leave","Attendance Alert");
                        }
                        else
                        {
                        Log.e("attendanceDetail====>>","attendance row clicked");
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("attendanceId",BreakID);
                        AppService appService = RetrofitInstance.getService();


                        Call<AttendanceDetail_Response> call = appService.attendanceDetail("Bearer " + sessionManager.getStringData(Globals.tokent), jsonObject);
                        call.enqueue(new Callback<AttendanceDetail_Response>() {
                            @Override
                            public void onResponse(Call<AttendanceDetail_Response> call, Response<AttendanceDetail_Response> response) {
                                if(response.isSuccessful())
                                {
                                    attendanceDetail_break_list_models.clear();
                                    attendanceDetail_break_list_models.addAll(response.body().getResponse().getBreakList());

//                                    String Att_dt =
                                    String CheckIn_t = response.body().getResponse().getCheckInTime();
                                    String CheckOut_t = response.body().getResponse().getCheckOutTime() != null ? response.body().getResponse().getCheckOutTime():"";
                                    String attendanceDt = response.body().getResponse().getAttendanceDate();

                                    DateFormat originalFormat6 = new SimpleDateFormat("yyyy-MM-dd");
                                    DateFormat targetFormat6 = new SimpleDateFormat("dd MMM, EEEE");
                                    Date date6 = null;
                                    try {

//                                        date6 = originalFormat6.parse(Att_dt);
//                                        attendanceDt = targetFormat6.format(date6);
                                        Log.d("Att Detail API ERS:", "Something went Wrong into Attendance List");
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }

                                    DateFormat originalFormat5 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    DateFormat targetFormat5 = new SimpleDateFormat("hh:mm aa");
                                    Date date5 = null;
                                    try {
                                        if(CheckIn_t != null) {
                                            date5 = originalFormat5.parse(CheckIn_t);
                                            CheckIn_t = targetFormat5.format(date5);
                                        } else
                                            CheckIn_t = "";
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }

                                    DateFormat originalFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    DateFormat targetFormat2 = new SimpleDateFormat("hh:mm aa");
                                    Date date2 = null;
                                    try {
                                        if(CheckOut_t != null) {
                                            date2 = originalFormat2.parse(CheckOut_t);
                                            CheckOut_t = targetFormat2.format(date2);
                                        } else
                                            CheckOut_t = "";
                                    } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                    }

                                    Log.e("AttendanceDetail=====>>",response.body().toString());

                                    AlertDialog.Builder builder = new AlertDialog.Builder( context );
                                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                                    final View customLayout = layoutInflater.inflate( R.layout.break_history_row, null );
                                    builder.setView( customLayout );

                                    AlertDialog dialog = builder.create();
                                    dialog.setCancelable( true );
                                    dialog.show();

                                    TextView tv_today_dt,tv_checkIn,tv_checkOut,tv_total;
                                    AppCompatButton btn_Close;
                                    RecyclerView rv_breakList;
                                    AttendanceDetail_Break_List_Adapter attendanceDetail_break_list_adapter;

                                    tv_today_dt = customLayout.findViewById(R.id.tv_checkIn_date_break);
                                    tv_checkIn = customLayout.findViewById(R.id.tv_checkIn_break);
                                    tv_checkOut = customLayout.findViewById(R.id.tv_checkOut_break);

                                    rv_breakList = customLayout.findViewById(R.id.rv_breakList);


                                    tv_today_dt.setText(attendanceDt);
                                    tv_checkIn.setText(CheckIn_t);
                                    tv_checkOut.setText(CheckOut_t);


                                    attendanceDetail_break_list_adapter = new AttendanceDetail_Break_List_Adapter(context,attendanceDetail_break_list_models);

                                    //------------------------Recycle View Inflated start Here for Display of Attendance Details break List------------------------------------------------------------------
                                    LinearLayoutManager layoutManager = new LinearLayoutManager( context, LinearLayoutManager.VERTICAL, false );
                                    rv_breakList.setLayoutManager( layoutManager );
                                    rv_breakList.setAdapter( attendanceDetail_break_list_adapter );
                                    //----------------------------------------------------------------------------------------------------------------------------------------------------------
                                    attendanceDetail_break_list_adapter.notifyDataSetChanged();
                                    //break_list_adapter = new Break_List_Adapter(context,mData);

                                }
                            }

                            @Override
                            public void onFailure(Call<AttendanceDetail_Response> call, Throwable t) {
                                Log.d("Att Detail API ERS:", "Something went Wrong into Attendance List");
                            }
                        });
                        
                       }
                    }
                });

                tv_checkIn_date.setText(attend_dt);
                tv_checkIn.setText(check_in);
                tv_checkOut.setText(check_out);
                tv_day.setText(attend_dt);
                if (list_of_attendance_model.getAttendanceDate().length() > 0) {
                    DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    DateFormat targetFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Date date = originalFormat.parse(list_of_attendance_model.getAttendanceDate());
                    String attendance_dt = targetFormat.format(date);

                    DateFormat targetFormat1 = new SimpleDateFormat("EEEE");
                    Date date1 = originalFormat.parse(list_of_attendance_model.getAttendanceDate());
                    String attendance_dt1 = targetFormat1.format(date1);
                    tv_checkIn_date.setText(attendance_dt);
                    tv_day.setText(attendance_dt1);
                } else {
                    tv_checkIn_date.setText("DD/MM/YYYY");
                }

                if(attendance_status.equalsIgnoreCase("WO")){
                    tv_checkOut.setText("Sunday");
                    tv_checkOut.setTextColor(Color.RED);
                    tv_checkOut.setTextSize(14);
                }

                if (attendance_status.equalsIgnoreCase("PL"))
                {
                    tv_checkIn.setText("OnLeave");
                    tv_checkOut.setText("NA");
                }

                if (attendance_status.equalsIgnoreCase("A"))
                {
                    tv_checkIn.setText("Absent");
                    tv_checkOut.setText("NA");
                }

                if (attendance_status.equals("P")) {
                    DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);
                    DateFormat targetFormat = new SimpleDateFormat("hh:mm");
                    tv_checkIn.setText(targetFormat.format(originalFormat.parse(check_in)));
                    if(list_of_attendance_model.getCheckOutTime() != null){
                        tv_checkOut.setText(targetFormat.format(originalFormat.parse(check_out)));
                    }
                }
            } catch (Exception e) {
                Log.d("Adpter attendance list", "" + e);
                e.printStackTrace();
            }
        }
    }
}
