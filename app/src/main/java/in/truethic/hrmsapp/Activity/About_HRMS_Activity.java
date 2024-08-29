package in.truethic.hrmsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import in.truethic.hrmsapp.R;

public class About_HRMS_Activity extends AppCompatActivity {
    ImageView iv_back,iv_notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_hrms);
        iv_back = findViewById(R.id.iv_back);
        iv_notification = findViewById(R.id.iv_notification);
        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(About_HRMS_Activity.this, NotificationActivity.class);
                startActivity(i);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(About_HRMS_Activity.this,Personal_Info_Activity.class);
                startActivity(i);*/
                finish();
            }
        });
    }
}