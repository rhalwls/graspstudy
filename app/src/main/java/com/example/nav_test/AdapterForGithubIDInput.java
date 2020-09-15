package com.example.nav_test;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AdapterForGithubIDInput extends RecyclerView.Adapter<AdapterForGithubIDInput.GithubIDInput> {
    private ArrayList<String> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class GithubIDInput extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public EditText editTextID;
        public ImageButton valid;
        public Button buttonDelete;


        public GithubIDInput(View v) {
            super(v);
            Log.i("AdapterForGithubIDInput", "calling constructor of a view holder");
            editTextID = v.findViewById(R.id.edit_text_github_id);
            valid = v.findViewById(R.id.image_view_id_valid);
            //valid.setImageResource(AdapterForGithubIDInput.this.VALID);
            valid.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("View: ", v.toString());
                    //Toast.makeText(v.getContext(), mTextViewTitle.getText() + " position = " + getPosition(), Toast.LENGTH_SHORT).show();
                    if (v.equals(valid)) {
                        //removeAt(getLayoutPosition());
                        valid.setBackgroundResource(0);
                        Log.i("AdapterForG",getLayoutPosition()+"의 edittext는 "+editTextID.getText());
                        String inputID = editTextID.getText().toString();
                        int checkValid =AdapterForGithubIDInput.this.checkValidity(inputID);//id validity check
                        if(checkValid==AdapterForGithubIDInput.this.VALID) {
                            mDataset.set(getLayoutPosition(), inputID);
                        }
                        valid.setImageResource(checkValid);
                    }
                }
            });
            buttonDelete = v.findViewById(R.id.button_delete);
            buttonDelete.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("View: ", v.toString());
                    //Toast.makeText(v.getContext(), mTextViewTitle.getText() + " position = " + getPosition(), Toast.LENGTH_SHORT).show();
                    if (v.equals(buttonDelete)) {
                        removeAt(getLayoutPosition());
                    }
                }
            });

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterForGithubIDInput(ArrayList<String> idSet) {
        mDataset = idSet;
    }


    @NonNull
    @Override
    public GithubIDInput onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("AdapterForGithubIDInput","onCreateViewHolder called");

        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.github_id_recyclerview_item, parent, false);
        GithubIDInput viewHolder = new GithubIDInput(view);

        return viewHolder;
    }
    public static int VALID =R.drawable.valid_icon;
    //일단 각 원인에 대해 유저에게 알려줄 필요는 없다고 해서 다 invalid아이콘으로 해둠
    public static int NOT_DETERMINED = R.drawable.invalid_icon;
    public static int INVALID = R.drawable.invalid_icon;
    public static int UNABLE_TO_FIGURE_OUT = R.drawable.no_internet;
    public static int PLZ_CHECK= R.drawable.want_check;
    public int checkValidity(String id){
        GithubIDValidator validator = new GithubIDValidator(id);
        try {
            Boolean isValidUsername = validator.execute().get();
            if(id==null||id==""){
                return NOT_DETERMINED;
            }
            else if(id!=null&&!id.equals("")&&isValidUsername){
                Log.i("AdapterForG","three conditions and input id: "+(id!=null)+(id!="")+isValidUsername+id);
                return VALID;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return INVALID;
    }


    @Override
    public void onBindViewHolder(@NonNull GithubIDInput holder, int position) {
        String id = mDataset.get(position);

        Log.i("AdapterForGithubIDInput","onBindViewHolder called ,id : "+id);
        holder.editTextID.setText(id);
        //holder.valid.setImageResource(checkValidity(id));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public void removeAt(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataset.size());
    }
    public ArrayList<String> getRefinedMembers(){//if return is null it isn't ready to make a team
        ArrayList<String> nonEmptyIDS =(ArrayList<String>) mDataset.clone();

        for(int i =0;i<nonEmptyIDS.size();i++){
            String checkingID =nonEmptyIDS.get(i);
            if(checkValidity(checkingID)!=VALID){
                return null;
            }
        }
        if(nonEmptyIDS.isEmpty()){
            return null;
        }
        return nonEmptyIDS;
    }
}
