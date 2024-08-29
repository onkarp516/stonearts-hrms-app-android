package in.truethic.hrmsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import in.truethic.hrmsapp.Adapter.Leave_Status_Adapter;
import in.truethic.hrmsapp.Adapter.SiteList_Adapter;
import in.truethic.hrmsapp.Model.LeaveStatusModal;
import in.truethic.hrmsapp.Model.SiteDataModel;
import in.truethic.hrmsapp.Model.Site_List_Response;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Retrofit.AppService;
import in.truethic.hrmsapp.Retrofit.RetrofitInstance;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteListActivity extends AppCompatActivity {

    RecyclerView rv_siteList;
    SessionManager sessionManager;
    List<SiteDataModel> sData;
    SiteList_Adapter siteList_adapter;
    ProgressDialog dialog;
    ImageView iv_back;
    String TAG = "THOMEACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_list);
        sessionManager =new SessionManager(SiteListActivity.this);
        rv_siteList = findViewById(R.id.rv_siteList);
        iv_back = findViewById(R.id.iv_back);

        dialog = new ProgressDialog(SiteListActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);


        sData = new ArrayList<>();
        siteList_adapter = new SiteList_Adapter(SiteListActivity.this,sData);

//------------------------Recycle View Inflated start Here for Display of Advance Payment List------------------------------------------------------------------

        LinearLayoutManager layoutManager = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false );
        rv_siteList.setLayoutManager( layoutManager );
        rv_siteList.setAdapter( siteList_adapter );

//----------------------------------------------------------------------------------------------------------------------------------------------------------
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getMySites();
    }

    public void getMySites() {
        try {
            dialog.show();
            AppService appService = RetrofitInstance.getService();
            Call<Site_List_Response> call = appService.getSiteData("Bearer " + sessionManager.getStringData( Globals.tokent ), "application/json");
            call.enqueue(new Callback<Site_List_Response>() {
                @Override
                public void onResponse(Call<Site_List_Response> call, Response<Site_List_Response> response) {
                    dialog.dismiss();
                    if (response.body() != null) {
                        //Log.e(TAG, "ST2" + response.body().toString());
                        if (response.body().getResponseStatus() == 200) {

                            String data = response.body().getResponse().toString();
                            Log.e("SiteResp==>",data);
                            sData.clear();
                            sData.addAll(response.body().getResponse());
                            siteList_adapter.notifyDataSetChanged();

                        }
                    } else {
                        Log.e(TAG, "Req is Null");
                    }


                }

                @Override
                public void onFailure(Call<Site_List_Response> call, Throwable t) {
                    dialog.dismiss();
                    Log.e(TAG, "2" + t.toString());
                    //ShowToast("Try again Later");
                }
            });
        } catch (Exception x) {
            //ShowToast.show(mContext,"Step 2");
            dialog.dismiss();
            x.printStackTrace();
            Log.e(TAG, x.toString());
        }
    }
}