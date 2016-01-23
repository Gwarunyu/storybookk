package com.nuttwarunyu.blankbook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

/**
 * Created by Dell-NB on 28/12/2558.
 */
public class MyPageViewAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    int PAGE_COUNT = 3;

    private final int[] ICON_INDICATOR = {R.drawable.com_facebook_button_icon, R.drawable.com_facebook_tooltip_black_topnub
            , R.drawable.ic_launcher};
    protected static final String[] PAGE_NAME = new String[]{"New Feed", "Editor Pick", "Add Content"};

    public MyPageViewAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new MainActivity();
        }
        if (position == 2) {
            return new AddStoryBookActivity();
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
    public int getIconResId(int index) {
        return ICON_INDICATOR[index % ICON_INDICATOR.length];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
