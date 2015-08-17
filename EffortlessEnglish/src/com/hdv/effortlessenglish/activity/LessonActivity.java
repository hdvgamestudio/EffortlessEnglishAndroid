package com.hdv.effortlessenglish.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hdv.effortlessenglish.adapter.GvLessonAdapter;
import com.hdv.effortlessenglish.model.ItemsLesson;

public class LessonActivity extends ActionBarActivity {

	GridView gridView;
	GvLessonAdapter lessonAdapter;
	ArrayList<ItemsLesson> listSrc;

	String jsonItemsLesson;
	AdView adView; // Banner
	AdRequest adRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lesson);

		init();
		Intent intent = getIntent();
		jsonItemsLesson = intent.getStringExtra("jsonItemsLesson");

		getItemLesson(jsonItemsLesson);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(LessonActivity.this,
						LearnActivity.class);
				intent.putExtra("jsonItemsTest", listSrc.get(position)
						.getStringJsonItemTest());
				intent.putExtra("jsonItemsLesson", jsonItemsLesson);
				intent.putExtra("isNotifi", 0);
				startActivity(intent);
			}

		});

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

	public void init() {
		// Banner
		adView = (AdView) findViewById(R.id.adView);
		adRequest = new AdRequest.Builder().addTestDevice(
				"8C654AC937891D1DADC267C0197ED0E1").build();
		adView.loadAd(adRequest);

		//actionbar
		android.support.v7.app.ActionBar actionBar;
		actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#990E15")));
		actionBar.setTitle("Các Bài Học");

		listSrc = new ArrayList<ItemsLesson>();
		lessonAdapter = new GvLessonAdapter(this, R.layout.customgridviewmain,
				listSrc);
		gridView = (GridView) findViewById(R.id.gridView);
		gridView.setAdapter(lessonAdapter);
	}

	// lay item less tu json add vao listSrc
	public void getItemLesson(String json) {
		try {
			JSONArray jsonArray = new JSONArray(json);

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonObject = jsonArray.getJSONObject(i);

				ItemsLesson itemsLesson = new ItemsLesson();
				itemsLesson.setTitleLesson(jsonObject.getString("titleLesson"));
				itemsLesson.setStringJsonItemTest(jsonObject
						.getString("itemsTest"));

				listSrc.add(itemsLesson);
			}

			lessonAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			Log.d("jsonErr", "Khong lay dc json array");
			e.printStackTrace();
		}
	}
}
