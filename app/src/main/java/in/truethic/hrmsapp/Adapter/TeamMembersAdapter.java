package in.truethic.hrmsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.truethic.hrmsapp.Model.TeamMembersModel;
import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Utils.Globals;

public class TeamMembersAdapter extends RecyclerView.Adapter<TeamMembersAdapter.AttHolder> {

    Context mContext;
    List<TeamMembersModel> mData;

    public TeamMembersAdapter(Context context, List<TeamMembersModel> data) {
        mContext = context;
        mData = data;
    }

    @NonNull
    @Override
    public AttHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_team_members, parent, false);
        AttHolder viewHolder = new AttHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttHolder holder, int position) {
        holder.bindOrder(mData.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void checkAll(){
       for(int i=0;i<mData.size();i++){
           mData.get(i).setChecked(true);
       }
        Globals.finalList.clear();
        Globals.finalList.addAll(mData);

        notifyDataSetChanged();
    }

    public void unCheckAll(){
        for(int i=0;i<mData.size();i++){
            mData.get(i).setChecked(false);
        }
        Globals.finalList.clear();
        notifyDataSetChanged();
    }

    public class AttHolder extends RecyclerView.ViewHolder {

        TextView tv_member_name;

        TextView tv_member_code;

        CheckBox cb_member;

        public AttHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            tv_member_name = itemView.findViewById(R.id.tv_member_name);
            tv_member_code = itemView.findViewById(R.id.tv_member_code);
            cb_member = itemView.findViewById(R.id.cb_member);
        }

        public void bindOrder(TeamMembersModel teamMembersModel,int position) {

            tv_member_name.setText(teamMembersModel.getEmployeeName());


            if(teamMembersModel.isChecked()){
                cb_member.setChecked(true);
            }else{
                cb_member.setChecked(false);
            }
            cb_member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cb_member.isChecked()){
                        //Toast.makeText(mContext,"Checked",Toast.LENGTH_LONG).show();
                        Globals.finalList.add(teamMembersModel);
                        mData.get(position).setChecked(true);
                    }else{
                        //Toast.makeText(mContext,"Un Checked",Toast.LENGTH_LONG).show();
                        Globals.finalList.remove(Globals.finalList.indexOf(teamMembersModel));
                        mData.get(position).setChecked(false);
                    }
                }
            });


        }
    }
}

