package com.example.nav_test.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nav_test.R;
import com.example.nav_test.ReadMyName;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class TeamRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LinkedList<String> mData = null;
    String user;
    int max;

    Context mContext;

    public String getmData(int pos) {
        return mData.get(pos);
    }


    public class ViewHolder0 extends RecyclerView.ViewHolder{
        TextView title;
        ImageView image;
        Button delete;
        TextView detail;


        ViewHolder0(View itemView){ // 화면에 표시될 아이템뷰를 저장해주는 객체
            super(itemView);
            delete = itemView.findViewById(R.id.team_delete_button);
            title = itemView.findViewById(R.id.team_title);//수정 필요!
            image = itemView.findViewById(R.id.recycler_imageview);
            detail = itemView.findViewById(R.id.team_detail);

            delete.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){


                        if(bListener!=null){
                            bListener.onButtonClick(v,pos);
                        }
                    }
                    notifyDataSetChanged();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener!=null){
                            mListener.onItemClick(v,pos);
                        }
                    }
                }
            });
        }

    }
    void logList(){
        for(int i =0;i<mData.size();i++){
            Log.i("TeamRecyclerView","team list path : "+mData.get(i));
        }
    }
    TeamRecyclerView(LinkedList<String> list, Context context){
        mData = list;

        mContext=context;
        user = new ReadMyName(context).getMyName();
        logList();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;
        TeamRecyclerView.ViewHolder0 vh0;
        view = inflater.inflate(R.layout.teamgrass_recyclerview_item,parent,false);
        vh0 = new TeamRecyclerView.ViewHolder0(view);
        return vh0;

    }

    public void setDetailAsMembers(ViewHolder0 viewHolder0 , String teamName){
        Team team = Team.loadTeamFile(mContext,user,teamName);
        ArrayList<String> teamMembers =team.getMembers();
        int team_size = teamMembers.size();
        String detailStr = "";
        Iterator<String> it=teamMembers.iterator();
        String member;
        int ctr = 0;
        while(it.hasNext()&&ctr<3){
            member = it.next();
            Log.i("team_recycler_view_adapter","reading a team member #"+ctr+" , "+member);
            detailStr+=member+"\n";
            ctr++;
        }
        detailStr+="멤버 수 : "+team_size;

        viewHolder0.detail.setText(detailStr);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            //지금 이거 case 가 뭔지 모르겟슴

            case 0:
                ViewHolder0 viewHolder0 = (ViewHolder0)holder;
                String teamname = mData.get(position); //team name 받음
                String txt_removed_teamname = teamname.substring(0, teamname.lastIndexOf("."));

                viewHolder0.title.setText(txt_removed_teamname);
                setDetailAsMembers(viewHolder0, txt_removed_teamname);


                File file = new File("/data/data/com.example.nav_test/cache/"+txt_removed_teamname+".jpg" );

                Log.e("image path","/data/data/com.example.nav_test/cache/"+txt_removed_teamname+".jpg");
                if (file.exists()&&file != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile("/data/data/com.example.nav_test/cache/"+txt_removed_teamname+".jpg");
                    viewHolder0.image.setImageBitmap(bitmap);
                }

                break;
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View v,int pos);
    }

    public interface OnButtonClickListener{
        void onButtonClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    private OnButtonClickListener bListener = null;


    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public void setOnButtonClickListener(OnButtonClickListener listener){
        this.bListener = listener;
    }
}
