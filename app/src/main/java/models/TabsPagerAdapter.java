package models;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import fragments.ModuleInfoFragment;
import fragments.ModulesFragment;
import fragments.ProjectsFragment;

/**
 * Created by packa on 16/11/2015.
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence Titles[] = new String[] {"informations", "projets"};

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ModuleInfoFragment();
            case 1:
                return new ProjectsFragment();
        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}
