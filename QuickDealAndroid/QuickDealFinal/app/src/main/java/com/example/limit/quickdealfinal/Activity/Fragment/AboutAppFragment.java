package com.example.limit.quickdealfinal.Activity.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.limit.quickdealfinal.Activity.Adapter.AboutAppPagerAdapter;
import com.example.limit.quickdealfinal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Limit on 12/7/2015.
 */
public class AboutAppFragment extends Fragment
{
    private Context context;
    int position=-1;
    private final String[] dhk_tab_titles = {"Help","About"};


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {

            List<String> tabList=new ArrayList<>();

            for (int i=0;i<dhk_tab_titles.length;i++)
            {
                tabList.add(dhk_tab_titles[i]);
            }


            View rootView = inflater.inflate(R.layout.freagment_app_about, container, false);
            // Initialize the ViewPager and set an adapter
            ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);

            pager.setAdapter(new AboutAppPagerAdapter(context,getActivity().getSupportFragmentManager(),tabList));
            pager.setCurrentItem(0);

            // Bind the tabs to the ViewPager
            PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
            tabs.setViewPager(pager);
            tabs.setIndicatorHeight(8);
            tabs.setIndicatorColor(0xFFFFFFFF);
            tabs.setTextColor(0xFFFFFFFF);
            tabs.setAllCaps(false);
            return rootView;
        }

}
