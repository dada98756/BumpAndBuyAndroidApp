package com.pennapps.bumpandbuy;

import com.bump.api.BumpAPIIntents;
import com.bump.api.IBumpAPI;
import com.pennapps.bumpandbuy.VenmoLibrary.VenmoResponse;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class BumpActivity extends Activity {

	private IBumpAPI api;
	private TextView logTextView;
	private String bumpUser;


	private final ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			Log.i("BumpTest", "onServiceConnected");
			logTextView.append("onServiceConnected\n");
			api = IBumpAPI.Stub.asInterface(binder);
			try {
				api.configure("a4b0169618ac415d95b7b8b59afc11af",
						bumpUser);
			} catch (RemoteException e) {
				Log.w("BumpTest", e);
			}
			Log.d("Bump Test", "Service connected");
			logTextView.append("Service connected\n");
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			Log.d("Bump Test", "Service disconnected");
			logTextView.append("Service disconnected\n");
		}
	};

	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			try {
				if (action.equals(BumpAPIIntents.DATA_RECEIVED)) {
					logTextView.append("Received data from: " + api.userIDForChannelID(intent.getLongExtra("channelID", 0))+"\n");
					logTextView.append("Data: " + new String(intent.getByteArrayExtra("data"))+"\n");
					//TODO here we should see if itemid consistent
					boolean isConsistent = true;
					if (isConsistent){	//itemids consistent
						finish();
					}else{	//itemids not consistent
						Toast.makeText(getApplicationContext(),
								"Yah Man, you got the wrong person!",
								Toast.LENGTH_LONG).show();
					}
				} else if (action.equals(BumpAPIIntents.MATCHED)) {
					long channelID = intent.getLongExtra("proposedChannelID", 0); 
					logTextView.append("Matched with: " + api.userIDForChannelID(channelID)+"\n");
					api.confirm(channelID, true);
					logTextView.append("Confirm sent\n");
				} else if (action.equals(BumpAPIIntents.CHANNEL_CONFIRMED)) {
					long channelID = intent.getLongExtra("channelID", 0);
					logTextView.append("Channel confirmed with " + api.userIDForChannelID(channelID)+"\n");
					//TODO here we should send itemid
					api.send(channelID, "Hello, world!".getBytes());
				} else if (action.equals(BumpAPIIntents.NOT_MATCHED)) {
					logTextView.append("Not matched.\n");
					Toast.makeText(getApplicationContext(),
							"Not Matched! Bump Harder!",
							Toast.LENGTH_LONG).show();
				} else if (action.equals(BumpAPIIntents.CONNECTED)) {

					logTextView.append("Connected to Bump...\n");
					api.enableBumping();
				}
			} catch (RemoteException e) {}
		} 
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bump);

		bindService(new Intent(IBumpAPI.class.getName()),
				connection, Context.BIND_AUTO_CREATE);
		Log.i("BumpTest", "boot");

		IntentFilter filter = new IntentFilter();
		filter.addAction(BumpAPIIntents.CHANNEL_CONFIRMED);
		filter.addAction(BumpAPIIntents.DATA_RECEIVED);
		filter.addAction(BumpAPIIntents.NOT_MATCHED);
		filter.addAction(BumpAPIIntents.MATCHED);
		filter.addAction(BumpAPIIntents.CONNECTED);
		registerReceiver(receiver, filter);
		logTextView = (TextView)findViewById(R.id.logTextView);
		//logTextView.setVisibility(TextView.INVISIBLE);

	}

	public void onStart() {
		Log.i("BumpTest", "onStart");
		logTextView.append("onStart\n");
		super.onStart();
	}

	public void onRestart() {
		Log.i("BumpTest", "onRestart");
		logTextView.append("onRestart\n");
		super.onRestart();
	}

	public void onResume() {
		Log.i("BumpTest", "onResume");
		logTextView.append("onResume\n");
		bumpUser = "bump"+System.currentTimeMillis();
		super.onResume();
	}

	public void onPause() {
		Log.i("BumpTest", "onPause");
		logTextView.append("onPause\n");
		super.onPause();
	}

	public void onStop() {
		Log.i("BumpTest", "onStop");
		logTextView.append("onStop\n");
		super.onStop();
	}

	public void onDestroy() {
		Log.i("BumpTest", "onDestroy");
		logTextView.append("onDestroy\n");
		unbindService(connection);
		unregisterReceiver(receiver);
		Toast.makeText(getApplicationContext(),
				"BumpTest Destroying!",
				Toast.LENGTH_LONG).show();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bump, menu);
		return true;
	}

}
