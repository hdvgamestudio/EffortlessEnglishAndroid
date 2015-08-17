package com.hdv.effortlessenglish.fragment;

import com.hdv.effortlessenglish.activity.R;
import com.hdv.effortlessenglish.utils.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentHelp extends Fragment {

	int i;

	public FragmentHelp() {
		// TODO Auto-generated constructor stub
	}

	public FragmentHelp(int i) {
		this.i = i;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.helpfragment, container,
				false);

		TextView txtHelp = (TextView) rootView.findViewById(R.id.txtHelp);

		switch (i) {
		case 0:
			String str0 = Utils.readTxt(getActivity(), "3dieu.txt");
			txtHelp.setText(str0);
			break;
		case 1:
			String str1 = Utils.readTxt(getActivity(), "7_rule.txt");
			txtHelp.setText(str1);
			break;
		case 2:
			String str2 = Utils.readTxt(getActivity(), "kehoach.txt");
			txtHelp.setText(str2);
			break;
		case 3:
			String str3 = Utils.readTxt(getActivity(), "thutuhoc.txt");
			txtHelp.setText(str3);
			break;
		}

		return rootView;
	}
	
	
}
