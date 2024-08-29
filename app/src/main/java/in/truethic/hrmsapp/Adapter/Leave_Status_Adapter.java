package in.truethic.hrmsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.truethic.hrmsapp.Model.LeaveStatusModal;
import in.truethic.hrmsapp.R;

public class Leave_Status_Adapter extends RecyclerView.Adapter<Leave_Status_Adapter.Leave_Status_Holder> {
    Context mContext;
   List<LeaveStatusModal> tData;

    public Leave_Status_Adapter(Context mContext, List<LeaveStatusModal> tData) {
        this.mContext = mContext;
        this.tData = tData;
    }

    @NonNull
    @Override
    public Leave_Status_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.salary_status_row,parent,false );
        Leave_Status_Holder viewHolder = new Leave_Status_Holder( view );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Leave_Status_Holder holder, int position) {
        holder.bindOrder(tData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return tData.size();
    }

    public  class Leave_Status_Holder extends RecyclerView.ViewHolder {
        TextView tv_type,tv_status, tv_applied_on,tv_from,tv_to,tv_total_days, tv_comment;
        public Leave_Status_Holder(@NonNull View itemView) {
            super(itemView);
            tv_type = itemView.findViewById( R.id.tv_leave_type );
            tv_status = itemView.findViewById( R.id.tv_status );
            tv_applied_on = itemView.findViewById( R.id.tv_date_applied );
            tv_from = itemView.findViewById( R.id.tv_from );
            tv_to = itemView.findViewById( R.id.tv_to );
            tv_total_days = itemView.findViewById( R.id.tv_total_days );
            tv_comment = itemView.findViewById( R.id.tv_comment );
        }

        public void bindOrder(LeaveStatusModal leaveStatusModal, int position) {

            try{
                tv_status.setText( leaveStatusModal.getStatus() );
                tv_type.setText(leaveStatusModal.getLeaveType());
                tv_from.setText(tv_from.getText()+" "+leaveStatusModal.getLeaveFrom());
                tv_to.setText(tv_to.getText()+" "+leaveStatusModal.getLeaveTo());
                tv_total_days.setText(leaveStatusModal.getTotalDays());
                tv_applied_on.setText(leaveStatusModal.getAppliedOn());
                tv_comment.setText(leaveStatusModal.getComment() != null ? leaveStatusModal.getComment() : "");
            }catch (Exception e){

            }
        }
    }
}
