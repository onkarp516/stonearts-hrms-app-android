package in.truethic.hrmsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.truethic.hrmsapp.Model.List_of_Leave_Response;
import in.truethic.hrmsapp.Model.List_of_Leave_model;
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

public class
Apply_Leave_Activity extends AppCompatActivity {
    ImageView iv_back,iv_notification;
    TextView tv_Reset;
    RelativeLayout rl_documents;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    ArrayAdapter<String> adapter;
    private boolean isVissible;
    int day, month, year, final_diff;
    String dateFrom, dateTo, total_d, Leave_remark="";
    EditText edt_Leave_type;
    List Leave_List = new ArrayList<>();
    List<List_of_Leave_model> list_of_leave_models = new ArrayList<>();
    EditText edt_total_days,edt_Leave_reason;
    TextView btn_submit_leave,edt_From_Date,edt_To_Date;
    String leaveId,leaveName;
    Long Leave_type_id;
    RelativeLayout rl_from,rl_to;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);
        sessionManager = new SessionManager(Apply_Leave_Activity.this);

        edt_Leave_type = findViewById(R.id.edt_Leave_type);
        iv_back = findViewById(R.id.iv_back);
        tv_Reset = findViewById(R.id.tv_Reset);
        rl_documents = findViewById(R.id.rl_documents);
        iv_notification = findViewById(R.id.iv_notification);

        edt_Leave_reason = findViewById(R.id.edt_Leave_reason);
        edt_total_days = findViewById(R.id.edt_total_days);
        edt_To_Date = findViewById(R.id.edt_To_Date1);
        edt_From_Date = findViewById(R.id.edt_From_Date1);
        btn_submit_leave = findViewById(R.id.tv_submit);

        rl_to = findViewById(R.id.rl_to);
        rl_from = findViewById(R.id.rl_from);

        progressDialog=new ProgressDialog(Apply_Leave_Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        leaveId = intent.getStringExtra("leaveId");
        leaveName = intent.getStringExtra("leaveName");
        Log.e("LeaveId=MainClass==>",leaveId);
        Log.e("LeaveName=MainClass==>",leaveName);

        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Apply_Leave_Activity.this, NotificationActivity.class);
                startActivity(i);
            }
        });
        tv_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*isVissible = true;
                rl_documents.setVisibility(View.VISIBLE);
                tv_upload.setVisibility(View.GONE);*/

                edt_From_Date.setText("");
                edt_To_Date.setText("");
                edt_total_days.setText("");
                edt_Leave_reason.setText("");
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Log.e("Globals Leave==>",Globals.leave_name);

       edt_Leave_type.setText(leaveName);
        btn_submit_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Leave_remark = edt_Leave_reason.getText().toString();

                if (TextUtils.isEmpty( edt_Leave_type.getText().toString() )
                        || TextUtils.isEmpty( edt_From_Date.getText().toString() )
                        || TextUtils.isEmpty( edt_To_Date.getText().toString() )) {
                    //ShowToast( "Please Enter All Fields" );
                    DisplayDialogBox.showDialogBox( Apply_Leave_Activity.this, "Please Enter All Fields", "Apply Leave Alert" );
                }
                else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Apply_Leave_Activity.this);
                    LayoutInflater layoutInflater = (LayoutInflater) Apply_Leave_Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View customLayout = layoutInflater.inflate(R.layout.confirmation_dailog, null);
                    builder.setView(customLayout);


                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.show();

                    AppCompatButton  btn_Yes;

                    btn_Yes = customLayout.findViewById(R.id.btn_Yes);

                    btn_Yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            progressDialog.show();
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("leaveId", leaveId);
                            jsonObject.addProperty("fromDate", dateFrom);
                            jsonObject.addProperty("toDate", dateTo);
                            jsonObject.addProperty("totalDays", total_d);
                            jsonObject.addProperty("reason", Leave_remark);
                            AppService appService = RetrofitInstance.getService();
                            Call<JsonObject> call = appService.Leave_Info_Submit("Bearer " + sessionManager.getStringData(Globals.tokent), jsonObject);
                            call.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    progressDialog.dismiss();
                                    if (response.isSuccessful()) {

                                        if(response.body().get("responseStatus").getAsInt()==200)
                                        {
                                            ShowToast("Leave Request Submitted successfully");
                                            //dialog.dismiss();
                                            finish();
                                        }
                                        else
                                        {
                                            DisplayDialogBox.showDialogBox(Apply_Leave_Activity.this,response.body().get("message").getAsString(),"Leave Alert");
                                            //dialog.dismiss();
                                        }
                                    } else {
                                        DisplayDialogBox.showDialogBox(Apply_Leave_Activity.this, "Server not Responding. Contact Administrator", "Server Alert");
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Log.d("Leave API ERS:", "Something went Wrong into Leave API");
                                    DisplayDialogBox.showDialogBox(Apply_Leave_Activity.this, "Server is Down. Contact Administrator", "Server Alert");
                                }
                            });

                        }
                    });
                }
            }
        });

       /* get_List_of_leave_type();
        adapter = new ArrayAdapter<String>( Apply_Leave_Activity.this, android.R.layout.simple_list_item_1, Leave_List );
        edt_Leave_type.setAdapter( adapter );
        edt_Leave_type.setTextColor( Color.BLACK );
*/
       /* edt_Leave_type.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                List_of_Leave_model model = new List_of_Leave_model();
                for (int i = 0; i < list_of_leave_models.size(); i++) {
                    if (adapter.getItem( position ).equalsIgnoreCase( list_of_leave_models.get( i ).getName() )) {
                        model = list_of_leave_models.get( i );
                    }

                }
                Leave_type_id = model.getId();

                //Toast.makeText( LeaveActivity.this,""+Leave_type_id,Toast.LENGTH_SHORT ).show();

                *//*List_of_Leave_model model1 = list_of_leave_models.get( position );
                Leave_type_id = model1.getId();*//*
            }
        } );*/
        rl_to.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                day = calendar.get( Calendar.DAY_OF_MONTH );
                month = calendar.get( Calendar.MONTH );
                year = calendar.get( Calendar.YEAR );
                DatePickerDialog datePickerDialog1 = new DatePickerDialog( Apply_Leave_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set( Calendar.DAY_OF_MONTH, dayOfMonth );
                        calendar.set( Calendar.MONTH, month );
                        calendar.set( Calendar.YEAR, year );
                        String date22 = new SimpleDateFormat( "dd-MMM-yyyy" ).format( calendar.getTimeInMillis() );
                        edt_To_Date.setText( date22 );

                        SimpleDateFormat parseFormat1 = new SimpleDateFormat( "dd-MMM-yyyy" );
                        try {
                            Date date11 = parseFormat1.parse( date22 );
                            dateTo = new SimpleDateFormat( "yyyy-MM-dd" ).format( date11 );
                            //ShowToast( dateTo );

                            int dateDifference = (int) getDateDiff( new SimpleDateFormat( "yyyy-MM-dd" ), dateFrom, dateTo );
                            final_diff = 1 + dateDifference;

                            if(final_diff > 0 ) {
                                total_d = "" + final_diff;
                                edt_total_days.setText("" + total_d);
                            }else {
                                DisplayDialogBox.showDialogBox(Apply_Leave_Activity.this,"Please select Proper Date Range","Alert");
                                edt_To_Date.setText("");
                                edt_From_Date.setText("");
                                return;
                            }
                            //ShowToast( ""+total_d );
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d( "Date Parse Error", "" + e );
                        }
                    }
                }, year, month, day );
                datePickerDialog1.getDatePicker().setMinDate( System.currentTimeMillis() - 1000 );
                datePickerDialog1.show();
            }
        } );


        rl_from.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                day = calendar.get( Calendar.DAY_OF_MONTH );
                month = calendar.get( Calendar.MONTH );
                year = calendar.get( Calendar.YEAR );

                Log.d( "DAY:", day + "-" + month + "-" + year );
                DatePickerDialog datePickerDialog = new DatePickerDialog( Apply_Leave_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set( Calendar.DAY_OF_MONTH, dayOfMonth );
                        calendar.set( Calendar.MONTH, month );
                        calendar.set( Calendar.YEAR, year );
                        String date = new SimpleDateFormat( "dd-MMM-yyyy" ).format( calendar.getTimeInMillis() );
                        edt_From_Date.setText( date );

                        SimpleDateFormat parseFormat = new SimpleDateFormat( "dd-MMM-yyyy" );
                        try {
                            Date date1 = parseFormat.parse( date );
                            dateFrom = new SimpleDateFormat( "yyyy-MM-dd" ).format( date1 );
                            // ShowToast( dateFrom );
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d( "Date Parse Error", "" + e );
                        }
                    }
                }, year, month, day );
                datePickerDialog.getDatePicker().setMinDate( System.currentTimeMillis() - 1000 );
                datePickerDialog.show();
            }
        } );

    }
    public void ShowToast(String msg) {
        Toast.makeText( Apply_Leave_Activity.this, msg, Toast.LENGTH_LONG ).show();
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*private void get_List_of_leave_type() {
        progressDialog.show();
        AppService appService = RetrofitInstance.getService();
        Call<List_of_Leave_Response> call = appService.Type_of_Leave( "Bearer " + sessionManager.getStringData( Globals.tokent ));
        call.enqueue( new Callback<List_of_Leave_Response>() {
            @Override
            public void onResponse(Call<List_of_Leave_Response> call, Response<List_of_Leave_Response> response)
            {  progressDialog.dismiss();
                if(response.isSuccessful() && response.body() != null)
                {
                    Log.d("List of Leave Type",response.body().toString());
                    List_of_Leave_Response list_of_leave_response = response.body();
                    if (list_of_leave_response.getResponseStatus()==200)
                    {
                        list_of_leave_models.addAll(list_of_leave_response.getResponse());
                        for (List_of_Leave_model model: list_of_leave_response.getResponse())
                        {
                            Leave_List.add( model.getName());

                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List_of_Leave_Response> call, Throwable t) {
                progressDialog.dismiss();
                Log.d( "ERS:","Something went wrong in List of Leave API" );
            }
        } );
    }*/
}