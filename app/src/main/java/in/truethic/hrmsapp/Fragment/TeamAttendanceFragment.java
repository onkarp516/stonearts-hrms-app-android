package in.truethic.hrmsapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.truethic.hrmsapp.Adapter.Attendance_List_Adapter;
import in.truethic.hrmsapp.Adapter.TeamAttendance_List_Adapter;
import in.truethic.hrmsapp.Model.List_of_Attendance_Model;
import in.truethic.hrmsapp.Model.List_of_Attendance_Response;
import in.truethic.hrmsapp.Model.TeamStatus_Model;
import in.truethic.hrmsapp.Model.TeamStatus_Response;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;
import in.truethic.hrmsapp.Utils.ShowToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamAttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamAttendanceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SessionManager sessionManager;

    ArrayList<TeamStatus_Model> aData;

    RecyclerView rv_team_attendanceList;

    TeamAttendance_List_Adapter teamAttendance_list_adapter;
    public TeamAttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeamAttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamAttendanceFragment newInstance(String param1, String param2) {
        TeamAttendanceFragment fragment = new TeamAttendanceFragment();
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
        View v =  inflater.inflate(R.layout.fragment_team_attendance, container, false);

        sessionManager = new SessionManager(getContext());

        rv_team_attendanceList = v.findViewById(R.id.rv_team_attendanceList);


        aData=new ArrayList<>();
        teamAttendance_list_adapter = new TeamAttendance_List_Adapter(getContext(),aData);

        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext(), LinearLayoutManager.VERTICAL, false );
        rv_team_attendanceList.setLayoutManager( layoutManager );
        rv_team_attendanceList.setAdapter(teamAttendance_list_adapter);

        getData();

        return v;
    }

    private void getData( ) {
        //-------------------------------------------------Attendance List API----------------------------------------------------------------------------
        try {
//            progressDialog.show();

            AppService appService = RetrofitInstance.getService();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty( "siteId", Globals.token_branchId );
            jsonObject.addProperty( "teamId", Globals.token_teamId );
            jsonObject.addProperty( "currentMonth", "2024-02" );
            Call<TeamStatus_Response> call = appService.getTeamAttStatusList( "Bearer " + sessionManager.getStringData( Globals.tokent ), jsonObject );
            call.enqueue( new Callback<TeamStatus_Response>() {
                @Override
                public void onResponse(Call<TeamStatus_Response> call, Response<TeamStatus_Response> response) {
//                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        Log.e( "atten_list", response.body().toString() );

                        if (response.body().getResponse() != null)
                        {
                            //Log.d( "Attendance API", "" + response.body().getResponse().size() );
                            if (response.body().getResponseStatus() == 200) {
                                aData.clear();
                                aData.addAll( response.body().getResponse());
                                Log.d( "Advance Payment:", response.body().toString());

                                teamAttendance_list_adapter.notifyDataSetChanged();
                            }
                        } else {
                            ShowToast.show( getContext(), "Attendance list is empty" );
                        }
                    }
                }

                @Override
                public void onFailure(Call<TeamStatus_Response> call, Throwable t) {
                    Log.d( "attendance list Api", "Attendance API fails........" );
//                    progressDialog.dismiss();
                }
            } );
        }
        catch (Exception e)
        {
            Log.d( "catch in attendance api", ""+e );
            e.printStackTrace();
        }
//--------------------------------------------------------------------------------------------------------------------------------------------
    }
}