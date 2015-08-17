package com.hdv.effortlessenglish.fragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hdv.effortlessenglish.activity.R;
import com.hdv.effortlessenglish.model.ItemTest;
import com.hdv.effortlessenglish.service.MyService;
import com.hdv.effortlessenglish.utils.Utils;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;

public class MyFragment extends Fragment implements OnClickListener {

	public interface IGetItem {
		public ItemTest getItemsTest(int fragmentName);

		public ItemTest getItemSelected();

		public void initNotifi(MyService myService, String musicName);

		public void closeNotifi(MyService myService);

	}

	IGetItem item;
	private int fragmentName;

	enum State {
		Playing, Paused, NotStart
	}

	private ToggleButton toggleButton;
	private SeekBar seekBar;
	private TextView txtTime1, txtTime2, txtText, txtTitle, textClick;
	private ImageView imgDisk;

	private State mState = State.NotStart;

	private MyService myService;
	private UpdateProcessTask updateProcessTask;

	// xu ly text
	private String textRead;
	private boolean isShow = false;

	// end xu ly text
	public boolean flag = true;
	ItemTest itemWatching, itemSelected;

	public MyFragment() {

	}

	public MyFragment(int fragmentName) {
		this.fragmentName = fragmentName;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		item = (IGetItem) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment, container, false);
		// Log.d("fragment", "oncreateview" + fragmentName);
		init(rootView);
		return rootView;
	}

	public void init(View rootView) {
		toggleButton = (ToggleButton) rootView
				.findViewById(R.id.playPauseButton);
		seekBar = (SeekBar) rootView.findViewById(R.id.seekBar1);
		txtTime1 = (TextView) rootView.findViewById(R.id.txtTime1);
		txtTime2 = (TextView) rootView.findViewById(R.id.txtTime2);
		txtText = (TextView) rootView.findViewById(R.id.txtText);
		txtTitle = (TextView) rootView.findViewById(R.id.txtTitle);
		textClick = (TextView) rootView.findViewById(R.id.textClick);
		imgDisk = (ImageView) rootView.findViewById(R.id.imgDisk);

		imgDisk.setOnClickListener(this);
		txtText.setOnClickListener(this);
		textClick.setOnClickListener(this);

		AdBuddiz.setPublisherKey("a264138b-0c52-4251-91da-925374d20e5a");
		AdBuddiz.cacheAds(getActivity()); // this = current Activity
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("fragment", "onpause" + fragmentName);
		flag = false;
	}

	@Override
	public void onResume() {
		Log.d("fragment", "onresume" + fragmentName);
		super.onResume();

		itemWatching = item.getItemsTest(fragmentName);
		itemSelected = item.getItemSelected();

		// kiem tra neu nhu co service dang chay (co nhac dang phat)
		if (MyService.getInstance() != null) {
			myService = MyService.getInstance();
			// kiem tra xem bai nhac co dang dc phat trong fragment hay ko
			if (myService.checkUrlPlaying(itemWatching.getUrlMp3())) {
				// neu co thi cap nhat seekbar va cac text
				updateSeekBar();
				// mState = State.Playing;
				if (myService.isPlaying()) {
					toggleButton.setChecked(true);// dang chay nhac
					mState = State.Playing;
				} else {
					toggleButton.setChecked(false);// dang pause
					mState = State.Paused;
				}
			} else {
				toggleButton.setChecked(false);
				mState = State.NotStart;
				seekBar.setProgress(0);
			}

		}

		// load du lieu cho fragment
		txtTitle.setText(itemWatching.getTitle());
		// txtText.setText(itemWatching.getUrlMp3());

		toggleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("toggel", "--" + fragmentName);

				myService = MyService.getInstance();
				if (myService.checkUrlPlaying(itemWatching.getUrlMp3())) {
					if (toggleButton.isChecked()) {
						// bam vao play
						
						updateSeekBar();
						myService.startMedia();
						item.initNotifi(myService, itemWatching.getTitle());

					} else {
						// bam vao pase
						myService.pauseMedia();
						mState = State.Paused;
						// closeNoti();
						item.closeNotifi(myService);
					}
				} else {
					// neu khong phai bai dang phat thi trang thai phai la
					// notstart
					if (toggleButton.isChecked()
							& mState.equals(State.NotStart)) {
						
						AdBuddiz.showAd(getActivity());

						ProgressDialog dialog = ProgressDialog.show(
								getActivity(), "Loading Data..........",
								"Please Wait...", true);
						myService.startSrc(itemWatching.getUrlMp3(), dialog);
						txtTime2.setText("Loading...");
						updateSeekBar();
						item.initNotifi(myService, itemWatching.getTitle());

					}
				}
			}
		});

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (myService != null) {
					if (fromUser & myService.isLoaded()) {
						if (mState.equals(State.Playing)) {
							myService.seekTo(progress);
							// myService.startMedia();
						}
						if (mState.equals(State.Paused)) {
							myService.seekTo(progress);
							myService.startMedia();
							toggleButton.setChecked(true);
							item.initNotifi(myService, itemWatching.getTitle());
						}
					}
				}
			}
		});

	}

	public void updateSeekBar() {
		flag = true;
		mState = State.Playing;
		if (myService != null) {
			updateProcessTask = new UpdateProcessTask();
			updateProcessTask.execute(" ");
		}
	}

	// // update seekbar
	class UpdateProcessTask extends AsyncTask<String, Integer, Void> {

		int time = 0;// tinh theo ms

		public UpdateProcessTask() {
			Log.d("Zzz", "update process");
		}

		@Override
		protected Void doInBackground(String... params) {
			while (flag) {
				time = myService.getCurrentPosition();
				publishProgress(time);
				SystemClock.sleep(500);
			}
			time = 0;
			publishProgress(time);
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);

			if (myService != null) {
				seekBar.setProgress(values[0]);
				seekBar.setMax(myService.getDuration());
				txtTime1.setText(Utils.milliSecondsToTimer(values[0]));
				txtTime2.setText(Utils.milliSecondsToTimer(myService
						.getDuration()));
			}
		}
	}

	// class get text from url
	class GetTextTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
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
			txtText.setText(result);
			textRead = result;
			imgDisk.setVisibility(View.INVISIBLE);
			textClick.setVisibility(View.INVISIBLE);
			isShow = true;
		}
	}

	// xu kien click
	@Override
	public void onClick(View v) {
		if (v == imgDisk || v == txtText || v == textClick) {
			
			 AdBuddiz.showAd(getActivity());
			
			GetTextTask getTextTask = new GetTextTask();
			if (!isShow) {
				String url = itemWatching.getUrlPDF();
				if (textRead == null) {
					if (Utils.checkURL(url)) {
						getTextTask.execute(url);
						txtText.setText("Loading...........");
						imgDisk.setVisibility(View.VISIBLE);
						textClick.setVisibility(View.VISIBLE);
					} else {
						txtText.setText("Không có bài đọc tương ứng!");
					}
				} else {
					txtText.setText(textRead);
					isShow = true;
					imgDisk.setVisibility(View.INVISIBLE);
					textClick.setVisibility(View.INVISIBLE);
				}
			} else {
				// getTextTask.cancel(true);
				txtText.setText("  ");
				isShow = false;
				imgDisk.setVisibility(View.VISIBLE);
				textClick.setVisibility(View.VISIBLE);
			}

		}
	}
}
