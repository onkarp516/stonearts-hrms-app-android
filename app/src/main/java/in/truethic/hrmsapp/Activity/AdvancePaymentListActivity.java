package in.truethic.hrmsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.truethic.hrmsapp.Adapter.Advance_Payment_List_Adapter;
import in.truethic.hrmsapp.Model.List_of_Advance_Pay_Model;
import in.truethic.hrmsapp.Model.List_of_Advance_Pay_Response;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.DisplayDialogBox;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvancePaymentListActivity extends AppCompatActivity {
    ImageView iv_back;
    RecyclerView RV_first;
    Advance_Payment_List_Adapter advance_payment_list_adapter;
    AppCompatButton btn_advance_pay_req;
    SessionManager sessionManager;
    ArrayList<List_of_Advance_Pay_Model> aData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_payment_list);
        sessionManager = new SessionManager( AdvancePaymentListActivity.this );
        aData = new ArrayList<>();
        advance_payment_list_adapter = new Advance_Payment_List_Adapter(AdvancePaymentListActivity.this,aData,sessionManager);
        iv_back = findViewById( R.id.iv_back );
        RV_first = findViewById( R.id.RV_first );
        btn_advance_pay_req = findViewById( R.id.btn_advance_pay_req );

//------------------------Recycle View Inflated start Here for Display of Advance Payment List------------------------------------------------------------------

        LinearLayoutManager layoutManager = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false );
        RV_first.setLayoutManager( layoutManager );
        RV_first.setAdapter( advance_payment_list_adapter );

//----------------------------------------------------------------------------------------------------------------------------------------------------------

        btn_advance_pay_req.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdvancePaymentListActivity.this,Advance_Payment_Activity.class);
                startActivity( i );
            }
        } );

//----------------------------------List of Advance Pay API Start Here----------------------------------------------------------------------
       AdvancePayList();
//------------------------------------------------------------------------------------------------------------------------------------------

        iv_back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

    }

    @Override
    protected void onResume() {
        super.onResume();
        AdvancePayList();
    }

    private void AdvancePayList() {
        /*try {*/

            AppService appService = RetrofitInstance.getService();
            Call<List_of_Advance_Pay_Response> call = appService.AdvancePaymentList( "Bearer " + sessionManager.getStringData( Globals.tokent ) );
            call.enqueue( new Callback<List_of_Advance_Pay_Response>() {
                @Override
                public void onResponse(Call<List_of_Advance_Pay_Response> call, Response<List_of_Advance_Pay_Response> response)
                {
                    if (response.isSuccessful())
                    {
                        if(response.body().getResponse().size() > 0) {
                            Log.d( "Advance_pay_size==>>", "" + response.body().getResponse().size() );
                            Toast.makeText( AdvancePaymentListActivity.this, "" + response.body().getResponse().size(), Toast.LENGTH_SHORT );
                            aData.clear();
                            aData.addAll( response.body().getResponse() );

                            Log.d( "Adv Pay list", "onResponse:" + response.body().toString() );

                            advance_payment_list_adapter.notifyDataSetChanged();
                        }
                        else {
                            Log.e( "Advance Pay List Page", "Data List is empty"  );
                            // DisplayDialogBox.showDialogBox(Activity_Advance_Payment_List.this, "Advance Pay not applied Yet. List is Empty", "Advance Pay Alert" );
                        }

                    }else {
                        DisplayDialogBox.showDialogBox(AdvancePaymentListActivity.this, "Server not Responding. Contact Administrator", "Server Alert" );
                    }
                }

                @Override
                public void onFailure(Call<List_of_Advance_Pay_Response> call, Throwable t) {
                    Log.d( "ERS", "error while List of Advance Pay API Call" );
                    Log.d("ERS", "onFailure: "+t.getMessage());
                    t.printStackTrace();
                    DisplayDialogBox.showDialogBox(AdvancePaymentListActivity.this,"Server is Down. Contact Administrator", "Server Alert" );
                }
            } );

        /*} catch (Exception e) {
            Log.e( "Error List of Adv Pay", e.toString() );
        }*/
    }
}