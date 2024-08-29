package in.truethic.hrmsapp.Activity;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;

import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.DisplayDialogBox;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;
import in.truethic.hrmsapp.Utils.ShowToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Salary_Slip_Activity extends AppCompatActivity {
    ImageView iv_back,iv_notification;
    DecimalFormat df = new DecimalFormat("####0.00");
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    TextView tv_basic,tv_pf,tv_pt,tv_esi,tv_net_sal,tv_total_sal,tv_net_salary,tv_site_filter,tv_current_month, tv_overtime;

    RelativeLayout rl_top_nav1;
    String final_month="";
    String[] Last_Six_Month_List = new String[6];
    Calendar c = Calendar.getInstance();
    Format formatter = new SimpleDateFormat("MMMM yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_slip);
        iv_back = findViewById(R.id.iv_back);
        iv_notification = findViewById(R.id.iv_notification);


        tv_basic = findViewById(R.id.tv_basic);
        tv_overtime = findViewById(R.id.tv_overtime);
        tv_pf = findViewById(R.id.tv_pf);
        tv_pt = findViewById(R.id.tv_pt);
        tv_esi = findViewById(R.id.tv_esi);
        tv_net_sal = findViewById(R.id.tv_net_sal);
        tv_total_sal = findViewById(R.id.tv_total_sal);
        tv_net_salary = findViewById(R.id.tv_net_salary);
        tv_current_month = findViewById(R.id.tv_current_month);
        rl_top_nav1 = findViewById(R.id.rl_top_nav1_calender);
        tv_site_filter = findViewById(R.id.tv_site_filter);

        sessionManager = new SessionManager(Salary_Slip_Activity.this);
        progressDialog=new ProgressDialog(Salary_Slip_Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);


        DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
        Date date = new Date();
        Log.d("Month",dateFormat.format(date));
        tv_current_month.setText(dateFormat.format(date));


        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Salary_Slip_Activity.this, NotificationActivity.class);
                startActivity(i);
            }
        });


        for (int i = 0; i < Last_Six_Month_List.length; i++) {
            Last_Six_Month_List[i] = formatter.format(c.getTime());
            c.add(Calendar.MONTH, -1);
        }
        tv_site_filter.setText(Last_Six_Month_List[0].toString());
        Globals.radioSelected = 0;
        rl_top_nav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new
                        BottomSheetDialog(Salary_Slip_Activity.this,R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(Salary_Slip_Activity.this).inflate(R.layout.bottomsheet_dailogbox_salary_month,
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
                RadioGroup rg = new RadioGroup(Salary_Slip_Activity.this); //create the RadioGroup
                rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL

                for(int i=0; i<Last_Six_Month_List.length; i++){
                    rb[i]  = new RadioButton(Salary_Slip_Activity.this);
                    rb[i].setText(Last_Six_Month_List[i].toString());
                    rb[i].setTextColor(Color.parseColor("#7C3694"));
//                    rb[i].setTextColor(Color.BLACK);
                    rb[i].setPadding(10,10,10,10);
                    rb[i].setId(i + 100);
                    if(i == Globals.radioSelected){
                        rb[i].setChecked(true);
                    }
                    int finalI = i;
                    rb[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Log.e("RadioMonth====>>>","Clicked at "+Last_Six_Month_List[finalI].toString());
                                final_month = Last_Six_Month_List[finalI].toString();
                                Log.e("FinalMonth====>>>",final_month);
                                Globals.radioSelected = finalI;
                                tv_site_filter.setText(final_month);
                                DateFormat originalFormat = new SimpleDateFormat( "MMMM yyyy", Locale.ENGLISH );
                                DateFormat targetFormat = new SimpleDateFormat( "MM-yyyy" );
                                DateFormat SecondTargetFortmat = new SimpleDateFormat("MMM yyyy");
                                Date date = null;
                                try
                                {
                                    date = originalFormat.parse(final_month);
                                    String attendance_dt = targetFormat.format( date );
                                    Log.e("actualMonth====>>>",attendance_dt);


                                    String second_dt = SecondTargetFortmat.format(date);
                                    tv_current_month.setText(second_dt);
                                    getSalarySlip(attendance_dt );
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    ShowToast.show(Salary_Slip_Activity.this,"date parse error onItem Select Listener");
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

        getSalarySlip("");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void getSalarySlip(String month) {
        try {
            progressDialog.show();
            AppService appService1 = RetrofitInstance.getService();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("currentMonth", month);
            Call<JsonObject> call1 = appService1.getSalarySheet("Bearer " + sessionManager.getStringData(Globals.tokent), jsonObject);
            call1.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        String net_salary;
                        Log.e("Salary Sheet:", response.body().toString());
                        if (response.body().get("responseStatus").getAsInt() != 200)
                        {
                            Log.e("Not Success==>>","Response not 200");
                            tv_basic.setText("₹ 0");
                            tv_pf.setText("₹ 0");
                            tv_pt.setText("₹ 0");
                            tv_esi.setText("₹ 0");
                            tv_net_sal.setText("₹ 0");
                            tv_net_salary.setText("₹ 0");
                            tv_total_sal.setText("₹ 0");
                        }
                        else
                        {
                            double basic_sal =response.body().get("response").getAsJsonObject().get("basic").getAsDouble();

                            Log.e("basic_sal===>>",basic_sal+"");

                            tv_basic.setText("₹ "+df.format(basic_sal));
                            if(response.body().get("response").getAsJsonObject().get("employeeHavePf").getAsBoolean())
                            {
                                double pf_amt = response.body().get("response").getAsJsonObject().get("pfAmount").getAsDouble();
                                tv_pf.setText("₹ "+df.format(pf_amt));
                            }

                            double overtime =response.body().get("response").getAsJsonObject().get("overtime").getAsDouble();
                            tv_overtime.setText("₹ "+df.format(overtime));

                            double pt_amt = response.body().get("response").getAsJsonObject().get("profTax").getAsDouble();
                            tv_pt.setText("₹ "+df.format(pt_amt));

                            if(response.body().get("response").getAsJsonObject().get("employeeHaveEsi").getAsBoolean())
                            {
                                double esi_amt = response.body().get("response").getAsJsonObject().get("esiAmount").getAsDouble();
                                tv_esi.setText("₹ "+df.format(esi_amt));
                            }

                            double net_sal = response.body().get("response").getAsJsonObject().get("netSalary").getAsDouble();
                            tv_net_sal.setText("₹ "+df.format(net_sal));
                            tv_net_salary.setText(df.format(net_sal));

                            double total_sal = response.body().get("response").getAsJsonObject().get("grossTotal").getAsDouble();
                            tv_total_sal.setText("₹ "+df.format(total_sal));
                        }

                    } else {
                        Log.e("Salary Sheet:", "response is not successful");
                        DisplayDialogBox.showDialogBox(Salary_Slip_Activity.this, "Please Retry!", "Salary Alert");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("Salary Sheet API Fail", "Salary Sheet API Fails while calling API");
                    DisplayDialogBox.showDialogBox(Salary_Slip_Activity.this, "Server is Down. Contact Administrator", "Server Alert");
                }
            });
        } catch (Exception e) {
            Log.e("Salary Sheet ERR API", "Error while Calling Salary Slip API");
            e.printStackTrace();
        }
    }
}