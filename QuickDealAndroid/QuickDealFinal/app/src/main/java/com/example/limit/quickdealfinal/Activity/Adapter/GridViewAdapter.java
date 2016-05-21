package com.example.limit.quickdealfinal.Activity.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.limit.quickdealfinal.Activity.Pojo.GridRowItem;
import com.example.limit.quickdealfinal.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class GridViewAdapter extends BaseAdapter
{
    Context context;
    List<GridRowItem> eachArrayList;


    public GridViewAdapter(Context context, List<GridRowItem> eachArrayList)
    {
        this.context = context;
        this.eachArrayList = eachArrayList;
    }

    @Override
    public int getCount()
    {
        return eachArrayList.size();
    }

    @Override
    public Object getItem(int i)
    {
        return eachArrayList.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View r=view;
        GridViewHolder gridholder=null;

        if(r==null)
        {
            LayoutInflater inflt = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            r=inflt.inflate(R.layout.grid_item_layout, viewGroup,false);
            gridholder=new GridViewHolder(r);
            r.setTag(gridholder);
        }

        else
        {
            gridholder=(GridViewHolder) r.getTag();
        }

        GridRowItem t=eachArrayList.get(i);

        Picasso.with(context)
                .load(t.getImage())
                .into(gridholder.image);

        //gridholder.image.setImageResource(t.getImage());
        Log.d("settext",t.getPrice());
        gridholder.name.setText(t.getName()+"");
        gridholder.price.setText(t.getPrice()+" ৳");

        return r;
    }
}