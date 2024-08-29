package in.truethic.hrmsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.DisplayDialogBox;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Manage_Profile_Activity extends AppCompatActivity {
    ImageView iv_back,iv_notification;

    TextView tv_emp_name,tv_emp_designation,tv_emp_email,tv_emp_phone_number,tv_emp_dob,tv_emp_add,tv_blood_group,tv_emp_joiningDate;

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);
        iv_back = findViewById(R.id.iv_back);
        iv_notification = findViewById(R.id.iv_notification);
        tv_emp_name = findViewById(R.id.tv_emp_name);
        tv_emp_designation = findViewById(R.id.tv_emp_designation);
        tv_emp_email = findViewById(R.id.tv_emp_email);
        tv_emp_phone_number = findViewById(R.id.tv_emp_phone_number);
        tv_emp_dob = findViewById(R.id.tv_emp_dob);
        tv_emp_add = findViewById(R.id.tv_emp_add);
        tv_blood_group = findViewById(R.id.tv_blood_group);
        tv_emp_joiningDate = findViewById(R.id.tv_emp_joiningDate);

        sessionManager = new SessionManager(Manage_Profile_Activity.this);

        getEmployeePersonalInfo();
        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Manage_Profile_Activity.this, NotificationActivity.class);
                startActivity(i);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(Manage_Profile_Activity.this,Personal_Info_Activity.class);
                startActivity(i);*/
                finish();
            }
        });

    }

    private void getEmployeePersonalInfo() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("isOwner", false);

        AppService appService = RetrofitInstance.getService();
        Call<JsonObject> call = appService.getEmployeePersonalInfo("Bearer " + sessionManager.getStringData( Globals.tokent ), jsonObject );
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful() && response != null) {
                    if(response.body().get("responseStatus").getAsInt()==200)
                    {
                        Log.e("ZonalDashboardRes==>",response.body().toString());
                        String employeeName = response.body().get("response").getAsJsonObject().get("employeeName").getAsString();
                        Log.e("employeeName==>",employeeName);

                        String designation = response.body().get("response").getAsJsonObject().get("designation").getAsString();
                        Log.e("designation==>",designation);

                        String dob = response.body().get("response").getAsJsonObject().get("dob").getAsString();
                        Log.e("dob==>",dob);

                        String contact = response.body().get("response").getAsJsonObject().get("contact").getAsString();
                        Log.e("contact==>",contact);

                        String address = response.body().get("response").getAsJsonObject().get("address").getAsString();
                        Log.e("address==>",address);

                        String workEmailId = response.body().get("response").getAsJsonObject().get("workEmailId").getAsString();
                        Log.e("workEmailId==>",workEmailId);

                        String bloodGroup = response.body().get("response").getAsJsonObject().get("bloodGroup").getAsString();
                        Log.e("bloodGroup==>",bloodGroup);

                        String joiningDate = response.body().get("response").getAsJsonObject().get("joiningDate").getAsString();
                        Log.e("joiningDate==>",joiningDate);

//joiningDate
//  bloodGroup
                        tv_blood_group.setText(joiningDate);
                        tv_blood_group.setText(bloodGroup);
                        tv_emp_email.setText(workEmailId);
                        tv_emp_add.setText(address);
                        tv_emp_phone_number.setText(contact);
                        tv_emp_dob.setText(dob);
                        tv_emp_designation.setText(designation);
                        tv_emp_name.setText(employeeName);


                    }
                    else
                    {
                        DisplayDialogBox.showDialogBox(Manage_Profile_Activity.this, "Please Retry!", "Zonal Head Info Alert");
                    }
                }
                else
                {
                    DisplayDialogBox.showDialogBox(Manage_Profile_Activity.this, "Contact Admin!", "Zonal Head Info Alert");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("stateHDash_onFailure==>",t.toString());
            }
        });
    }
}