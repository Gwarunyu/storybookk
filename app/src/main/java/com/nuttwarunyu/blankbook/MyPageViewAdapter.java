package com.nuttwarunyu.blankbook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Dell-NB on 28/12/2558.
 */
public class MyPageViewAdapter extends FragmentPagerAdapter {

    int PAGE_COUNT = 3;

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
    public int getCount() {
        return 3;
    }
}
