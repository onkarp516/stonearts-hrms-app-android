package in.truethic.hrmsapp.Fragment;

import static in.truethic.hrmsapp.Utils.Globals.radioSelectedAttendance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
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
import java.util.List;
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

public class AttendanceFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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


    public AttendanceFragment() {
        // Required empty public constructor
    }

    public static AttendanceFragment newInstance(String param1, String param2) {
        AttendanceFragment fragment = new AttendanceFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_attendance, container, false);
        sessionManager = new SessionManager(getContext());

        view_pager = v.findViewById(R.id.view_pager);
        tab_layout = v.findViewById(R.id.tab_layout);


        selfAttendanceFragment = new SelfAttendanceFragment();
        teamAttendanceFragment = new TeamAttendanceFragment();


        setupViewPager(view_pager);

        tab_layout.setupWithViewPager(view_pager);

        view_pager.setOffscreenPageLimit(2);

        tab = tab_layout.getTabAt(0);

       /* tab_layout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#00A4F4"), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#919ca7"), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#919ca7"), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#919ca7"), PorterDuff.Mode.SRC_IN);*/
        tab.select();

        return v;
    }




    private void setupViewPager(ViewPager view_pager) {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter( getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT );
        viewPagerAdapter.addFragment(selfAttendanceFragment,"Self");
        viewPagerAdapter.addFragment(teamAttendanceFragment,"Team");
        view_pager.setAdapter(viewPagerAdapter);
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm, int behavior) {
            super( fm, behavior );
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add( fragment );
            fragmentTitle.add( title );
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get( position );
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get( position );
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


    }

}