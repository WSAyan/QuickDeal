package com.example.limit.quickdealfinal.Activity.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.example.limit.quickdealfinal.Activity.Fragment.ComputersFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.DressFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.OthersFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.PhoneFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.RecentFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.SportsFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.VehicleFragment;

import java.util.List;

/**
 * Created by tanvir_pias on 22-07-15.
 */
public class MyPagerAdapte extends FragmentStatePagerAdapter
{
    List<String> tabTitlesList;
    Context context;

    public MyPagerAdapte(Context context,FragmentManager fm, List<String> tabTitlesList)
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
            Fragment mfragment=new RecentFragment();
            //Bundle bundle=new Bundle();
            //bundle.putInt("position",position);
            //bundle.putString("tabname",tabTitlesList.get(0));
            //mfragment.setArguments(bundle);
            return mfragment;
        }
        if (position==1)
        {
            Fragment mfragment=new DressFragment();
            //Bundle bundle=new Bundle();
            //bundle.putInt("position",position);
            //bundle.putString("tabname",tabTitlesList.get(0));
            //mfragment.setArguments(bundle);
            return mfragment;
        }
        if (position==2)
        {
            Fragment mfragment = new SportsFragment();
            //Bundle bundle=new Bundle();
            //bundle.putInt("position",position);
            //bundle.putString("tabname",tabTitlesList.get(0));
            //mfragment.setArguments(bundle);
            return mfragment;
        }

        else if (position==3)
        {
            Fragment mfragment=new ComputersFragment();
            //Bundle bundle=new Bundle();
            //bundle.putInt("position",position);
            //bundle.putString("tabname",tabTitlesList.get(0));
            //mfragment.setArguments(bundle);
            return mfragment;
        }

        else if (position==4)
        {
            Fragment mfragment=new PhoneFragment();
            //Bundle bundle=new Bundle();
            //bundle.putInt("position",position);
            //bundle.putString("tabname",tabTitlesList.get(0));
            //mfragment.setArguments(bundle);
            return mfragment;
        }
        else if (position==5)
        {
            Fragment mfragment=new VehicleFragment();
            //Bundle bundle=new Bundle();
            //bundle.putInt("position",position);
            //bundle.putString("tabname",tabTitlesList.get(0));
            //mfragment.setArguments(bundle);
            return mfragment;
        }
        else
        {
            Fragment mfragment=new OthersFragment();
            //Bundle bundle=new Bundle();
            //bundle.putInt("position",position);
            //bundle.putString("tabname",tabTitlesList.get(0));
            //mfragment.setArguments(bundle);
            return mfragment;
        }
    }
}