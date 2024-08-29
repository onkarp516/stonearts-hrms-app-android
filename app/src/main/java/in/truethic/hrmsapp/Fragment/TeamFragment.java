package in.truethic.hrmsapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import butterknife.OnClick;
import in.truethic.hrmsapp.Activity.LoginActivity;
import in.truethic.hrmsapp.Activity.SiteListActivity;
import in.truethic.hrmsapp.Activity.TeamMemberListActivity;
import in.truethic.hrmsapp.Model.Site_List_Response;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView iv_team_punchin,iv_team_punchout;
    SessionManager sessionManager;

    public TeamFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TeamFragment newInstance(String param1, String param2) {
        TeamFragment fragment = new TeamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_team, container, false);
        iv_team_punchin = v.findViewById(R.id.iv_team_punchin);
        iv_team_punchout = v.findViewById(R.id.iv_team_punchout);

        sessionManager = new SessionManager(getContext());

        iv_team_punchin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Globals.att_type = true;
                Intent i = new Intent(getContext(), SiteListActivity.class);
                startActivity(i);*/

                Log.e("Token=>",Globals.tokent);

                getAttendanceStatus();

    }
});

   iv_team_punchout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Globals.att_type = false;
        Intent intent_out = new Intent(getContext(), SiteListActivity.class);
        intent_out.putExtra("att_type", "punchout");
        startActivity(intent_out);
      //  TeamAttendanceActivity.this.finish();
    }
});

        return v;
    }

    private void getAttendanceStatus(){
        AppService appService = RetrofitInstance.getService();
        Call<JsonObject> call = appService.getCheckPunchtime("Bearer " + sessionManager.getStringData( Globals.tokent ));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //     dialog.dismiss();
                if (response.body() != null) {
                    //Log.e(TAG, "ST2" + response.body().toString());
                    if (response.body().getAsJsonObject().get("responseStatus").getAsInt() == 200) {
                        boolean puchInTimeStatus = response.body().getAsJsonObject().get("punchStatus").getAsBoolean();
                        if (puchInTimeStatus) {
                            Globals.att_type = true;
                            Intent intent_in = new Intent(getContext(), SiteListActivity.class);
                            intent_in.putExtra("att_type", "punchin");
                            startActivity(intent_in);
                            // TeamAttendanceActivity.this.finish();
                        } else {
                            ShowToast("You are exceeding PunchIn grace time ");
                        }
                    }
                } else {
                    ShowToast("Data Not Found");
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //  dialog.dismiss();
                Log.e("TAG=>", "2" + t.toString());
                //ShowToast("Try again Later");
            }
        });
    }

    private void ShowToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

}