package com.example.nav_test.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.nav_test.MainActivity;
import com.example.nav_test.R;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystem;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this.getContext();


            path = requireContext().getFilesDir().getPath()+File.separator+"teamname";
            File teamname_dir = new File(path);
            if(!teamname_dir.exists())
                teamname_dir.mkdirs();

        toTeamgrass = new Intent(requireContext(), invidual_teamgrass.class);

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
                //all_file_string[i] = fileArray[i].getName();
                //createlayout(fileArray[i].getName());
            }
        }
        temp_array.add("this is for add button");

        all_file_array.clear();
        all_file_array.addAll(temp_array);




    }


            /*    Log.e("teamTitle.getText()", Team_title.getText().toString());
                toTeamgrass.putExtra("selected_team_name", Team_title.getText().toString());
                mContext.startActivity(toTeamgrass);
                fab.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Fragment fragment = new input_teamgrass();
                                               FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                               FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                               fragmentTransaction.replace(R.id.drawer_layout,fragment);
                                               fragmentTransaction.addToBackStack(null);
                                               fragmentTransaction.commit();




                                           }
        }

        );
        return teamgrass_block;
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.page_teamgrass, container, false);

        Log.e("file_length",Integer.toString(fileArray_length));

        /*for(int i=0;i<fileArray_length;i++){
            Log.e("block_name",all_file_string[i]);
            ((LinearLayout)root.findViewById(R.id.teamgrass_scrollview_layout)).addView(createlayout(all_file_string[i]));
            Log.e("each block","created");
        }*/

        final RecyclerView recyclerView = root.findViewById(R.id.teamgrass_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        final team_recycler_view_adapter adapter = new team_recycler_view_adapter(all_file_array);

        adapter.setOnItemClickListener(new team_recycler_view_adapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int pos) {
                Fragment fragment;

                if(pos ==all_file_array.size()-1) {
                    fragment = new input_teamgrass();


                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.drawer_layout, fragment);
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
                    fragment = new invidual_teamgrass();
                    fragment.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.drawer_layout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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
        //((LinearLayout)root.findViewById(R.id.teamgrass_scrollview_layout)).setLayoutParams(new ScrollView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //((ScrollView)root.findViewById(R.id.teamgrass_scrollview));//setLayoutParams(new LinearLayoutCompat.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));
        //root.findViewById(R.id.teamgrass_scrollview).invalidate();

        //FloatingActionButton fab = root.findViewById(R.id.button_on_teamgrass);


        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new input_teamgrass();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.drawer_layout,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });*///버블
        //((LinearLayout)root.findViewById(R.id.teamgrass_scrollview_layout)).addView(fab);
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

