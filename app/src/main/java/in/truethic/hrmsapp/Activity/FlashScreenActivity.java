package in.truethic.hrmsapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationManagerCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.google.gson.JsonObject;

import java.util.Map;

import in.truethic.hrmsapp.BuildConfig;
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

public class    FlashScreenActivity extends AppCompatActivity {

    private AppCompatButton button;
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    SessionManager sessionManager;
    ProgressDialog dialog1;
    String emp_name, designationName, userId, doj, address,userCode;
    String siteName, siteCode, designationLevel, showSalarySheet, username_token;
    String siteLat, siteLong, siteRadius, currentLat, currentLong, isInRange;
    int finishActivity=0;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);
        sessionManager = new SessionManager(FlashScreenActivity.this);


        dialog1 = new ProgressDialog( FlashScreenActivity.this );
        dialog1.setCancelable( false );
        dialog1.setMessage( "Loading..." );

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               // checkForUpdate();
                gotoNext();
            }
        }, 3000);
    }

    private void startUpdate() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(Intent.createChooser( new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)),"Open with" ));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(Intent.createChooser( new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)),"Open with" ));
        }

        finishActivity=1;
    }
    private void checkForUpdate() {
        try {
            AppService appService = RetrofitInstance.getService();
            Call<JsonObject> call = appService.getAppVersion();
            call.enqueue( new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    dialog1.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e( "UPDATECHECK", "1" + response.body().toString() );

                        if (response.body().get("responseStatus").getAsInt() == 200) {

                            int vCode = response.body().get( "response" ).getAsJsonObject().get( "versionCode" ).getAsInt();

                            //Log.e("UPDT","MB="+BuildConfig.VERSION_CODE+", ON="+vCode);
                            if (vCode > BuildConfig.VERSION_CODE) {
                                showAlert();
                            }
                            else
                            {
                                gotoNext();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    dialog1.dismiss();
                    ShowToast.show( FlashScreenActivity.this,"Technical error, Try Later!" );
                }
            } );
        } catch (Exception x) {
            //ShowToast.show(mContext,"Step 2");
            x.printStackTrace();
            ShowToast.show(FlashScreenActivity.this, "Technical error, Try Later!");
        }
    }

    private void showAlert() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        startUpdate();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        ShowToast.show( FlashScreenActivity.this,"Update is mandatory!" );
                        finish();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Update available for your app!").setPositiveButton("Update", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }


    private void gotoNext() {
        if (sessionManager.getBooleanData(Globals.isLoggedIn)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("username", sessionManager.getStringData(SessionManager.Employee_Username));
            jsonObject.addProperty("password", sessionManager.getStringData(SessionManager.Employee_Password));

            dialog1.show();
            AppService appService = RetrofitInstance.getService();
            Call<JsonObject> call = appService.loginUser(jsonObject);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    dialog1.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("MaG", "" + response.body());
                        if (response.body().get("responseStatus").getAsInt() == 200) {

                            sessionManager.setStringData(Globals.tokent,
                                    response.body().get("response").getAsJsonObject().get("access_token").getAsString());

                            sessionManager.setBooleanData(Globals.isLoggedIn, true);

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
                            String branchId = allClaims.get("branchId").asString();

                            String teamId = null;
                            if (allClaims.containsKey("teamId")){
                                teamId = allClaims.get("teamId").asString();
                                Log.e("teamId", teamId);

                                sessionManager.setStringData(SessionManager.token_teamId,teamId);

                                Globals.token_teamId = teamId;

                            }

                            Log.e("branchId", branchId);
                          //  Log.e("teamId", teamId);
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



                            sessionManager.setStringData(SessionManager.token_branchId,branchId);

                            Globals.token_branchId = branchId;

                            sessionManager.setStringData(SessionManager.siteLongitude, siteLong);
                            sessionManager.setStringData(SessionManager.siteLatitude, siteLat);
                            sessionManager.setStringData(SessionManager.siteRadius, siteRadius);

                            String sr = sessionManager.getStringData(SessionManager.siteRadius);
                            Log.e("SiteR==>>", sr);
                            Log.e("Login_LL", sessionManager.getStringData(SessionManager.siteLongitude) + "," + sessionManager.getStringData(SessionManager.siteLatitude));


                            Intent i = new Intent(FlashScreenActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            Intent i = new Intent(FlashScreenActivity.this,LoginActivity.class);
                            startActivity(i);
                        }

                    } else {

                        Intent i = new Intent(FlashScreenActivity.this,LoginActivity.class);
                        startActivity(i);
                        //Toast.makeText(FlashScreenActivity.this, "Access Denied", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    dialog1.dismiss();
                    Log.e("Res==>", "Response Failed" + t.toString());
                    Toast.makeText(FlashScreenActivity.this, "Network Issue", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Intent i = new Intent(FlashScreenActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

}