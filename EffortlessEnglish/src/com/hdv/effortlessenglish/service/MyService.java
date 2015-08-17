package com.hdv.effortlessenglish.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.hdv.effortlessenglish.model.DownloadTask;

import android.R.integer;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service implements OnPreparedListener,
		OnErrorListener {

	public MediaPlayer player;
	private static MyService instance;
	private ProgressDialog dialog;

	public MyService() {
		MyService.instance = this;
	}

	public static MyService getInstance() {
		return instance;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("MyService", "onBind");
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d("MyService", "onCreate");
		// khoi tao bien player tu res
		// player.setOnPreparedListener(this);
		player = new MediaPlayer();
		player.setOnPreparedListener(this);
		player.setOnErrorListener(this);
		player.reset();
		urlPlaying = " ";
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		super.onCreate();
	}

	private static boolean STARTED = false;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("MyService", "onStartCommand");
		// if (STARTED) {
		// return Service.START_STICKY;
		// }
		// if (Build.VERSION.SDK_INT < 18) {
		// startForeground(6677028, new Notification());
		// this.onStart(intent, startId);
		// }
		// STARTED = true;
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("MyService", "onDestroy");
		if (player.isPlaying()) {
			// neu dang choi nhac thi stop file nhac
			// closeNoti();00------
			stopMedia();
		}
		instance = null;
		super.onDestroy();
	}

	String urlPlaying = " ";

	public void startSrc(String url ,ProgressDialog dialog) {
//		try {
//			urlPlaying = url;
//			player.reset();
//			player.setDataSource(url);
//			player.prepare();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		this.dialog= dialog;
		LoadMp3Task loadMp3Task = (LoadMp3Task) new LoadMp3Task().execute(url);
	}

	public boolean checkUrlPlaying(String url) {

		if (urlPlaying == null)
			return false;
		if (urlPlaying.equals(url))
			return true;
		else {
			return false;
		}
	}

	public void startMedia() {
		if (!player.isPlaying()) {
			player.start();
			player.setLooping(true);
		}
	}

	public void stopMedia() {

		player.stop();
		player.release();
		// player = null;
	}

	public void pauseMedia() {
		player.pause();
	}

	// lay ve thoi luong bai hat
	public int getDuration() {
		return player.getDuration();
	}

	public int getCurrentPosition() {
		return player.getCurrentPosition();
	}

	public void seekTo(int i) {
		player.seekTo(i);
	}

	public boolean isPlaying() {
		return player.isPlaying();
	}

	boolean isLoaded = false;

	public boolean isLoaded() {
		return isLoaded;
	}
	
	// class de load nhac tu mang
	public class LoadMp3Task extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			player.reset();
			try {
				player.setDataSource(params[0]);
				
				urlPlaying = params[0];
				player.prepare();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// player.start();
			dialog.dismiss();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		
		startMedia();
		player.setLooping(true);
		isLoaded = true;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		mp.stop();
		Log.d("error", "loi");
		return false;
	}
}
