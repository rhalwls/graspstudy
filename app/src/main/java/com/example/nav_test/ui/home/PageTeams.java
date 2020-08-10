package com.example.nav_test.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nav_test.LoadingActivity;
import com.example.nav_test.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;

public class PageTeams extends Fragment {

    Fragment thisfrag = this;
    tabPagerAdapter pagerAdapter;

    String path;
    ArrayList<String> all_file_array = new ArrayList<>();
    Path sharedDirectoryPath;
    WatchKey watchKey;
    WatchService watchService;

    String[] all_file_string;

    int fileArray_length;
    Intent toTeamgrass = null;
    Intent toInputgrass = null;

    Context mContext;
    public PageTeams() {
        // Required empty public constructor
    }
    public PageTeams(tabPagerAdapter adapter) {
        pagerAdapter = adapter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this.getContext();


            path = requireContext().getFilesDir().getPath()+File.separator+"teamname";
            File teamname_dir = new File(path);
            if(!teamname_dir.exists())
                teamname_dir.mkdirs();

        toTeamgrass = new Intent(requireContext(), FragTeamgrass.class);

        toInputgrass = new Intent(requireContext(), ActivityInputTeam.class);




        loadAllFile(path);

    }

    public void loadAllFile(String path){
        ArrayList<String> temp_array = new ArrayList<>();
        File file = new File(path);
        Log.e("file_path",path);

        File[] fileArray = file.listFiles();
        Log.e("Load_all_Files_amount",Integer.toString(fileArray.length));
        if(fileArray != null) {
            fileArray_length = fileArray.length;
            all_file_string = new String[fileArray_length];
            for (int i = 0; i < fileArray_length; i++) {
                Log.e("filename",fileArray[i].getName());
                temp_array.add(fileArray[i].getName());

            }
        }
        temp_array.add("this is for add button");

        all_file_array.clear();
        all_file_array.addAll(temp_array);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.page_teamgrass, container, false);

        Log.e("file_length",Integer.toString(fileArray_length));
        FloatingActionButton fab = root.findViewById(R.id.add_team); // 우측하단 동그라미 버튼


        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Intent intent = new Intent(
                                               mContext, // 현재 화면의 제어권자
                                               ActivityInputTeam.class); // 다음 넘어갈 클래스 지정
                                       startActivity(intent); // 다음 화면으로 넘어간다

                                   }
                               });//버블

        final RecyclerView recyclerView = root.findViewById(R.id.teamgrass_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        final TeamRecyclerView adapter = new TeamRecyclerView(all_file_array,mContext);

        adapter.setOnItemClickListener(new TeamRecyclerView.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int pos) {
                Fragment fragment;

                if(pos ==all_file_array.size()-1) {

                }//onclick end


                else {
                    Bundle args = new Bundle();
                    String txt_removed_teamname = all_file_array.get(pos).substring(0, all_file_array.get(pos).lastIndexOf("."));
                    args.putString("selected_team_name", txt_removed_teamname);

                    Team team = new Team(txt_removed_teamname);
                    team.loadTeamMembers(mContext);

                    Intent intent = new Intent(
                            mContext, // 현재 화면의 제어권자
                            LoadingTeamGrass.class); // 다음 넘어갈 클래스 지정

                    intent.putExtra("teamObj",team);
                    startActivity(intent); // 다음 화면으로 넘어간다
                }

            }
        });
        adapter.setOnButtonClickListener((new TeamRecyclerView.OnButtonClickListener() {//팀목록의 팀 하나를 눌렀을 때
            @Override
            public void onButtonClick(View v, int pos) {
                File file = new File(path+"/"+all_file_array.get(pos));
                String txt_removed_teamname = all_file_array.get(pos).substring(0, all_file_array.get(pos).lastIndexOf("."));
                File imagefile = new File("/data/data/com.example.nav_test/cache/"+txt_removed_teamname+".jpg");
                if(file.exists()){
                    imagefile.delete();
                    file.delete();
                }

                all_file_array.remove(pos);
                adapter.notifyItemRemoved(pos);
                adapter.notifyItemRangeChanged(pos,all_file_array.size());

                //adapter.notifyDataSetChanged();
                //loadAllFile(path);

            }
        }));



        recyclerView.setAdapter(adapter);

        //recyclerView.getAdapter().notifyDataSetChanged();


        Log.e("all block","created");
        Log.e("bubble","created");
        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

