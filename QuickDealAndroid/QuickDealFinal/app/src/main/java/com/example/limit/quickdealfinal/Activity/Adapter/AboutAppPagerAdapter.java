package com.example.limit.quickdealfinal.Activity.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.limit.quickdealfinal.Activity.Fragment.AboutFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.ComputersFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.DressFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.HelpFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.OthersFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.PhoneFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.RecentFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.SportsFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.VehicleFragment;

import java.util.List;

/**
 * Created by Limit on 12/7/2015.
 */
public class AboutAppPagerAdapter extends FragmentStatePagerAdapter
{
    List<String> tabTitlesList;
    Context context;

    public AboutAppPagerAdapter(Context context, FragmentManager fm, List<String> tabTitlesList)
    {
        super(fm);
        this.context = context;
        this.tabTitlesList = tabTitlesList;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return tabTitlesList.get(position);
    }


    @Override
    public int getCount()
    {
        return tabTitlesList.size();
    }

    @Override
    public Fragment getItem(int position)
    {
        //return SuperAwesomeCardFragment.newInstance(position);

        //Toast.makeText(context,tabTitlesList.get(position)+":"+position,Toast.LENGTH_LONG).show();
        if (position==0)
        {
            Fragment mfragment=new HelpFragment();
            return mfragment;
        }
        else
        {
            Fragment mfragment=new AboutFragment();
            return mfragment;
        }
    }
}