package com.dabinu.apps.skeda.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dabinu.apps.skeda.fragments.CommunFragment;
import com.dabinu.apps.skeda.fragments.ConnectivityFragment;
import com.dabinu.apps.skeda.fragments.SystemFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int numbOfTabs;


    public ViewPagerAdapter(FragmentManager fm, int numbOfTabs){
        super(fm);
        this.numbOfTabs = numbOfTabs;
    }



    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new SystemFragment();
            case 1:
                return new ConnectivityFragment();
            case 2:
                return new CommunFragment();
        }
        return null;
    }



    @Override
    public int getCount() {
        return numbOfTabs;
    }
}
