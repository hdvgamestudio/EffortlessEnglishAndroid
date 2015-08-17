package com.hdv.effortlessenglish.model;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class DownloadTask extends AsyncTask<String, integer, String> {

	private Context context;
	private PowerManager.WakeLock mWakeLock;
	String fileName;

	public DownloadTask(Context context,String fileName ) {
		this.context = context;
		this.fileName= fileName;
	}

	@Override
	protected String doInBackground(String... sUrl) {
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		String result;
		String line = null;
		//read txt
		// @BadSkillz codes with same changes
//        try {
//            DefaultHttpClient httpClient = new DefaultHttpClient();
//            HttpGet httpGet = new HttpGet(sUrl[0]);
//            HttpResponse response = httpClient.execute(httpGet);
//            HttpEntity entity = response.getEntity();
//
//            BufferedHttpEntity buf = new BufferedHttpEntity(entity);
//
//            InputStream is = buf.getContent();
//
//            BufferedReader r = new BufferedReader(new InputStreamReader(is));
//
//            StringBuilder total = new StringBuilder();
//            
//            while ((line = r.readLine()) != null) {
//                total.append(line + "\n");
//            }
//            result = total.toString();
//            Log.d("Get URL", "Downloaded string: " + result);
//            return result;
//        } catch (Exception e) {
//            Log.e("Get Url", "Error in downloading: " + e.toString());
//        }
        
		
		// ghi file
		try {
			URL url = new URL(sUrl[0]);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			// expect HTTP 200 OK, so we don't mistakenly save error report
			// instead of the file
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return "Server returned HTTP " + connection.getResponseCode()
						+ " " + connection.getResponseMessage();
			}

			int fileLength = connection.getContentLength();

			// download the file
			input = connection.getInputStream();
			output = new FileOutputStream("/sdcard/"+fileName+".mp3");
			
			byte data[] = new byte[4096];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {
				// allow canceling with back button
				if (isCancelled()) {
					input.close();
					return null;
				}
				total += count;
				// publishing the progress....
				if (fileLength > 0) // only if total length is known
//					publishProgress((int) (total * 100 / fileLength));
				output.write(data, 0, count);
			}
		} catch (Exception e) {
			return e.toString();
		} finally {
			try {
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			} catch (IOException ignored) {
			}

			if (connection != null)
				connection.disconnect();
		}
		return null;
	}

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user 
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
             getClass().getName());
        mWakeLock.acquire();
//        mProgressDialog.show();
    }


    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
//        mProgressDialog.dismiss();
        if (result != null)
            Toast.makeText(context,"Download error: ", Toast.LENGTH_LONG).show();
        	
        else
            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
    }
}
