package in.truethic.hrmsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.truethic.hrmsapp.Adapter.TeamMembersAdapter;
import in.truethic.hrmsapp.Model.TeamMembersModel;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamMemberListActivity extends AppCompatActivity {

    TeamMembersAdapter mAdapter;
    List<TeamMembersModel> membersModels;
    Context mContext;
    SessionManager sessionManager;
    ProgressDialog dialog;
    String att_type = "NA";
    String TAG = "TEAMMEM";

    CheckBox cb_select_all;
    RecyclerView rv_team_members;

    Button btn_take_selfie;

    ImageView iv_back;
 //   CheckBox cb_select_all;

    private int PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_member_list);

        mContext = TeamMemberListActivity.this;
        ButterKnife.bind(TeamMemberListActivity.this);
        rv_team_members = findViewById(R.id.rv_team_members);
        cb_select_all = findViewById(R.id.cb_select_all);
        btn_take_selfie = findViewById(R.id.btn_take_selfie);
        iv_back = findViewById(R.id.back);

        sessionManager = new SessionManager(TeamMemberListActivity.this);

        dialog = new ProgressDialog(TeamMemberListActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);





        Globals.finalList.clear();

        membersModels = new ArrayList<>();

        mAdapter = new TeamMembersAdapter(mContext, membersModels);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        rv_team_members.setLayoutManager(layoutManager);
        rv_team_members.setHasFixedSize(true);
        rv_team_members.setAdapter(mAdapter);

        getTeamMembers();

        cb_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_select_all.isChecked()){
                    Globals.isSelectAll=true;
                    mAdapter.checkAll();
                }else{
                    Globals.isSelectAll=false;
                    mAdapter.unCheckAll();
                }
            }
        });


        btn_take_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTakeSelfie();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public void getTeamMembers() {

        try {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("attendanceType", Globals.att_type);
            jsonObject.addProperty("siteId", Globals.branchId);
            jsonObject.addProperty("teamId",  Globals.teamId);


            dialog.show();
            AppService appService = RetrofitInstance.getService();
            Call<JsonObject> call = appService.getMembersList("Bearer " + sessionManager.getStringData( Globals.tokent ), "application/json",jsonObject );
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    dialog.dismiss();
                    if (response.body() != null) {
                        Log.e(TAG, "1" + response.body().toString());

                        if (response.body().get("responseStatus").getAsInt() == 200) {


                            JsonArray membersList = response.body().getAsJsonArray("members");
                            membersModels.clear();
                            Globals.finalList.clear();
                            for (int i = 0; i < membersList.size(); i++) {
                                JsonObject memberObject = membersList.get(i).getAsJsonObject();
                                membersModels.add(new TeamMembersModel(memberObject.get("employeeName").getAsString(), memberObject.get("employeeId").getAsString(), memberObject.get("teamId").getAsString(),false));
                            }
                            //Globals.finalList.addAll(membersModels);
                            mAdapter.notifyDataSetChanged();

                            if (membersModels.size() < 1) {
                                btn_take_selfie.setVisibility(View.GONE);
                                ShowToast("No Data For Attendance");
                            }

                        } else {
                            btn_take_selfie.setVisibility(View.GONE);
                            ShowToast("No Data For Attendance");

                        }
                    } else {
                        btn_take_selfie.setVisibility(View.GONE);
                        ShowToast("Try again Later");
                    }


                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "2" + t.toString());

                }
            });
        } catch (Exception x) {
            //ShowToast.show(mContext,"Step 2");
            dialog.dismiss();
            x.printStackTrace();
            Log.e(TAG, x.toString());
        }
    }

    private void ShowToast(String msg) {
        Toast.makeText(TeamMemberListActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    public void gotoTakeSelfie() {

        //Toast.makeText(TeamMembersActivity.this,"Clicked",Toast.LENGTH_LONG).show();


        if (!checkPermission()) {

            requestPermission();

        } else {

            if (Globals.finalList.size() > 0) {
                Intent intent = new Intent(TeamMemberListActivity.this, TeamSelfieActivity.class);
                startActivity(intent);
                TeamMemberListActivity.this.finish();
            } else {
                ShowToast("No members available");
            }
        }

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(TeamMemberListActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        // requestPermission(Manifest.permission.CAMERA, PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {

        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);

        if (result1 == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;

    }
}