package com.pennapps.bumpandbuy;

import static com.pennapps.bumpandbuy.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.pennapps.bumpandbuy.CommonUtilities.EXTRA_MESSAGE;
import static com.pennapps.bumpandbuy.CommonUtilities.SENDER_ID;
import static com.pennapps.bumpandbuy.CommonUtilities.SERVER_URL;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.UserSettingsFragment;
import com.google.android.gcm.GCMRegistrar;

public class LoginUsingLoginFragmentActivity extends FragmentActivity {
    private UserSettingsFragment userSettingsFragment;
    
    AsyncTask<Void, Void, Void> mRegisterTask;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_fragment_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        userSettingsFragment = (UserSettingsFragment) fragmentManager.findFragmentById(R.id.login_fragment);
        userSettingsFragment.setSessionStatusCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                Log.d("LoginUsingLoginFragmentActivity", String.format("New session state: %s", state.toString()));
                if(state.isOpened())
                {
                	makeMeRequest(session);
                }
                if(exception!=null)
                	Log.d("LoginUsingLoginFragmentActivity",String.format("Exception: %s", exception.getMessage()));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        userSettingsFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void makeMeRequest(final Session session) {
        // Make an API call to get user data and define a 
        // new callback to handle the response.
    	Request request = Request.newMeRequest(session, 
                new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                // If the response is successful
                if (session == Session.getActiveSession()) {
                    if (user != null) {
                        // Set the id for the ProfilePictureView
                        // view that in turn displays the profile picture.
                    	String userID,userName,accessToekn,regID;
                    	userID=user.getId();
                    	userName=user.getName();
                    	accessToekn=session.getAccessToken();
                    	regID=getGCM();
                    	
                    	Log.d("ID & Name %s", userID+";"+userName);
                    	Log.d("The token is %s",accessToekn);
                    	Log.d("regID is %s",regID);
                    	//Intent intent=new Intent();
                    }
                }
                if (response.getError() != null) {
                    // Handle errors, will do so later.
                }
            }
        });
        request.executeAsync();
    }
    
    private String getGCM(){
    	checkNotNull(SERVER_URL, "SERVER_URL");
        checkNotNull(SENDER_ID, "SENDER_ID");
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
        //mDisplay = (TextView) findViewById(R.id.display);
        registerReceiver(mHandleMessageReceiver,
                new IntentFilter(DISPLAY_MESSAGE_ACTION));
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            // Automatically registers application on startup.
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            // Device is already registered on GCM, check server.
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
                //mDisplay.append(getString(R.string.already_registered) + "\n");
            } else {
                  
            }
        }
		return regId;
    }
    private void checkNotNull(Object reference, String name) {
        if (reference == null) {
            throw new NullPointerException(
                    getString(R.string.error_config, name));
        }
    }
    
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        unregisterReceiver(mHandleMessageReceiver);
        GCMRegistrar.onDestroy(this);
        super.onDestroy();
    }
    
    private final BroadcastReceiver mHandleMessageReceiver =
            new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            //mDisplay.append(newMessage + "\n");
        }
    };

}

