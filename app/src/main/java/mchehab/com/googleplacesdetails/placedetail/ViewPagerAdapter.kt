package mchehab.com.googleplacesdetails.placedetail

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager,
                       val listFragments: List<Fragment>,
                       val listTitle: List<String>): FragmentPagerAdapter(fragmentManager) {
    override fun getItem(item: Int): Fragment {
        return listFragments[item]
    }

    override fun getCount(): Int {
        return listFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }

}