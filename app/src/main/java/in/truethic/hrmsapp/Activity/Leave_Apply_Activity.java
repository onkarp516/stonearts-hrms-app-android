package in.truethic.hrmsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.truethic.hrmsapp.Adapter.ApplyLeaveAdapter;
import in.truethic.hrmsapp.Model.Leave_Dashboard_Model;
import in.truethic.hrmsapp.Model.Leave_Dashboard_Response;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.DisplayDialogBox;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Leave_Apply_Activity extends AppCompatActivity {

    /*int RemainingCasual=0,RemainingEmergency=0,RemainingMedical=0;*/
    ProgressDialog progressDialog;

    /*TextView tv_Casual_RemainingDays,tv_Casual_used_days,tv_Casual_total_days,tv_Emergency_RemainingDays,
            tv_Emergency_used_days,tv_Emergency_total_days,tv_Medical_RemainingDays,tv_Medical_used_days,
            tv_Medical_total_days,tv_Casual_heading,tv_emergency_heading,tv_medical_heading;*/
    ImageView iv_back;
    /*AppCompatButton btn_Casual,btn_emergency,btn_medical;*/
    SessionManager sessionManager;
    ApplyLeaveAdapter applyLeaveAdapter;
    RecyclerView rv_ApplyLeave;
    AppCompatButton btn_leave_status;

    ArrayList<Leave_Dashboard_Model> lData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_apply);
        sessionManager = new SessionManager(Leave_Apply_Activity.this);
        rv_ApplyLeave = findViewById(R.id.rv_ApplyLeave);
        btn_leave_status = findViewById(R.id.btn_leave_status);
        lData = new ArrayList<>();

//------------------------Recycle View Inflated start Here for Display of Apply Leave List------------------------------------------------------------------
        applyLeaveAdapter =new ApplyLeaveAdapter(Leave_Apply_Activity.this,lData);
        LinearLayoutManager layoutManager = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false );
        rv_ApplyLeave.setLayoutManager( layoutManager );
        rv_ApplyLeave.setAdapter( applyLeaveAdapter );
//----------------------------------------------------------------------------------------------------------------------------------------------------------

        iv_back = findViewById(R.id.iv_back);

        btn_leave_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Leave_Apply_Activity.this, Leave_Status_Activity.class);
                startActivity(intent);
            }
        });

        progressDialog=new ProgressDialog(Leave_Apply_Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        getLeaveDashboard();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLeaveDashboard();
    }

    private void getLeaveDashboard() {
        progressDialog.show();
        AppService appService = RetrofitInstance.getService();
        Call<Leave_Dashboard_Response> call = appService.LeaveDashboard("Bearer " + sessionManager.getStringData(Globals.tokent));
        call.enqueue(new Callback<Leave_Dashboard_Response>() {
            @Override
            public void onResponse(Call<Leave_Dashboard_Response> call, Response<Leave_Dashboard_Response> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("Leave Dashboard====>>>", response.body().toString());

                    if(response.body().getResponseStatus()==200)
                    {
                        lData.clear();
                        lData.addAll(response.body().getResponse());
/*
                        Log.e("Leave Dashboard====>>>", lData.get(0).getName());
                        Log.e("Leave Dashboard====>>>", lData.get(1).getName());
                        Log.e("Leave Dashboard====>>>", lData.get(2).getName());

                        tv_Casual_heading.setText(lData.get(0).getName());
                        tv_emergency_heading.setText(lData.get(1).getName());
                        tv_medical_heading.setText(lData.get(2).getName());

                        RemainingCasual = Integer.parseInt(lData.get(0).getLeaves_allowed()) - Integer.parseInt(lData.get(0).getUsedleaves());
                        Log.e("RemainingCasual======>>",RemainingCasual+"");

                        tv_Casual_RemainingDays.setText(RemainingCasual+"");
                        tv_Casual_used_days.setText(lData.get(0).getUsedleaves());
                        tv_Casual_total_days.setText(lData.get(0).getLeaves_allowed());

                        RemainingEmergency = Integer.parseInt(lData.get(1).getLeaves_allowed()) - Integer.parseInt(lData.get(1).getUsedleaves());
                        Log.e("RemainingEmergency===>>",RemainingEmergency+"");

                        tv_Emergency_RemainingDays.setText(RemainingEmergency+"");
                        tv_Emergency_used_days.setText(lData.get(1).getUsedleaves());
                        tv_Emergency_total_days.setText(lData.get(1).getLeaves_allowed());

                        RemainingMedical = Integer.parseInt(lData.get(2).getLeaves_allowed()) - Integer.parseInt(lData.get(2).getUsedleaves());
                        Log.e("RemainingMedical=====>>",RemainingMedical+"");

                        tv_Medical_RemainingDays.setText(RemainingMedical+"");
                        tv_Medical_used_days.setText(lData.get(2).getUsedleaves());
                        tv_Medical_total_days.setText(lData.get(2).getLeaves_allowed());
*/                 applyLeaveAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        DisplayDialogBox.showDialogBox(Leave_Apply_Activity.this,"Please Retry or Contact Admin!","Leave Alert");
                    }
                }
            }

            @Override
            public void onFailure(Call<Leave_Dashboard_Response> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("ERS:", "Something went wrong in Leave Dashboard");
            }
        });
    }
}