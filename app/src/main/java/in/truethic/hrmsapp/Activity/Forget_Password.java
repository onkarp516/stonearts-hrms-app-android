package in.truethic.hrmsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
import in.truethic.hrmsapp.Utils.Globals;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Forget_Password extends AppCompatActivity {
    ImageView img_back;
    AppCompatButton btn_okey;
    TextView tv_terms_and_service;
    TextInputEditText edit_mobile;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        img_back = findViewById(R.id.img_back);
        btn_okey = findViewById(R.id.btn_okey);
        edit_mobile = findViewById(R.id.edit_mobile);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Forget_Password.this, LoginActivity.class);
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
                Intent i = new Intent(Forget_Password.this, Terms_Activity.class);
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
                Intent i = new Intent(Forget_Password.this, Privacy_Policy_Activity.class);
                startActivity(i);
            }
        };
        spannableString.setSpan(privacyClickableSpan, 21, fullText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Apply the SpannableString to the TextView
        textView.setText(spannableString);

        // Make the links clickable
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edit_mobile.getText().toString();
                if(username.equalsIgnoreCase(""))
                {
                    Toast.makeText(Forget_Password.this,"Please Enter All Fields",Toast.LENGTH_LONG).show();
                    edit_mobile.setError("Username Required");
                }
                else {
                    progressDialog.show();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("username",edit_mobile.getText().toString());
                    AppService appService = RetrofitInstance.getService();
                    Call<JsonObject> call = appService.mobileNumberValidation(jsonObject);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            progressDialog.dismiss();
                            if(response.isSuccessful() && response.body()!=null){
                                if(response.body().get("responseStatus").getAsInt()==200){
                                    Log.e("Res==>>",response.body().toString());
                                    Globals.username=edit_mobile.getText().toString();
                                    Toast.makeText(Forget_Password.this, "Employee Registered Successful", Toast.LENGTH_LONG).show();

                                    Intent i = new Intent(Forget_Password.this,New_Password.class);
                                    startActivity(i);
                                    Forget_Password.this.finish();
                                }
                                else {
                                    Toast.makeText(Forget_Password.this, "Username Does Not Exist", Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Log.e("MobAuth===>>", "Not Success");
                                Toast.makeText(Forget_Password.this, "Contact Administrator! Something Went Wrong", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            progressDialog.dismiss();
                            Log.e( "onFailure==>>", "API fails in Forget Password Activity");
                            Toast.makeText(Forget_Password.this, "API Failed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}