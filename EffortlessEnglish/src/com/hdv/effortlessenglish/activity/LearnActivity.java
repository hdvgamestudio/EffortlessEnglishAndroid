package com.hdv.effortlessenglish.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hdv.effortlessenglish.fragment.MyFragment;
import com.hdv.effortlessenglish.fragment.MyFragment.IGetItem;
import com.hdv.effortlessenglish.fragment.TabsPagerAdapter;
import com.hdv.effortlessenglish.model.ItemTest;
import com.hdv.effortlessenglish.service.MyService;

public class LearnActivity extends ActionBarActivity implements TabListener,
		IGetItem {

	ViewPager viewPager;
	android.support.v7.app.ActionBar actionBar;
	ArrayList<String> tabTitle;
	TabsPagerAdapter tabPager;

	ArrayList<ItemTest> listItemTest;
	ArrayList<MyFragment> listMyFragment;

	String jsonItemsTest; // chuoi json lay dc tu lessonactivity
	String jsonItemsLesson; // chuoi json de cho nut back

	// MyService myService;
	static boolean stopService = false;
	ItemTest itemSelected = new ItemTest();

	AdView adView; // Banner
	AdRequest adRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn);

		// Banner
		adView = (AdView) findViewById(R.id.adView);
		adRequest = new AdRequest.Builder().addTestDevice(
				"8C654AC937891D1DADC267C0197ED0E1").build();
		adView.loadAd(adRequest);

		Intent intent1 = getIntent();
		jsonItemsTest = intent1.getStringExtra("jsonItemsTest");
		jsonItemsLesson = intent1.getStringExtra("jsonItemsLesson");
		int isNotifi = intent1.getIntExtra("isNotifi", 0);

		//
		listItemTest = getListItem(jsonItemsTest);
		listMyFragment = new ArrayList<MyFragment>();
		for (int i = 0; i < listItemTest.size(); i++) {
			listMyFragment.add(new MyFragment(i));
		}

		init();

		Intent intent = new Intent(LearnActivity.this, MyService.class);
		startService(intent);

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
		// TODO Auto-generated method stub
		super.onDestroy();
		if (adView != null) {
			adView.destroy();
		}

		if (MyService.getInstance() != null) {
			if (!MyService.getInstance().isPlaying()) {
				stopService(new Intent(LearnActivity.this, MyService.class));
			} else {
				stopService = true;
			}

		} else {
			stopService(new Intent(LearnActivity.this, MyService.class));
		}

	}

	public void init() {
		viewPager = (ViewPager) findViewById(R.id.paper);
		tabTitle = new ArrayList<String>();

		actionBar = getSupportActionBar();
		// actionBar.setDisplayHomeAsUpEnabled(true); // hien ra button back
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#990E15")));

		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view = inflater.inflate(R.layout.customactionbarheader, null);

		ImageButton btnBack = (ImageButton) view.findViewById(R.id.btnBack);
		TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);

		txtTitle.setText(listItemTest.get(0).getTitle());
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LearnActivity.this,
						LessonActivity.class);
				intent.putExtra("jsonItemsLesson", jsonItemsLesson);
				startActivity(intent);
				finish();
			}
		});
		actionBar.setCustomView(view);

		// tao tab
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		tabPager = new TabsPagerAdapter(getSupportFragmentManager(),
				listMyFragment);

		viewPager.setAdapter(tabPager);
		// khoi tao cac tab
		for (int i = 0; i < listItemTest.size(); i++) {

			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			Tab tab = actionBar.newTab();
			tab.setTabListener(this);
			// tab.setText(listItemTest.get(i).getTitle());
			tab.setCustomView(R.layout.customactionbar);
			tab.getCustomView().setLayoutParams(layoutParams);

			TextView txtTab = (TextView) tab.getCustomView().findViewById(
					R.id.txtTitleTab);
			txtTab.setText(listItemTest.get(i).getTitle());

			actionBar.setStackedBackgroundDrawable(new ColorDrawable(
					getResources().getColor(R.color.backgroundtab)));
			actionBar.addTab(tab);
		}

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

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

	public ArrayList<ItemTest> getListItem(String json) {
		ArrayList<ItemTest> listItemTest = new ArrayList<ItemTest>();

		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				ItemTest itemTest = new ItemTest();
				itemTest.setTitle(jsonObject.getString("title"));
				itemTest.setUrlMp3(jsonObject.getString("urlMp3"));
				itemTest.setUrlPDF(jsonObject.getString("urlPDF"));

				listItemTest.add(itemTest);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listItemTest;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		// Log.d("learn", "tapreselect"+ arg0.getPosition());
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
		listMyFragment.get(tab.getPosition()).flag = true;
		Log.d("learn", "tapselect" + tab.getPosition());
		itemSelected = listItemTest.get(tab.getPosition());

		if (MyService.getInstance() != null) {

			if (MyService.getInstance().checkUrlPlaying(
					itemSelected.getUrlMp3())) {
				listMyFragment.get(tab.getPosition()).updateSeekBar();

			}
		}
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		listMyFragment.get(arg0.getPosition()).flag = false;
		Log.d("learn", "tapunselect" + arg0.getPosition());
	}

	@Override
	public ItemTest getItemsTest(int fragmentName) {
		// TODO Auto-generated method stub
		return listItemTest.get(fragmentName);
	}

	@Override
	public void initNotifi(MyService myService, String musicName) {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		RemoteViews notificationView = new RemoteViews(getPackageName(),
				R.layout.customnotification);

		Intent notificationIntent = new Intent(this, LearnActivity.class);
		notificationIntent.putExtra("jsonItemsTest", jsonItemsTest);
		notificationIntent.putExtra("jsonItemsLesson", jsonItemsLesson);
		notificationIntent.putExtra("isNotifi", 1);

		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
				this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.musicic).setAutoCancel(true)
				.setContentIntent(pendingNotificationIntent)
				.setContent(notificationView);

		notificationView.setTextViewText(R.id.appName, musicName);

		// button is clicked
		Intent switchIntent = new Intent(this, switchButtonListener.class);
		PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 0,
				switchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		notificationView.setOnClickPendingIntent(R.id.closeOnFlash,
				pendingSwitchIntent);

		myService.startForeground(MyService.START_CONTINUATION_MASK,
				builder.build());
	}

	@Override
	public void closeNotifi(MyService myService) {
		myService.stopForeground(true);
	}

	public static class switchButtonListener extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("Here", "I am here");

			if (stopService) {
				MyService.getInstance().stopSelf();
				MyService.getInstance().stopForeground(true);
			} else {
				Toast.makeText(context, "Thoat sau khi kill app",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	public ItemTest getItemSelected() {
		// TODO Auto-generated method stub
		return itemSelected;
	}

}
