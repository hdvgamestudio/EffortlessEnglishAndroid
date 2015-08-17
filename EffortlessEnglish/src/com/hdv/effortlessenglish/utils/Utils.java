package com.hdv.effortlessenglish.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.util.Patterns;
import android.webkit.URLUtil;

public class Utils {
	
	public static final int IO_BUFFER_SIZE = 8 * 1024;

    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }
        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean checkInternet(Context context) {
        final ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // notify user you are online
            return true;
        } else {
            //
            return false;
        }
    }
	
	// check url
	public static boolean checkURL(CharSequence input) {
	   
		if(input== null){
			return false;
		}
		
	    Pattern URL_PATTERN = Patterns.WEB_URL;
	    boolean isURL = URL_PATTERN.matcher(input).matches();
	    if (!isURL) {
	        String urlString = input + "";
	        if (URLUtil.isNetworkUrl(urlString)) {
	            try {
	                new URL(urlString);
	                isURL = true;
	            } catch (Exception e) {
	            }
	        }
	    }
	    return isURL;
	}

	// doc txt
	public static String readTxt(Context context, String resource) {
		String str = "";
		StringBuffer buf = new StringBuffer();

		
		try {
			InputStream is = context.getResources().getAssets().open(resource);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			if (is != null) {

				while ((str = reader.readLine()) != null) {
					buf.append(str + "\n");
				}

			}
			is.close();
			return buf.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// // doi thoi gian tu mili toi h:p:s
	public static String milliSecondsToTimer(long milliseconds) {
		String finalTimerString = "";
		String secondsString = "";

		int hours = (int) (milliseconds / (1000 * 60 * 60));
		int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
		int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
		// Thêm giờ nếu có
		if (hours > 0) {
			finalTimerString = hours + ":";
		}

		// Thêm 0 nếu như giây có thêm một chữ số
		if (seconds < 10) {
			secondsString = "0" + seconds;
		} else {
			secondsString = "" + seconds;
		}

		finalTimerString = finalTimerString + minutes + ":" + secondsString;

		return finalTimerString;
	}
}
