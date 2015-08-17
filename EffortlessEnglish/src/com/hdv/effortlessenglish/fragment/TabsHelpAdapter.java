package com.hdv.effortlessenglish.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsHelpAdapter extends FragmentPagerAdapter{

	public TabsHelpAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return new FragmentHelp(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}

}
