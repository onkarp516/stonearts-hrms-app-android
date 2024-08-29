package in.truethic.hrmsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.DisplayDialogBox;
import in.truethic.hrmsapp.Utils.Globals;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class New_Password extends AppCompatActivity {
    AppCompatButton btn_okey;
    ImageView img_back;
    TextView tv_terms_and_service;
    TextInputEditText edit_new_pass,edit_confirm_pass;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        btn_okey = findViewById(R.id.btn_okey);
        img_back = findViewById(R.id.img_back);
        edit_new_pass = findViewById(R.id.edit_new_pass);
        edit_confirm_pass = findViewById(R.id.edit_confirm_pass);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(New_Password.this,Forget_Password.class);
                startActivity(i);
            }
        });
        TextView textView = findViewById(R.id.tv_terms_and_service); // Replace with the actual TextView ID

        // Your original text
        String fullText = "Terms of Service, and Privacy Policy.";

        // Create a SpannableString
        SpannableString spannableString = new SpannableString(fullText);

        // Set a ClickableSpan for "Terms of Service"
        ClickableSpan termsClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Handle click for "Terms of Service"
                // Example: Open the "Terms of Service" page
                Intent i = new Intent(New_Password.this, Terms_Activity.class);
                startActivity(i);
            }
        };
        spannableString.setSpan(termsClickableSpan, 0, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set a ClickableSpan for "Privacy Policy"
        ClickableSpan privacyClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Handle click for "Privacy Policy"
                // Example: Open the "Privacy Policy" page
                Intent i = new Intent(New_Password.this, Privacy_Policy_Activity.class);
                startActivity(i);
            }
        };
        spannableString.setSpan(privacyClickableSpan, 21, fullText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Apply the SpannableString to the TextView
        textView.setText(spannableString);

        // Make the links clickable
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");


        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_new_pass.getText().toString().equalsIgnoreCase("")||edit_confirm_pass.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(New_Password.this, "UPlease Enter All Fields", Toast.LENGTH_LONG).show();
                }
                else {
                    if (edit_new_pass.getText().toString().equalsIgnoreCase(edit_confirm_pass.getText().toString().toLowerCase())) {
                        progressDialog.show();
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty( "mobile_number", Globals.mobileNumber);
                        jsonObject.addProperty("password",edit_confirm_pass.getText().toString());
                        AppService appService = RetrofitInstance.getService();
                        Call<JsonObject> call = appService.forgetPassword( jsonObject );

                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                progressDialog.dismiss();
                                if (response.isSuccessful() && response.body() != null) {
                                    Log.e("ResponseSetPass==>>", response.body().toString());
                                    Log.e("SetPass===>>", "success");
                                    String msg = response.body().get("message").getAsString();
                                    Toast.makeText(New_Password.this,msg,Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(New_Password.this,LoginActivity.class);
                                    startActivity(i);
                                    New_Password.this.finish();
                                }
                                else
                                {
                                    Log.e("SetPass===>>", "Not Success");
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e( "NOTTAG", "Failure of response" + t.toString() );
                                DisplayDialogBox.showDialogBox(New_Password.this, "Network Issue.. Retry Later!", "New Password Alert" );
                            }
                        });
                    }
                    else {
                        DisplayDialogBox.showDialogBox(New_Password.this,"New & Confirm Password Not Same","New Password Alert");
                    }
                }
            }
        });
    }
}