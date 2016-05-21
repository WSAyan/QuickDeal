package com.example.limit.quickdealfinal.Activity.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.limit.quickdealfinal.R;


public class GridViewHolder
{
    ImageView image;
    TextView name;
    TextView price;


    public GridViewHolder(View v)
    {
        super();
        image = (ImageView) v.findViewById(R.id.image);
        name = (TextView) v.findViewById(R.id.name_txtview);
        price = (TextView) v.findViewById(R.id.price_1);
    }

}
