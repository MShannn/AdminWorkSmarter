package com.jvidal.worksmarter.Adapters;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jvidal.worksmarter.Activities.AllImagesActivity;
import com.jvidal.worksmarter.Fragments.AnomaliesImagesFragment;
import com.jvidal.worksmarter.Fragments.StructureImagesFragment;

public class ImagesStructureTabLayoutAdapter extends FragmentPagerAdapter {

    int totalTabs;
    AllImagesActivity myContext;
    String twoDigitCode;
    Bundle bundle;


    public ImagesStructureTabLayoutAdapter(AllImagesActivity context, FragmentManager fm, int totalTabs, String code) {
        super(fm);

        myContext = context;
        this.totalTabs = totalTabs;
        this.twoDigitCode = code;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                StructureImagesFragment structureImagesFragment = new StructureImagesFragment();
                bundle = new Bundle();
                bundle.putString("towDigitCode", twoDigitCode);
                structureImagesFragment.setArguments(bundle);
                return structureImagesFragment;
            case 1:
                AnomaliesImagesFragment anomaliesImagesFragment = new AnomaliesImagesFragment();
                bundle = new Bundle();
                bundle.putString("towDigitCode", twoDigitCode);
                anomaliesImagesFragment.setArguments(bundle);
                return anomaliesImagesFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
