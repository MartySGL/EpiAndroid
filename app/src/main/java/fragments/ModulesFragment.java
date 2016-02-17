package fragments;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epiandroid.R;

import models.TabsAdapter;

public class ModulesFragment extends Fragment {

    public ModulesFragment() {
    }

    ViewPager pager;
    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modules, container, false);

        getActivity().setTitle(R.string.action_mmodules);
        pager = (ViewPager) view.findViewById(R.id.pager2);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout2);
        FragmentManager manager = getFragmentManager();
        PagerAdapter adapter = new TabsAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);

        return view;
    }
}