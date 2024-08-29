package in.truethic.hrmsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.truethic.hrmsapp.Activity.Apply_Leave_Activity;
import in.truethic.hrmsapp.Model.Leave_Dashboard_Model;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Utils.ShowToast;

public class ApplyLeaveAdapter extends RecyclerView.Adapter<ApplyLeaveAdapter.ApplyLeaveHolder> {

    Context context;
    ArrayList<Leave_Dashboard_Model> lData;

    public ApplyLeaveAdapter(Context context, ArrayList<Leave_Dashboard_Model> lData) {
        this.context = context;
        this.lData = lData;
    }

    @NonNull
    @Override
    public ApplyLeaveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.apply_leave_row,parent,false );
        ApplyLeaveAdapter.ApplyLeaveHolder viewHolder = new ApplyLeaveAdapter.ApplyLeaveHolder( view );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ApplyLeaveHolder holder, int position) {
        holder.bindOrder( lData.get( position ),position );
    }

    @Override
    public int getItemCount() {
        return lData.size();
    }

    public class ApplyLeaveHolder extends RecyclerView.ViewHolder {
        TextView tv_leave_type,tv_RemainingDays,tv_used_days,tv_total_days;
        AppCompatButton btn_leave;
        public ApplyLeaveHolder(@NonNull View itemView) {
            super(itemView);
            tv_leave_type = itemView.findViewById(R.id.tv_leave_type);
            tv_RemainingDays = itemView.findViewById(R.id.tv_RemainingDays);
            tv_used_days = itemView.findViewById(R.id.tv_used_days);
            tv_total_days = itemView.findViewById(R.id.tv_total_days);
            btn_leave = itemView.findViewById(R.id.btn_leave);
        }

        public void bindOrder(Leave_Dashboard_Model leave_dashboard_model, int position) {
            tv_leave_type.setText(leave_dashboard_model.getName());
            tv_RemainingDays.setText(leave_dashboard_model.getRemainingleaves());
            tv_used_days.setText(leave_dashboard_model.getUsedleaves());
            tv_total_days.setText(leave_dashboard_model.getLeaves_allowed());

            btn_leave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowToast.show(context,"clicked "+leave_dashboard_model.getId());

                    Intent i = new Intent(context, Apply_Leave_Activity.class);
                    i.putExtra("leaveId",leave_dashboard_model.getId());
                    i.putExtra("leaveName",leave_dashboard_model.getName());
                    context.startActivity(i);
                }
            });
        }
    }
}
