package com.jvidal.worksmarter.Adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jvidal.worksmarter.Activities.AddAnomaliesActivity;
import com.jvidal.worksmarter.Fragments.BillboardAnomalyFragment;
import com.jvidal.worksmarter.Fragments.CivilAnomalyFragment;
import com.jvidal.worksmarter.Fragments.ElectricAnomalyFragment;

public class AnomaliesTabLayoutAdapter extends FragmentPagerAdapter {

    int totalTabs;
    AddAnomaliesActivity myContext;


    public AnomaliesTabLayoutAdapter(AddAnomaliesActivity context, FragmentManager fm, int totalTabs) {
        super(fm);

        myContext = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CivilAnomalyFragment civilAnomalyFragment1 = new CivilAnomalyFragment();
                return civilAnomalyFragment1;
            case 1:
                ElectricAnomalyFragment electricAnomalyFragment = new ElectricAnomalyFragment();
                return electricAnomalyFragment;
            case 2:
                BillboardAnomalyFragment billboardAnomalyFragment = new BillboardAnomalyFragment();
                return billboardAnomalyFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
