package models;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import fragments.ModulesFragment;
import fragments.TabsFragment;

/**
 * Created by packa on 23/11/2015.
 */
public class TabsAdapter extends FragmentStatePagerAdapter {

    private CharSequence Titles[] = new String[] {"semestre 0", "semestre 1", "semestre 2", "semestre 3", "semestre 4", "semestre 5", "semestre 6", "semestre 7", "semestre 8", "semestre 9", "semestre 10"};

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TabsFragment.newInstance("0");
            case 1:
                return TabsFragment.newInstance("1");
            case 2:
                return TabsFragment.newInstance("2");
            case 3:
                return TabsFragment.newInstance("3");
            case 4:
                return TabsFragment.newInstance("4");
            case 5:
                return TabsFragment.newInstance("5");
            case 6:
                return TabsFragment.newInstance("6");
            case 7:
                return TabsFragment.newInstance("7");
            case 8:
                return TabsFragment.newInstance("8");
            case 9:
                return TabsFragment.newInstance("9");
            case 10:
                return TabsFragment.newInstance("10");
        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return 10;
    }
}
