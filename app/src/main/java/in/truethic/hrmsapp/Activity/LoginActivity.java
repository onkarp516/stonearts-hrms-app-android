package in.truethic.hrmsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.google.gson.JsonObject;
import java.util.Map;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.DisplayDialogBox;
import in.truethic.hrmsapp.Utils.SessionManager;
import in.truethic.hrmsapp.Utils.Globals;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText edit_Username, edit_Password;
    SessionManager sessionManager;
    String emp_name, designationName, userId, doj, address,userCode;
    String siteName, siteCode, designationLevel, showSalarySheet, username_token;
    String siteLat, siteLong, siteRadius, currentLat, currentLong, isInRange;
    AppCompatButton loginButton;
    TextView tv_forget_pass,tv_terms_and_service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.btnLogin);
        sessionManager = new SessionManager(LoginActivity.this);
        tv_forget_pass = findViewById(R.id.tv_forget_pass);
        edit_Username = findViewById(R.id.edit_mobile);
        edit_Password = findViewById(R.id.edit_pass);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

//        tv_terms_and_service = findViewById(R.id.tv_terms_and_service);

        tv_forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, Forget_Password.class);
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
                Intent i = new Intent(LoginActivity.this, Terms_Activity.class);
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
                Intent i = new Intent(LoginActivity.this, Privacy_Policy_Activity.class);
                startActivity(i);
            }
        };
        spannableString.setSpan(privacyClickableSpan, 21, fullText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Apply the SpannableString to the TextView
        textView.setText(spannableString);

        // Make the links clickable
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void login() {
        ProgressDialog dialog;
        String username = edit_Username.getText().toString();
        String password = edit_Password.getText().toString();
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Processing......");

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && username.length() == 10) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("username", username);
            jsonObject.addProperty("password", password);

            AppService appService = RetrofitInstance.getService();
            Call<JsonObject> call = appService.loginUser( jsonObject );

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("TAG", "" + response.body());
                        if (response.body().get("responseStatus").getAsInt() == 200) {
                            sessionManager.setStringData(SessionManager.Employee_Username, username);
                            Log.e("UserNameStr==>>", sessionManager.getStringData(SessionManager.Employee_Name));
                            sessionManager.setStringData(SessionManager.Employee_Password, password);

                            sessionManager.setStringData(Globals.tokent,
                                    response.body().get("response").getAsJsonObject().get("access_token").getAsString());

                            sessionManager.setBooleanData(Globals.isLoggedIn, true);

//------------------------------------------get Employee name and set to SPF with JWT---------------------------------------------
                            JWT jwt = new JWT(response.body().get("response").getAsJsonObject().get("access_token").getAsString());
                            Map<String, Claim> allClaims = jwt.getClaims();
                            Log.d("All Claims:", String.valueOf(allClaims));

                            emp_name = allClaims.get("employeeName").asString();
                            designationName = allClaims.get("designationName").asString();
                            siteLat = allClaims.get("branchLat").asString();
                            siteLong = allClaims.get("branchLong").asString();
                            siteRadius = allClaims.get("branchRadius").asString();
                            siteName = allClaims.get("branchName").asString();
                            userId = allClaims.get("username").asString();
                            userCode = allClaims.get("userId").asString();
                            String teamId = null;
                           if (allClaims.containsKey("teamId")){
                               teamId = allClaims.get("teamId").asString();
                               Log.e("teamId", teamId);

                               sessionManager.setStringData(SessionManager.token_teamId,teamId);

                               Globals.token_teamId = teamId;

                           }

                            String branchId = allClaims.get("branchId").asString();

                            Log.e("branchId", branchId);

                            Log.e("emp_name", emp_name);
                            Log.e("designationName", designationName);
                            Log.e("siteName", siteName);
                            Log.e("siteLat", siteLat);
                            Log.e("siteLong", siteLong);
                            Log.e("siteRadius", siteRadius);
                            Log.e("userId", userId);
                            Log.e("userCode", userCode);


                            sessionManager.setStringData(SessionManager.Employee_Name, emp_name);
                            Globals.emp_name = emp_name;
                            Globals.emp_phone = userId;
                            Globals.userId = userCode;
                            Globals.emp_designation = designationName;
                            sessionManager.setStringData(SessionManager.siteLongitude, siteLong);
                            sessionManager.setStringData(SessionManager.siteLatitude, siteLat);
                            sessionManager.setStringData(SessionManager.siteRadius, siteRadius);



                            sessionManager.setStringData(SessionManager.token_branchId,branchId);

                            Globals.token_branchId = branchId;
                            String sr = sessionManager.getStringData(SessionManager.siteRadius);
                            Log.e("SiteR==>>", sr);
                            Log.e("Login_LL", sessionManager.getStringData(SessionManager.siteLongitude) + "," + sessionManager.getStringData(SessionManager.siteLatitude));


                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            // Toast.makeText(LoginActivity.this, "Access Denied", Toast.LENGTH_LONG).show();
                            DisplayDialogBox.showDialogBox(LoginActivity.this, response.body().get("message").getAsString(), "Login Alert");
                        }
                    }
                    else
                    {
                        DisplayDialogBox.showDialogBox(LoginActivity.this, "Please contact Admin!", "Login Alert");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("Res==>", "Response Failed" + t.toString());
                    Toast.makeText(LoginActivity.this, "Network Issue", Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            DisplayDialogBox.showDialogBox(LoginActivity.this, "Please enter Valid Details", "Login Alert" );
        }
    }
}