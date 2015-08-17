package com.hdv .effortlessenglish.fragment;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	ArrayList<MyFragment> listMyFragment;

	public TabsPagerAdapter(FragmentManager fm, ArrayList<MyFragment> listMyFragment) {
		super(fm);
		this.listMyFragment=listMyFragment;
	}

	@Override
	public Fragment getItem(int arg0) {
		// switch (arg0) {
		// case 0:
		// return new MyFragment();
		// case 1:
		// return new MyFragment();
		//
		// }
		return listMyFragment.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listMyFragment.size();
	}

}
