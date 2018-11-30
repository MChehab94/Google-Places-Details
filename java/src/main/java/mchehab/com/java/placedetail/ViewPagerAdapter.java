package mchehab.com.java.placedetail;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> listFragments;
    private List<String> listTitles;

    public ViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> listFragments, List<String> listTitles) {
        super(fragmentManager);
        this.listFragments = listFragments;
        this.listTitles = listTitles;
    }

    @Override
    public Fragment getItem(int i) {
        return listFragments.get(i);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTitles.get(position);
    }
}