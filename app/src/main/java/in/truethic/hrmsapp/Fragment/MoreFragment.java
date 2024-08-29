package in.truethic.hrmsapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import in.truethic.hrmsapp.Activity.Achievements_Activity;
import in.truethic.hrmsapp.Activity.AdvancePaymentListActivity;
import in.truethic.hrmsapp.Activity.Advance_Payment_Activity;
import in.truethic.hrmsapp.Activity.Apply_Leave_Activity;
import in.truethic.hrmsapp.Activity.Leave_Apply_Activity;
import in.truethic.hrmsapp.Activity.Personal_Info_Activity;
import in.truethic.hrmsapp.Activity.Salary_Slip_Activity;
import in.truethic.hrmsapp.R;

public class MoreFragment extends Fragment {
    RelativeLayout rl_personal_info,rl_attendance,rl_salary_slip,rl_leave_apply,rl_advance_payment,rl_achievements;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public MoreFragment() {
        // Required empty public constructor
    }

    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
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
        View v = inflater.inflate(R.layout.fragment_more, container, false);
        rl_personal_info = v.findViewById(R.id.rl_personal_info);
        rl_attendance = v.findViewById(R.id.rl_attendance);
        rl_salary_slip = v.findViewById(R.id.rl_salary_slip);
        rl_leave_apply = v.findViewById(R.id.rl_leave_apply);
        rl_advance_payment = v.findViewById(R.id.rl_advance_payment);
        rl_achievements = v.findViewById(R.id.rl_achievements);

        rl_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Personal_Info_Activity.class);
                startActivity(i);
            }
        });
        rl_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new AttendanceFragment()).commit();
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setSelectedItemId(R.id.navigation_attendance);
            }
        });
        rl_salary_slip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Salary_Slip_Activity.class);
                startActivity(i);
            }
        });
        rl_leave_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Leave_Apply_Activity.class);
                startActivity(i);
            }
        });
        rl_advance_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AdvancePaymentListActivity.class);
                startActivity(i);
            }
        });
        rl_achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Achievements_Activity.class);
                startActivity(i);
            }
        });
        return v;
    }
}