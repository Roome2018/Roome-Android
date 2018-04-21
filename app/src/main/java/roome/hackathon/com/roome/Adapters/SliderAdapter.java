package roome.hackathon.com.roome.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import roome.hackathon.com.roome.Fragments.PagerFragment;
import roome.hackathon.com.roome.Models.Medium;

public class SliderAdapter extends FragmentStatePagerAdapter {

ArrayList<Medium> arrSlider;

   public SliderAdapter(FragmentManager fm, ArrayList<Medium> arrSlider) {
        super(fm);
        this.arrSlider=arrSlider;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new PagerFragment();
        Bundle args = new Bundle();
        args.putInt(PagerFragment.ARG_OBJECT, i);
        args.putParcelableArrayList("arrSlider",arrSlider);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return arrSlider.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}