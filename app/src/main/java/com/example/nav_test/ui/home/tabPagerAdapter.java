package com.example.nav_test.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.nav_test.R;

public class tabPagerAdapter extends FragmentPagerAdapter {
    private Context mContext = null;

    private  int tabCount;

    Fragment[] tab_num= null;
    String myID = "noname";



    public tabPagerAdapter(FragmentManager fm, int tabcount,String myId){


        super(fm);
        this.tabCount =tabcount;

        myID = myId;
        tab_num = new Fragment[3];
        tab_num[0] = new page_mygrass(myID);
        tab_num[1] = new page_teamgrass();
        tab_num[2] = new page_newgrass();


    }

    @Override
    public Fragment getItem(int position){
        Log.e("position",Integer.toString(position));

        return tab_num[position];
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
    @Override
    public int getCount() {
        return tabCount;
    }

}