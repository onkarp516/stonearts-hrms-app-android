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
import in.truethic.hrmsapp.Model.TeamStatus_Model;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.DisplayDialogBox;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamAttendance_List_Adapter extends RecyclerView.Adapter<TeamAttendance_List_Adapter.TeamAttendance_List_Holder>{

    Context context;
    List<TeamStatus_Model> aData;

    public
    TeamAttendance_List_Adapter(Context context, List<TeamStatus_Model> aData) {
        this.context = context;
        this.aData = aData;
    }


    @NonNull
    @Override
    public TeamAttendance_List_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.team_attendance_row,parent,false );
        TeamAttendance_List_Holder viewHolder = new TeamAttendance_List_Holder( view );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAttendance_List_Holder holder, int position) {
        holder.bindOrder(aData.get(position), position);

    }

    @Override
    public int getItemCount() {
        return aData.size();
    }

    public class TeamAttendance_List_Holder extends RecyclerView.ViewHolder {

        TextView tv_checkIn_date,tv_checkIn,tv_checkOut,tv_day,tv_total;
        public TeamAttendance_List_Holder(@NonNull View itemView) {
            super(itemView);

            tv_checkIn_date = itemView.findViewById(R.id.tv_checkIn_date);
            tv_checkIn = itemView.findViewById(R.id.tv_checkIn);
            tv_checkOut = itemView.findViewById(R.id.tv_checkOut);
            tv_day = itemView.findViewById(R.id.tv_day);
        }

        public void bindOrder(TeamStatus_Model teamStatus_model, int position) {
            String check_in = teamStatus_model.getPunchInTime();
            String check_out = teamStatus_model.getPunchOutTime();
            String attend_dt = teamStatus_model.getAttendanceDate();
            tv_checkIn_date.setText(teamStatus_model.getEmployeeName());
            tv_checkIn.setText(check_in);
            tv_checkOut.setText(check_out);
            tv_day.setText(teamStatus_model.getAttendanceDate());

            /*DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("hh:mm");
            tv_checkIn.setText(targetFormat.format(check_in));
            if(teamStatus_model.getPunchOutTime() != null){
                tv_checkOut.setText(targetFormat.format(check_out));
            }*/

            if(!check_out.equalsIgnoreCase("")) {
                DateFormat originalFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                DateFormat targetFormat2 = new SimpleDateFormat("hh:mm:ss aa");
                Date date2 = null;
                try {
                    Log.e("chekOut============>>", check_out);
                    date2 = originalFormat2.parse(check_out);
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


            if(!check_in.equalsIgnoreCase("")) {
                DateFormat originalFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                DateFormat targetFormat2 = new SimpleDateFormat("hh:mm:ss aa");
                Date date2 = null;
                try {
                    Log.e("chekOut============>>", check_in);
                    date2 = originalFormat2.parse(check_in);
                    String check_in_time = targetFormat2.format(date2);
                    tv_checkIn.setText(check_in_time);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                tv_checkIn.setText("00:00:00");
            }

        }
    }

}
