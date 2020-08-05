package com.example.nav_test.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nav_test.R;
import com.example.nav_test.ReadMyName;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;

public class page_teamgrass extends Fragment {

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
    public page_teamgrass() {
        // Required empty public constructor
    }
    public page_teamgrass(tabPagerAdapter adapter) {
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

        toTeamgrass = new Intent(requireContext(), individual_teamgrass.class);

        toInputgrass = new Intent(requireContext(),input_teamgrass.class);




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

        final RecyclerView recyclerView = root.findViewById(R.id.teamgrass_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        final team_recycler_view_adapter adapter = new team_recycler_view_adapter(all_file_array,mContext);

        adapter.setOnItemClickListener(new team_recycler_view_adapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int pos) {
                Fragment fragment;

                if(pos ==all_file_array.size()-1) {
                    fragment = new input_teamgrass();


                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.drawer_layout, fragment); // 네비게이션바
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    Thread thread = new Thread(new Runnable() {
                        @Override

                        public void run() {


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                try {

                                    sharedDirectoryPath = Paths.get(path);

                                    watchService = FileSystems.getDefault().newWatchService();
                                    watchKey = sharedDirectoryPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                while (true) {
                                    for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
                                        getActivity().runOnUiThread(new Runnable(){
                                            @Override
                                            public void run() {
                                                loadAllFile(path);
                                                Log.e("watchevent", "occured");
                                                adapter.notifyDataSetChanged();

                                            }
                                        });
                                        break;
                                    }
                                }
                            }

                        }
                    });
                    thread.start();
                }//onclick end


                else {
                    Bundle args = new Bundle();
                    String txt_removed_teamname = all_file_array.get(pos).substring(0, all_file_array.get(pos).lastIndexOf("."));
                    args.putString("selected_team_name", txt_removed_teamname);
                    /*
                    fragment = new individual_teamgrass(new ReadMyName(mContext).getMyName(),page_teamgrass.this);

                    fragment.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.drawer_layout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                     */
                    Team team = new Team(txt_removed_teamname);
                    team.loadTeamMembers(mContext);

                    Intent intent = new Intent(
                            mContext, // 현재 화면의 제어권자
                            TeamActivity.class); // 다음 넘어갈 클래스 지정

                    intent.putExtra("teamObj",team);
                    startActivity(intent); // 다음 화면으로 넘어간다
                }

            }
        });
        adapter.setOnButtonClickListener((new team_recycler_view_adapter.OnButtonClickListener() {
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

