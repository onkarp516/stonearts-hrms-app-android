package in.truethic.hrmsapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.truethic.hrmsapp.Model.List_of_Advance_Pay_Model;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Utils.SessionManager;

public class Advance_Payment_List_Adapter extends RecyclerView.Adapter<Advance_Payment_List_Adapter.Advance_Payment_List_Holder> {
    Context mContext;
    List<List_of_Advance_Pay_Model> tData;
    SessionManager sessionManager;
    public Advance_Payment_List_Adapter(Context mContext, List<List_of_Advance_Pay_Model> tData, SessionManager sessionManager) {
        this.mContext = mContext;
        this.tData = tData;
        this.sessionManager = sessionManager;
    }

    @Override
    public Advance_Payment_List_Holder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.advance_payment_row,parent,false );
        Advance_Payment_List_Holder viewHolder = new Advance_Payment_List_Holder( view );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Advance_Payment_List_Holder holder, int position) {
        holder.bindOrder( tData.get( position ),position );
    }

    @Override
    public int getItemCount() {
        return tData.size();
    }



    public class Advance_Payment_List_Holder extends RecyclerView.ViewHolder
    {
        TextView tv_req_dt,tv_req_amt,tv_status;
        RelativeLayout RL_part_info;
        public Advance_Payment_List_Holder(View itemView) {
            super( itemView );
            tv_req_dt = itemView.findViewById( R.id.tv_req_dt );
            tv_req_amt = itemView.findViewById( R.id.tv_req_amt );
            tv_status = itemView.findViewById( R.id.tv_status );
            RL_part_info = itemView.findViewById(R.id.RL_part_info);
        }

        public void bindOrder(List_of_Advance_Pay_Model list_of_advance_pay_model, int position)  {
            try
            {

                DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
                Date date = originalFormat.parse(list_of_advance_pay_model.getDateOfRequest());
                String formattedDate = targetFormat.format(date);
                tv_req_dt.setText( formattedDate );

                tv_req_amt.setText( list_of_advance_pay_model.getRequestAmount()+"" );

                tv_status.setText( list_of_advance_pay_model.getPaymentStatus() );

                RL_part_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView dtReq, amtReq, amtApproved, installments, installAmount;
                        ImageView closeButton;
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                        final View customLayout = layoutInflater.inflate( R.layout.advance_payment_details, null );
                        builder.setView( customLayout );
                        dtReq = customLayout.findViewById(R.id.tvDateRequested);
                        amtReq = customLayout.findViewById(R.id.tvRequestedAmount);
                        amtApproved = customLayout.findViewById(R.id.tvApprovedAmount);
                        installments = customLayout.findViewById(R.id.tvNoofInstallments);
                        installAmount = customLayout.findViewById(R.id.tvInstallmentAmt);
                        dtReq.setText(list_of_advance_pay_model.getDateOfRequest());
                        amtReq.setText(list_of_advance_pay_model.getRequestAmount().toString());
                        amtApproved.setText(list_of_advance_pay_model.getPaidAmount()!= null ? list_of_advance_pay_model.getPaidAmount().toString():"");
                        installments.setText(list_of_advance_pay_model.getNoofInstallments()!=null ? list_of_advance_pay_model.getNoofInstallments().toString():"");
                        installAmount.setText(list_of_advance_pay_model.getInstallmentAmount()!=null ? list_of_advance_pay_model.getInstallmentAmount().toString():"");
                        AlertDialog dialog = builder.create();
                        dialog.setCancelable( false );
                        dialog.show();
                        closeButton = customLayout.findViewById(R.id.ivClose);
                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                });

            }catch (Exception e)
            {
                e.printStackTrace();
                Log.d("Error End Downtime Api", e.toString());
            }
        }
    }
}
