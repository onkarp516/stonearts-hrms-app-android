package in.truethic.hrmsapp.Adapter;

import android.content.Context;
import android.util.Log;
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

import in.truethic.hrmsapp.Model.Today_Break_Model;
import in.truethic.hrmsapp.R;
import java.util.List;

import in.truethic.hrmsapp.Model.List_of_Menu_Downtime_Model;

public class Break_List_Adapter extends RecyclerView.Adapter<Break_List_Adapter.Break_List_Holder> {
    Context context;
    List<Today_Break_Model> lData;

    public Break_List_Adapter(Context context, List<Today_Break_Model> lData) {
        this.context = context;
        this.lData = lData;
    }

    @NonNull
    @Override
    public Break_List_Adapter.Break_List_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.break_row,parent,false );
        Break_List_Holder viewHolder = new Break_List_Holder( view );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Break_List_Adapter.Break_List_Holder holder, int position) {
        holder.bindOrder( lData.get(position),position );
    }

    @Override
    public int getItemCount() {
        return lData.size();
    }

    public class Break_List_Holder extends RecyclerView.ViewHolder {
        TextView tv_break_start,tv_break_end,tv_total;
        DateFormat originalFormat = new SimpleDateFormat("hh:mm:ss");
        DateFormat targetFormat = new SimpleDateFormat("hh:mm:ss aa");

        public Break_List_Holder(@NonNull View itemView) {
            super(itemView);
            tv_total = itemView.findViewById(R.id.tv_total);
            tv_break_end = itemView.findViewById(R.id.tv_break_end);
            tv_break_start = itemView.findViewById(R.id.tv_break_start);
        }

        public void bindOrder(Today_Break_Model today_break_model, int position) {
            try
            {

                Date date = null;
                try
                {
                    date = originalFormat.parse(today_break_model.getStartTime());
                    String b_start_time = targetFormat.format(date);
                    tv_break_start.setText(b_start_time);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if(!today_break_model.getEndTime().equalsIgnoreCase(""))
                {
                    DateFormat originalFormat1 = new SimpleDateFormat("hh:mm:ss");
                    DateFormat targetFormat1 = new SimpleDateFormat("hh:mm:ss aa");
                    Date date1 = null;
                    try {
                        //  Log.e("chekOut============>>", totalTime);
                        date1 = originalFormat1.parse(today_break_model.getEndTime());
                        String b_end_time = targetFormat1.format(date1);
                        tv_break_end.setText(b_end_time);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                {
                    tv_break_end.setText("00:00:00");
                }

                tv_total.setText(today_break_model.getTotalTime()+" MIN");
            } catch (Exception e) {
                Log.d( "Leave adpter Rpt Api", "" + e );
                e.printStackTrace();
            }
        }
    }
}