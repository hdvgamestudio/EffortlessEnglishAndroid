package com.hdv.effortlessenglish.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdv.effortlessenglish.activity.R;
import com.hdv.effortlessenglish.model.LevelDVD;

public class GvMainAdapter extends ArrayAdapter<LevelDVD>{

	Activity activity;
	ArrayList<LevelDVD> listSrc;
	
	public GvMainAdapter(Activity activity, int resource, ArrayList<LevelDVD> listSrc) {
		super(activity, resource, listSrc);
		// TODO Auto-generated constructor stub
		
		this.activity = activity;
		this.listSrc = listSrc;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.customgridviewmain, null);
		
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);
		TextView textView = (TextView) rowView.findViewById(R.id.textView1);
		
		//set cho rowview
		
		textView.setText(listSrc.get(position).getTitleDVD());
		imageView.setImageResource(R.drawable.music);
		
		
		return rowView;
	}
	
	

}
