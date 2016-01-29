package com.nuttwarunyu.blankbook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

/**
 * Created by Dell-NB on 28/12/2558.
 */
public class MyPageViewAdapter extends FragmentPagerAdapter {

    int PAGE_COUNT = 2;
    protected static final String[] PAGE_NAME = new String[]{"บทความ", "เรื่องที่เขียน"};

    public MyPageViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new MainActivity();
        }
        if (position == 1) {
            return new MyBookHistory();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return MyPageViewAdapter.PAGE_NAME[position % PAGE_NAME.length];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
