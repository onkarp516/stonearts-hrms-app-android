package in.truethic.hrmsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.truethic.hrmsapp.Model.AttendanceDetail_Break_List_Model;
import in.truethic.hrmsapp.R;


public class AttendanceDetail_Break_List_Adapter extends RecyclerView.Adapter<AttendanceDetail_Break_List_Adapter.AttendanceDetail_Break_List_Adapter_Holder>{
    Context context;
    List<AttendanceDetail_Break_List_Model> aData;

    public AttendanceDetail_Break_List_Adapter(Context context, List<AttendanceDetail_Break_List_Model> aData) {
        this.context = context;
        this.aData = aData;
    }

    @NonNull
    @Override
    public AttendanceDetail_Break_List_Adapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.break_row,parent,false );
        AttendanceDetail_Break_List_Adapter_Holder viewHolder = new AttendanceDetail_Break_List_Adapter_Holder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceDetail_Break_List_Adapter_Holder holder, int position) {
        holder.bindOrder( aData.get(position),position );
    }

    @Override
    public int getItemCount() {
        return aData.size();
    }

    public class AttendanceDetail_Break_List_Adapter_Holder extends RecyclerView.ViewHolder{
        TextView tv_break_start,tv_break_end,tv_total;
        public AttendanceDetail_Break_List_Adapter_Holder(@NonNull View itemView) {
            super(itemView);
            tv_break_start = itemView.findViewById(R.id.tv_break_start);
            tv_break_end =itemView.findViewById(R.id.tv_break_end);
            tv_total = itemView.findViewById(R.id.tv_total);
        }
        public void bindOrder(AttendanceDetail_Break_List_Model attendanceDetail_break_list_model, int position)
        {
           // tv_break_start.setText(attendanceDetail_break_list_model.getStartTime());
           // tv_break_end.setText(attendanceDetail_break_list_model.getEndTime());


            DateFormat originalFormat5 = new SimpleDateFormat("hh:mm:ss");
            DateFormat targetFormat5 = new SimpleDateFormat("hh:mm:ss");
            Date date5 = null;
            try {
                date5 = originalFormat5.parse(attendanceDetail_break_list_model.getStartTime());
                String  break_start_time = targetFormat5.format(date5);
                tv_break_start.setText(break_start_time);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            DateFormat originalFormat2 = new SimpleDateFormat("hh:mm:ss");
            DateFormat targetFormat2 = new SimpleDateFormat("hh:mm:ss");
            Date date2 = null;
            try {
                date2 = originalFormat2.parse(attendanceDetail_break_list_model.getEndTime());
                String break_end_time = targetFormat2.format(date2);
                tv_break_end.setText(break_end_time);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            tv_total.setText(attendanceDetail_break_list_model.getTotalTime());
        }
    }
}
