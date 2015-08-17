package com.hdv.effortlessenglish.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hdv.effortlessenglish.adapter.GvMainAdapter;
import com.hdv.effortlessenglish.model.LevelDVD;
import com.hdv.effortlessenglish.utils.Utils;

public class MainActivity extends ActionBarActivity {

	ProgressBar progressBar;
	GridView gridView;
	GvMainAdapter gvMainAdapter;
	ArrayList<LevelDVD> listSrc;

	AdView adView; // Banner
	AdRequest adRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Banner
		adView = (AdView) findViewById(R.id.adView);
		adRequest = new AdRequest.Builder().addTestDevice(
				"8C654AC937891D1DADC267C0197ED0E1").build();
		adView.loadAd(adRequest);

		android.support.v7.app.ActionBar actionBar;
		actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#990E15")));
		actionBar.setTitle("Effortless English");

		// tao preferences de hien thi 7 rule khi nguoi dung moi cai xong app
		SharedPreferences appPrefs = getSharedPreferences("ee", MODE_PRIVATE);
		SharedPreferences.Editor editor = appPrefs.edit();

		// lay ra 7rule
		if (appPrefs.getString("7rule", null) != null) {
			// Toast.makeText(getApplicationContext(), "da co 7rule",
			// Toast.LENGTH_LONG).show();
			// showDialog(0);
			// editor.clear();
			// editor.commit();
		} else {
			Toast.makeText(getApplicationContext(),
					"Xem kỹ hướng dẫn trước khi học", Toast.LENGTH_LONG).show();
			editor.putString("7rule", "Zzz");
			editor.commit();
		}

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);

		gridView = (GridView) findViewById(R.id.gridView);
		listSrc = new ArrayList<LevelDVD>();
		gvMainAdapter = new GvMainAdapter(this, R.layout.customgridviewmain,
				listSrc);

		gridView.setAdapter(gvMainAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(MainActivity.this,
						LessonActivity.class);
				intent.putExtra("jsonItemsLesson", listSrc.get(position)
						.getStringJsonItemLesson());
				startActivity(intent);
			}

		});

		if (!Utils.checkInternet(this)) {
			Toast.makeText(getApplicationContext(),
					"Kiểm tra lại kết nối internet và thử lại",
					Toast.LENGTH_LONG).show();
		} else {
			EETask task = new EETask();
			task.execute("https://onedrive.live.com/download?resid=A88065E28F45B4FF!2731&authkey=!AHwLGA1LG7FvhdM&ithint=file%2ctxt");
		}

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_help) {
			Intent intent = new Intent(MainActivity.this, HelpActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// class noi lay ve du lieu cho gvMain
	class EETask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = params[0];
			String line = null;

			try {
				// lay ve fille txt
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				HttpResponse response = httpClient.execute(httpGet);
				HttpEntity entity = response.getEntity();

				BufferedHttpEntity buf = new BufferedHttpEntity(entity);

				// doc file txt
				InputStream is = buf.getContent();

				BufferedReader r = new BufferedReader(new InputStreamReader(is));

				StringBuilder total = new StringBuilder();

				while ((line = r.readLine()) != null) {
					total.append(line + "\n");
				}

				String result = total.toString();
				// Log.d("Get URL", "Downloaded string: " + result);
				return result;
			} catch (Exception e) {
				Log.e("Get Url", "Error in downloading: " + e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			progressBar.setVisibility(View.INVISIBLE);
			// nhan ve chuoi json va xu ly
			try {
				JSONObject jObject = new JSONObject(result);
				JSONArray jArray = jObject.getJSONArray("levelDVD");

				for (int i = 0; i < jArray.length(); i++) {
					LevelDVD levelDVD = new LevelDVD();
					levelDVD.setTitleDVD(jArray.getJSONObject(i).getString(
							"titleDVD"));
					levelDVD.setStringJsonItemLesson(jArray.getJSONObject(i)
							.getJSONArray("itemsLesson").toString());

					listSrc.add(levelDVD);
				}

				// LevelDVD dvdHuongDan = new LevelDVD();
				// dvdHuongDan.setTitleDVD("HÆ°á»›ng Dáº«n");
				// dvdHuongDan.setStringJsonItemLesson(" ");// chua da set duong
				// // dan
				// listSrc.add(dvdHuongDan);

				gvMainAdapter.notifyDataSetChanged();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d("parse err", "loi ko chuyen dc json");
				e.printStackTrace();
			}

		}
	}
}
