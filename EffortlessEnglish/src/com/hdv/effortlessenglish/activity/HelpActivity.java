package com.hdv.effortlessenglish.activity;

import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hdv.effortlessenglish.fragment.TabsHelpAdapter;
import com.hdv.effortlessenglish.fragment.TabsPagerAdapter;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar.LayoutParams;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HelpActivity extends ActionBarActivity implements TabListener {

	ViewPager paperHelp;
	android.support.v7.app.ActionBar actionBar;
	ArrayList<String> tabTitle;
	TabsHelpAdapter tabHelpPager;

	AdView adView; // Banner
	AdRequest adRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		// Banner
		adView = (AdView) findViewById(R.id.adView);
		adRequest = new AdRequest.Builder().addTestDevice(
				"8C654AC937891D1DADC267C0197ED0E1").build();
		adView.loadAd(adRequest);

		tabTitle = new ArrayList<String>();
		tabTitle.add("3 điều cơ bản");
		tabTitle.add("7 quy tắc");
		tabTitle.add("Kế hoạch học");
		tabTitle.add("Thứ tự học");

		actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#990E15")));
		actionBar.setTitle("Hướng Dẫn");

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		tabHelpPager = new TabsHelpAdapter(getSupportFragmentManager());

		paperHelp = (ViewPager) findViewById(R.id.paperHelp);
		paperHelp.setAdapter(tabHelpPager);

		// khoi tao tab

		for (int i = 0; i < tabTitle.size(); i++) {
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

			Tab tab = actionBar.newTab();

			tab.setTabListener(this);

			tab.setCustomView(R.layout.customactionbar);
			tab.getCustomView().setLayoutParams(layoutParams);

			TextView txtTab = (TextView) tab.getCustomView().findViewById(
					R.id.txtTitleTab);
			txtTab.setText(tabTitle.get(i));

			actionBar.setStackedBackgroundDrawable(new ColorDrawable(
					getResources().getColor(R.color.backgroundtab)));

			actionBar.addTab(tab);

		}

		paperHelp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				actionBar.setSelectedNavigationItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		paperHelp.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (adView != null) {
			adView.pause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (adView != null) {
			adView.destroy();
		}
	}
}
