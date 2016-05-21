package com.example.limit.quickdealfinal.Activity.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.limit.quickdealfinal.R;

import java.util.List;

public class CustomList extends ArrayAdapter<String>
{
	private final Activity context;
	private final List<String> title;
	private final List<String> description;
	
	public CustomList(Activity context,List<String> t, List<String> d)
	{
		super(context, R.layout.list_single, t);
		
		this.context = context;
		this.title = t;
		this.description = d;
	}
	
	
	@Override
	public View getView(int position, View view, ViewGroup parent) 
	{
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.list_single, null, true);
		
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
	 
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
	
		txtTitle.setText(title.get(position));
		imageView.setImageResource(R.drawable.background);

		/*
		if(category.get(position).equals("Anivarsary"))
		{
			imageView.setImageResource(R.drawable.anivarsary);
		}
		
		else if(category.get(position).equals("Birthday"))	
		{
			imageView.setImageResource(R.drawable.birthday);
		}
		
		else if(category.get(position).equals("Meeting"))
		{
			imageView.setImageResource(R.drawable.meeting);
		}
		
		else
		{
			imageView.setImageResource(R.drawable.others);
		}
		
	*/
		return rowView;
	}

}