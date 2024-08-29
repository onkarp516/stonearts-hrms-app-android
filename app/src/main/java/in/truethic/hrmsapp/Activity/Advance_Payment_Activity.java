package in.truethic.hrmsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.truethic.hrmsapp.Adapter.AdvancePayment_Adapter;
import in.truethic.hrmsapp.Model.List_of_AdvancePayment_Model;
import in.truethic.hrmsapp.Model.List_of_Advance_Pay_Model;
import in.truethic.hrmsapp.Model.List_of_Advance_Pay_Response;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.DisplayDialogBox;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Advance_Payment_Activity extends AppCompatActivity {
    ImageView iv_back,iv_notification;
    AppCompatButton btn_apply_withdrawal;

    EditText edt_salary_amt,edt_salary_credit,edt_Date, edt_amount;
    ProgressDialog progressDialog;
    Switch idSwitch;

    boolean isInstallment = false;

    double Number_of_installment = 0.0;
    double Total_amt = 0.0;

    ArrayList<List_of_Advance_Pay_Model> aData;

    AdvancePayment_Adapter advancePaymentAdapter;

    int day, month, year, final_diff;

    String dateFrom, dateTo, total_d, Leave_remark="";
    RelativeLayout rl_withdrawal_layout,rl_installment,rl_withdrawal_layout_main;
    SessionManager sessionManager;


    EditText edit_no_installment;
    TextView tv_emi_txt,tv_total_amount,tv_number_install;
//
//    RecyclerView rv_payment_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_payment);
        iv_back = findViewById(R.id.iv_back);
        iv_notification = findViewById(R.id.iv_notification);
        btn_apply_withdrawal = findViewById(R.id.btn_apply_withdrawal);
//        rl_withdrawal_layout = findViewById(R.id.rl_withdrawal_layout);
        edt_salary_amt = findViewById(R.id.edt_salary_amt);
        edt_salary_credit = findViewById(R.id.edt_salary_credit);
        edt_Date = findViewById(R.id.edt_Date);
        idSwitch = findViewById(R.id.idSwitch);
        edt_amount = findViewById(R.id.edt_amount);
//        rv_payment_list = findViewById(R.id.rv_payment_list);
        rl_withdrawal_layout_main = findViewById(R.id.rl_withdrawal_layout_main);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        tv_number_install = findViewById(R.id.tv_number_install);
        edit_no_installment = findViewById(R.id.edit_no_installment);
        tv_emi_txt = findViewById(R.id.tv_emi_txt);
        sessionManager = new SessionManager(Advance_Payment_Activity.this);

        progressDialog=new ProgressDialog(Advance_Payment_Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        if (edt_amount.getText().toString().equalsIgnoreCase("")) {
            idSwitch.setEnabled(false);
        }

        aData = new ArrayList<>();
        advancePaymentAdapter = new AdvancePayment_Adapter(Advance_Payment_Activity.this,aData);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(Advance_Payment_Activity.this, LinearLayoutManager.VERTICAL, false);
//        rv_payment_list.setLayoutManager(layoutManager);
//        rv_payment_list.setAdapter(advancePaymentAdapter);

        edit_no_installment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!edit_no_installment.getText().toString().equalsIgnoreCase("")) {
                    idSwitch.setEnabled(true);
                    int total_installments = Integer.parseInt(edit_no_installment.getText().toString());
                    if (total_installments != 0) {
                        double withdrawal_amt = Double.parseDouble(edt_amount.getText().toString());

                        final DecimalFormat decfor = new DecimalFormat("0.00");

                        tv_number_install.setText("" + total_installments);

                        double Emi = (withdrawal_amt / total_installments);

                        tv_emi_txt.setText(decfor.format(Emi));
                    }
                } else {
                    tv_number_install.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    edt_amount.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!edt_amount.getText().toString().equalsIgnoreCase("")) {
                idSwitch.setEnabled(true);
                String total_amt = edt_amount.getText().toString();

                Log.e("Total Amt ===>",total_amt);

                tv_total_amount.setText(total_amt);

            } else {
                idSwitch.setChecked(false);
                idSwitch.setEnabled(false);
                tv_emi_txt.setText("");
                tv_number_install.setText("");
                tv_total_amount.setText("");
                edit_no_installment.setText("");
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    });

        getDistrictHeadList();

        isInstallment = false;

        getEmployeeAdvancePayment();
        idSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Log.e("Checked","Swich Checked");
                    isInstallment = true;
                    rl_withdrawal_layout_main.setVisibility(View.VISIBLE);
                } else {
                    // The toggle is disabled
                    isInstallment = false;
                    Log.e("UnChecked","Swich UnChecked");
                    rl_withdrawal_layout_main.setVisibility(View.GONE);
                }
            }
        });


        btn_apply_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Advance_Payment_Activity.this);
                LayoutInflater layoutInflater = (LayoutInflater) Advance_Payment_Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View customLayout = layoutInflater.inflate(R.layout.apply_withdrawal_pop_up, null);
                builder.setView(customLayout);

                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();

                AppCompatButton  btn_Okay, btn_Cancel;

                btn_Okay = customLayout.findViewById(R.id.btn_okay_withdrawal);
                btn_Cancel = customLayout.findViewById(R.id.btn_cancel);

                btn_Okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if(TextUtils.isEmpty(edt_Date.getText().toString()) || TextUtils.isEmpty(edt_salary_amt.getText().toString())){
                            DisplayDialogBox.showDialogBox( Advance_Payment_Activity.this, "Please Enter All Fields", "Advance Payment Alert" );
                        }
                        else {
                            createAdvancePaymentRequest(isInstallment);
                        }
                    }
                });
                btn_Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Advance_Payment_Activity.this, NotificationActivity.class);
                startActivity(i);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        edt_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                day = calendar.get( Calendar.DAY_OF_MONTH );
                month = calendar.get( Calendar.MONTH );
                year = calendar.get( Calendar.YEAR );

                Log.d( "DAY:", day + "-" + month + "-" + year );
                DatePickerDialog datePickerDialog = new DatePickerDialog( Advance_Payment_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set( Calendar.DAY_OF_MONTH, dayOfMonth );
                        calendar.set( Calendar.MONTH, month );
                        calendar.set( Calendar.YEAR, year );
                        String date = new SimpleDateFormat( "dd-MMM-yyyy" ).format( calendar.getTimeInMillis() );


                        SimpleDateFormat parseFormat = new SimpleDateFormat( "dd-MMM-yyyy" );
                        try {
                            Date date1 = parseFormat.parse( date );
                            dateFrom = new SimpleDateFormat( "yyyy-MM-dd" ).format( date1 );
                            // ShowToast( dateFrom );
                            edt_Date.setText( dateFrom );
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d( "Date Parse Error", "" + e );
                        }
                    }
                }, year, month, day );
                datePickerDialog.getDatePicker().setMinDate( System.currentTimeMillis() - 1000 );
                datePickerDialog.show();
            }
        });


    }

    private void getEmployeeAdvancePayment() {

        AppService appService = RetrofitInstance.getService();
        Call<JsonObject> call = appService.getEmployeeAdvancePayment("Bearer " + sessionManager.getStringData( Globals.tokent ) );
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful() && response != null) {
                    if(response.body().get("responseStatus").getAsInt()==200)
                    {
                        Log.e("PaymentDashboard==>",response.body().toString());

                        String employeeSalary = response.body().get("response").getAsJsonObject().get("employeeSalary").getAsString();
                        Log.e("employeeName==>",employeeSalary);

                        String employeeCreditSalary = response.body().get("response").getAsJsonObject().get("employeeCreditSalary").getAsString();
                        Log.e("employeeName==>",employeeCreditSalary);


                        edt_salary_amt.setText(employeeSalary);
                        edt_salary_credit.setText(employeeCreditSalary);

                    }
                    else
                    {
                        DisplayDialogBox.showDialogBox(Advance_Payment_Activity.this, "Please Retry!", "Zonal Head Info Alert");
                    }
                }
                else
                {
                    DisplayDialogBox.showDialogBox(Advance_Payment_Activity.this, "Contact Admin!", "Zonal Head Info Alert");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("stateHDash_onFailure==>",t.toString());
            }
        });
    }
    public void ShowToast(String msg) {
        Toast.makeText( Advance_Payment_Activity.this, msg, Toast.LENGTH_LONG ).show();
    }

    private void createAdvancePaymentRequest(Boolean isInstallment) {
        JsonObject jsonObject = new JsonObject();
        //jsonObject.addProperty("leaveCategory",obals.leaveCategory);
        if (isInstallment){
            Log.e("isInstallment==>", isInstallment.toString());
            Log.e("dateOfRequest==>", dateFrom);
            Log.e("requestAmount==>", edt_amount.toString());
            jsonObject.addProperty("dateOfRequest", dateFrom);
            jsonObject.addProperty("requestAmount", edt_amount.getText().toString());
            jsonObject.addProperty("isInstallment", true);
            jsonObject.addProperty("noOfInstallments", edit_no_installment.getText().toString());
            jsonObject.addProperty("installmentAmount", tv_emi_txt.getText().toString());
        } else {
            jsonObject.addProperty("dateOfRequest", dateFrom);
            jsonObject.addProperty("requestAmount", edt_amount.getText().toString());
        }
            AppService appService = RetrofitInstance.getService();
            Call<JsonObject> call = appService.createAdvancePaymentRequest("Bearer " + sessionManager.getStringData(Globals.tokent), jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {

                        if(response.body().get("responseStatus").getAsInt()==200)
                        {
                            ShowToast("Saved advance payment request");
                            //dialog.dismiss();
                            finish();
                        }
                        else
                        {
                            DisplayDialogBox.showDialogBox(Advance_Payment_Activity.this,response.body().get("message").getAsString(),"Leave Alert");
                            //dialog.dismiss();
                        }
                    } else {
                        DisplayDialogBox.showDialogBox(Advance_Payment_Activity.this, "Server not Responding. Contact Administrator", "Server Alert");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("Leave API ERS:", "Something went Wrong into Leave API");
                    DisplayDialogBox.showDialogBox(Advance_Payment_Activity.this, "Server is Down. Contact Administrator", "Server Alert");
                }
            });

        }

    private void getDistrictHeadList() {
        progressDialog.show();
        AppService appService = RetrofitInstance.getService();
        Call<List_of_Advance_Pay_Response> call = appService.AdvancePaymentList("Bearer " + sessionManager.getStringData( Globals.tokent ) );
        call.enqueue(new Callback<List_of_Advance_Pay_Response>() {
            @Override
            public void onResponse(Call<List_of_Advance_Pay_Response> call, Response<List_of_Advance_Pay_Response> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response != null) {
                    Log.e("Response==>",response.body().toString());
                    if(response.body().getResponseStatus()==200)
                    {
                        aData.addAll(response.body().getResponse());
                        advancePaymentAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        DisplayDialogBox.showDialogBox(Advance_Payment_Activity.this, "Please Retry!", "Advance Payment Alert");
                    }
                }
                else
                {
                    DisplayDialogBox.showDialogBox(Advance_Payment_Activity.this, "Please Retry!", "Advance Payment Alert");
                }
            }

            @Override
            public void onFailure(Call<List_of_Advance_Pay_Response> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("RegionanFailure==> ",t.toString());
            }
        });
    }





}