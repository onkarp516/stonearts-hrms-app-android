package in.truethic.hrmsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import in.truethic.hrmsapp.Adapter.Leave_Status_Adapter;
import in.truethic.hrmsapp.Model.LeaveStatusModal;
import in.truethic.hrmsapp.Model.LeaveStatusResponse;
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

public class Leave_Status_Activity extends AppCompatActivity {
    ImageView iv_notification,iv_back;
    RecyclerView recycler_view_of_leave;
    SessionManager sessionManager;
    List<LeaveStatusModal> lData;
    Leave_Status_Adapter leaveStatusAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_status);
        sessionManager =new SessionManager(this);
        iv_notification = findViewById(R.id.iv_notification);
        iv_back = findViewById(R.id.iv_back);
        recycler_view_of_leave = findViewById(R.id.recycler_view_of_leave);

        lData = new ArrayList<>();
        leaveStatusAdapter = new Leave_Status_Adapter(this,lData);

//------------------------Recycle View Inflated start Here for Display of Advance Payment List------------------------------------------------------------------

        LinearLayoutManager layoutManager = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false );
        recycler_view_of_leave.setLayoutManager( layoutManager );
        recycler_view_of_leave.setAdapter( leaveStatusAdapter );

//----------------------------------------------------------------------------------------------------------------------------------------------------------

        getLeaveStatusApi();


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Leave_Status_Activity.this, NotificationActivity.class);
                startActivity(i);
            }
        });
    }

    private void getLeaveStatusApi() {
        AppService appService = RetrofitInstance.getService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("yearMonth", "2024-02");
        Call<LeaveStatusResponse> call = appService.getAppliedLeaveStatus("Bearer " + sessionManager.getStringData( Globals.tokent ), jsonObject );
        call.enqueue(new Callback<LeaveStatusResponse>() {
            @Override
            public void onResponse(Call<LeaveStatusResponse> call, Response<LeaveStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("Leave Status >>>", response.body().toString());
                    if(response.body().getResponseStatus()==200)
                    {
                        lData.clear();
                        lData.addAll(response.body().getResponse());
                        leaveStatusAdapter.notifyDataSetChanged();
                       // Log.e("leaveStatus==>>",lData.get(0).getLeaveType());
                    }
                    else
                    {
                        DisplayDialogBox.showDialogBox(Leave_Status_Activity.this,"Yet No Leave Found","Leave Status Alert");
                    }
                }
            }

            @Override
            public void onFailure(Call<LeaveStatusResponse> call, Throwable t) {

            }
        });

    }
}