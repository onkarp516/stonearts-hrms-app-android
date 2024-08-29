package in.truethic.hrmsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.truethic.hrmsapp.Fragment.MoreFragment;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Utils.Globals;

public class Personal_Info_Activity extends AppCompatActivity {
    ImageView iv_back,iv_notification;
    TextView tv_mange_profile,tv_user_name,tv_mobile_munmber,tv_user_id;
    RelativeLayout rl_hrms,rl_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        iv_back = findViewById(R.id.iv_back);
        rl_hrms = findViewById(R.id.rl_hrms);
        iv_notification = findViewById(R.id.iv_notification);
        tv_mange_profile = findViewById(R.id.tv_mange_profile);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_mobile_munmber = findViewById(R.id.tv_mobile_munmber);
        tv_user_id = findViewById(R.id.tv_user_id);
        rl_location = findViewById(R.id.rl_location);

        tv_user_id.setText("Code: "+Globals.userId);
        tv_user_name.setText("Name:"+" "+Globals.emp_name);
        tv_mobile_munmber.setText("91"+" "+Globals.emp_phone);
       // tv_user_id.setText();

        rl_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Personal_Info_Activity.this,Map_Activity.class);
                i.putExtra("mapFlag",true);
                startActivity(i);
            }
        });
        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Personal_Info_Activity.this, NotificationActivity.class);
                startActivity(i);
            }
        });
        tv_mange_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Personal_Info_Activity.this, Manage_Profile_Activity.class);
                startActivity(i);
            }
        });
        rl_hrms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Personal_Info_Activity.this, About_HRMS_Activity.class);
                startActivity(i);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

       /* iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Personal_Info_Activity.this, MoreFragment.class);
                startActivity(i);
            }
        });*/
    }
}