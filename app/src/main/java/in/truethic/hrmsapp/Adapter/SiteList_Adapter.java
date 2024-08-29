package in.truethic.hrmsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.truethic.hrmsapp.Activity.TeamMapActivity;
import in.truethic.hrmsapp.Model.SiteDataModel;
import in.truethic.hrmsapp.Model.Site_List_Response;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;

public class SiteList_Adapter extends RecyclerView.Adapter<SiteList_Adapter.SiteList_Holder> {
    Context mContext;
    List<SiteDataModel> sData;

    SessionManager sessionManager;

    public SiteList_Adapter(Context mContext, List<SiteDataModel> sData) {
        this.mContext = mContext;
        this.sData = sData;

    }

    @NonNull
    @Override
    public SiteList_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.site_row,parent,false );
        SiteList_Adapter.SiteList_Holder viewHolder = new SiteList_Adapter.SiteList_Holder( view );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SiteList_Holder holder, int position) {
        holder.bindOrder( sData.get( position ),position );
    }

    @Override
    public int getItemCount() {
        return sData.size();
    }

    public class SiteList_Holder extends RecyclerView.ViewHolder
    {
      RelativeLayout rl_siteList;
      TextView tv_site_name;
        public SiteList_Holder(View itemView) {
            super( itemView );
            tv_site_name = itemView.findViewById(R.id.tv_site_name);
            rl_siteList = itemView.findViewById(R.id.rl_siteList);


        }

        public void bindOrder(SiteDataModel siteDataModel, int position)  {


            tv_site_name.setText(siteDataModel.getBranchName());

            rl_siteList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    siteLocation.setLatitude(sessionManager.getSiteData("sitedata").getSiteLat());
//                    siteLocation.setLongitude(sessionManager.getSiteData("sitedata").getSiteLong());
//                    siteRadius = sessionManager.getSiteData("sitedata").getSiteRadius();
//                 sessionManager.setStringData(SessionManager.teamsiteLongitude,siteDataModel.se);
//                  sessionManager.setStringData(SessionManager.teamsiteLatitude,siteDataModel.se().toString());

                    Globals.teamlat = siteDataModel.getBranchLat().toString();
                    Globals.teamlong = siteDataModel.getBranchLong().toString();
                    Globals.siteRadius = siteDataModel.getBranchRadius().toString();
                    Globals.teamId = siteDataModel.getTeamId().toString();
                    Globals.branchId = siteDataModel.getBranchId().toString();

                    Intent i = new Intent(mContext, TeamMapActivity.class);
                    mContext.startActivity(i);

                   // sessionManager.setStringData(SessionManager.teamsiteLatitude, Globals.teamlat);
                    //sessionManager.setStringData(SessionManager.teamsiteLongitude,Globals.teamlong);
                    Log.e("teamsiteLongitude",Globals.teamlat + "," +  Globals.teamlong);
                    Log.e("att Type",Globals.att_type+"");

                }
            });

        }
    }
}
