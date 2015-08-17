package com.hdv.effortlessenglish.broadcast;

import com.hdv.effortlessenglish.activity.R;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class NotificationBroadcast extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "da tat notifi", Toast.LENGTH_LONG).show();
	}

}
