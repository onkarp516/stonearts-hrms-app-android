package in.truethic.hrmsapp.Fragment;

import static in.truethic.hrmsapp.Utils.Globals.radioSelectedAttendance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.truethic.hrmsapp.Adapter.Attendance_List_Adapter;
import in.truethic.hrmsapp.Model.List_of_Attendance_Model;
import in.truethic.hrmsapp.Model.List_of_Attendance_Response;
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
 * Use the {@link SelfAttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelfAttendanceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RadioButton radio_present,radio_absent,radio_not_login;
    RecyclerView rv_attendanceList;
    ImageView iv_calender_month;
    ArrayList<List_of_Attendance_Model> aData;
    Attendance_List_Adapter attendance_list_adapter;
    String[] Last_Six_Month_List = new String[6];
    Calendar c = Calendar.getInstance();
    Format formatter = new SimpleDateFormat("MMMM yyyy");
    SessionManager sessionManager;
    String final_month="";
    TextView tv_selected_Month;

    ViewPager view_pager;
    TabLayout tab_layout;
    TabLayout.Tab tab;

    SelfAttendanceFragment selfAttendanceFragment;
    TeamAttendanceFragment teamAttendanceFragment;

    public SelfAttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelfAttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelfAttendanceFragment newInstance(String param1, String param2) {
        SelfAttendanceFragment fragment = new SelfAttendanceFragment();
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
        View v =  inflater.inflate(R.layout.fragment_self_attendance, container, false);

        sessionManager = new SessionManager(getContext());

            tv_selected_Month = v.findViewById(R.id.tv_selected_Month);
        rv_attendanceList = v.findViewById(R.id.rv_attendanceList);
        iv_calender_month = v.findViewById(R.id.iv_calender_month);

        aData=new ArrayList<>();
        attendance_list_adapter = new Attendance_List_Adapter(getContext(),aData);

        //------------------------Recycle View Inflated start Here for Display of attendance List------------------------------------------------------------------
        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext(), LinearLayoutManager.VERTICAL, false );
        rv_attendanceList.setLayoutManager( layoutManager );
        rv_attendanceList.setAdapter(attendance_list_adapter);
        //----------------------------------------------------------------------------------------------------------------------------------------------------------

        iv_calender_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
            }
        });

        //-------------Last Six Month Name and Year fetching dynamically with Calender Class Object--------------------
        for (int i = 0; i < Last_Six_Month_List.length; i++) {
            Last_Six_Month_List[i] = formatter.format(c.getTime());
            c.add(Calendar.MONTH, -1);
        }
        tv_selected_Month.setText(Last_Six_Month_List[0]);
        radioSelectedAttendance=0;
        iv_calender_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new
                        BottomSheetDialog(getContext(),R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.bottomsheet_dailogbox_salary_month,
                        v.findViewById(R.id.bottomSheetContainer1));

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

                RadioButton rb_FirstMonth,rb_SecondMonth;
                ImageView iv_close;
                rb_FirstMonth = bottomSheetView.findViewById(R.id.rb_FirstMonth);
                rb_FirstMonth.setVisibility(View.GONE);
                rb_SecondMonth = bottomSheetDialog.findViewById(R.id.rb_SecondMonth);
                rb_SecondMonth.setVisibility(View.GONE);

                LinearLayout layout = (LinearLayout) bottomSheetDialog.findViewById(R.id.ll_sample);

                final RadioButton[] rb = new RadioButton[Last_Six_Month_List.length];
                RadioGroup rg = new RadioGroup(getContext()); //create the RadioGroup
                rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
                for(int i=0; i<Last_Six_Month_List.length; i++){
                    rb[i]  = new RadioButton(getContext());
                    rb[i].setText(Last_Six_Month_List[i].toString());
                    rb[i].setTextColor(Color.parseColor("#7C3694"));
                    rb[i].setPadding(10,10,10,10);
                    rb[i].setId(i + 100);
                    if(i==radioSelectedAttendance)
                    {
                        rb[i].setChecked(true);
                    }
                    int finalI = i;
                    rb[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Log.e("RadioMonth====>>>","Clicked at "+Last_Six_Month_List[finalI].toString());
                                final_month = Last_Six_Month_List[finalI].toString();
                                tv_selected_Month.setText(Last_Six_Month_List[finalI]);
                                radioSelectedAttendance = finalI;
                                Log.e("FinalMonth====>>>",final_month);
//                                tv_site_filter.setText(final_month);
                                DateFormat originalFormat = new SimpleDateFormat( "MMMM yyyy", Locale.ENGLISH );
                                DateFormat targetFormat = new SimpleDateFormat( "MM-yyyy" );
                                Date date = null;
                                try
                                {
                                    date = originalFormat.parse(final_month);
                                    String attendance_dt = targetFormat.format( date );
                                    Log.e("actualMonth====>>>",attendance_dt);
                                    getData(attendance_dt );
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    ShowToast.show(getContext(),"date parse error onItem Select Listener");
                                }
                                bottomSheetDialog.dismiss();
                            }
                        }
                    });
                    rg.addView(rb[i]);
                }
                layout.addView(rg);

                iv_close = bottomSheetView.findViewById(R.id.iv_close);
                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });

        getData("" );
//        getAttendanceData("march");


        return v;
    }

    private void getData(String dt) {
        //-------------------------------------------------Attendance List API----------------------------------------------------------------------------
        try {
//            progressDialog.show();
            String temp_date = dt;
            AppService appService = RetrofitInstance.getService();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty( "currentMonth", temp_date );
            Call<List_of_Attendance_Response> call = appService.CurrentMonth( "Bearer " + sessionManager.getStringData( Globals.tokent ), jsonObject );
            call.enqueue( new Callback<List_of_Attendance_Response>() {
                @Override
                public void onResponse(Call<List_of_Attendance_Response> call, Response<List_of_Attendance_Response> response) {
//                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        Log.e( "atten_list", response.body().toString() );

                        if (response.body().getResponse() != null)
                        {
                            //Log.d( "Attendance API", "" + response.body().getResponse().size() );
                            if (response.body().getResponseStatus() == 200) {
                                aData.clear();
                                aData.addAll( response.body().getResponse().getList());
                                Log.d( "Advance Payment:", response.body().toString());
                                /*tv_total_leaves_days.setText(  response.body().getResponse().getlDays().toString() );
                                tv_total_attendance_days.setText( response.body().getResponse().getpDays().toString() );
                                tv_total_days.setText(response.body().getResponse().getTotalDays().toString() );*/
                                //showToast( "attendance list api" );

                                attendance_list_adapter.notifyDataSetChanged();
                            }
                        } else {
                            ShowToast.show( getContext(), "Attendance list is empty" );
                        }
                    }
                }

                @Override
                public void onFailure(Call<List_of_Attendance_Response> call, Throwable t) {
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

    private void getAttendanceData(String finalDate) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("attendanceDate",finalDate);

        AppService appService = RetrofitInstance.getService();
        Call<List_of_Attendance_Response> call = appService.CurrentMonth("Bearer " +sessionManager.getStringData(Globals.tokent), jsonObject);
        call.enqueue(new Callback<List_of_Attendance_Response>() {
            @Override
            public void onResponse(Call<List_of_Attendance_Response> call, Response<List_of_Attendance_Response> response) {
                if (response.isSuccessful() && response.body() != null){
                    Log.e("Attendance List:", response.body().toString());
                }
                else {
                    Log.e("Attendance List:", "No Data");
                }
            }

            @Override
            public void onFailure(Call<List_of_Attendance_Response> call, Throwable t) {
                Log.e("Network Issue", "Network Issue");
            }
        });
    }
}