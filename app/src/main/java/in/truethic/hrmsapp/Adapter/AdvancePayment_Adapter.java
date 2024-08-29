package in.truethic.hrmsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.truethic.hrmsapp.Model.List_of_AdvancePayment_Model;
import in.truethic.hrmsapp.Model.List_of_Advance_Pay_Model;
import in.truethic.hrmsapp.R;

public class AdvancePayment_Adapter extends RecyclerView.Adapter<AdvancePayment_Adapter.AdvancePayment_AdapterHolder>  {


    Context context;
    List<List_of_Advance_Pay_Model> aData;

    public AdvancePayment_Adapter(Context context, List<List_of_Advance_Pay_Model> aData) {
        this.context = context;
        this.aData = aData;
    }

    @NonNull
    @Override
    public AdvancePayment_Adapter.AdvancePayment_AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.withdrawal_history_row,parent,false );
        AdvancePayment_Adapter.AdvancePayment_AdapterHolder viewHolder = new AdvancePayment_Adapter.AdvancePayment_AdapterHolder( view );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdvancePayment_Adapter.AdvancePayment_AdapterHolder holder, int position) {
        holder.bindOrder( aData.get( position ),position );
    }

    @Override
    public int getItemCount() {
        return aData.size();
    }


    public class AdvancePayment_AdapterHolder extends RecyclerView.ViewHolder {

        TextView tv_salaryAmt,tv_salarydate;
        public AdvancePayment_AdapterHolder(@NonNull View itemView) {
            super(itemView);


            tv_salaryAmt = itemView.findViewById(R.id.tv_salaryAmt);
            tv_salarydate = itemView.findViewById(R.id.tv_salarydate);



        }

        public void bindOrder(List_of_Advance_Pay_Model List_of_AdvancePayment_Model, int position)  {


            tv_salaryAmt.setText(List_of_AdvancePayment_Model.getRequestAmount().toString()+".00 Rs");
            tv_salarydate.setText(List_of_AdvancePayment_Model.getDateOfRequest().toString());

        }
    }

}
