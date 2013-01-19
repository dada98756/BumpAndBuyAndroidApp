package com.pennapps.bumpandbuy;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.bump.api.BumpAPIIntents;
import com.bump.api.IBumpAPI;

public class BumpTest extends Activity
{
    private IBumpAPI api;
    private TextView logTextView;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.i("BumpTest", "onServiceConnected");
            logTextView.append("onServiceConnected\n");
            api = IBumpAPI.Stub.asInterface(binder);
            try {
                api.configure("a4b0169618ac415d95b7b8b59afc11af",
                              "Bump User");
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
                    Log.i("Bump Test", "Received data from: " + api.userIDForChannelID(intent.getLongExtra("channelID", 0))); 
                    Log.i("Bump Test", "Data: " + new String(intent.getByteArrayExtra("data")));
                    logTextView.append("Received data from: " + api.userIDForChannelID(intent.getLongExtra("channelID", 0))+"\n");
                    logTextView.append("Data: " + new String(intent.getByteArrayExtra("data"))+"\n");
                } else if (action.equals(BumpAPIIntents.MATCHED)) {
                    long channelID = intent.getLongExtra("proposedChannelID", 0); 
                    Log.i("Bump Test", "Matched with: " + api.userIDForChannelID(channelID));
                    logTextView.append("Matched with: " + api.userIDForChannelID(channelID)+"\n");
                    api.confirm(channelID, true);
                    Log.i("Bump Test", "Confirm sent");
                    logTextView.append("Confirm sent\n");
                } else if (action.equals(BumpAPIIntents.CHANNEL_CONFIRMED)) {
                    long channelID = intent.getLongExtra("channelID", 0);
                    Log.i("Bump Test", "Channel confirmed with " + api.userIDForChannelID(channelID));
                    logTextView.append("Channel confirmed with " + api.userIDForChannelID(channelID)+"\n");
                    api.send(channelID, "Hello, world!".getBytes());
                } else if (action.equals(BumpAPIIntents.NOT_MATCHED)) {
                    Log.i("Bump Test", "Not matched.");
                    logTextView.append("Not matched.\n");
                } else if (action.equals(BumpAPIIntents.CONNECTED)) {
                    Log.i("Bump Test", "Connected to Bump...");
                    logTextView.append("Connected to Bump...\n");
                    api.enableBumping();
                }
            } catch (RemoteException e) {}
        } 
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bumptest);

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
        super.onDestroy();
     }
}
