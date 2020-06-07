package com.example.photonetalbum;

import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title, int index) {
        mFragmentList.add(index, fragment);
        mFragmentTitleList.add(index, title);
    }

    public void changeFragment(Fragment fragment, String title, int index) {
        mFragmentTitleList.set(index, title);
        mFragmentList.add(fragment);
        mFragmentTitleList.add("+");
        notifyDataSetChanged();
    }
}