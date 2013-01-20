package com.pennapps.bumpandbuy;

import static com.pennapps.bumpandbuy.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.pennapps.bumpandbuy.CommonUtilities.EXTRA_MESSAGE;
import static com.pennapps.bumpandbuy.CommonUtilities.SENDER_ID;
import static com.pennapps.bumpandbuy.CommonUtilities.SERVER_URL;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
	private Context myContext;

	AsyncTask<Void, Void, Void> mRegisterTask;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_fragment_activity);

		FragmentManager fragmentManager = getSupportFragmentManager();
		userSettingsFragment = (UserSettingsFragment) fragmentManager
				.findFragmentById(R.id.login_fragment);
		userSettingsFragment
				.setSessionStatusCallback(new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						Log.d("LoginUsingLoginFragmentActivity",
								String.format("New session state: %s",
										state.toString()));
						if (state.isOpened()) {
							makeMeRequest(session);

						}
						if (state.isClosed()) {
							System.err.println("state is closed");
							session.closeAndClearTokenInformation();
							// userSettingsFragment.
						}
						if (exception != null)
							Log.d("LoginUsingLoginFragmentActivity",
									String.format("Exception: %s",
											exception.getMessage()));
					}
				});
		// userSettingsFragment.
		myContext = this;

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
								// view that in turn displays the profile
								// picture.
								String regID = getGCM();
								Map<String, String> map = new HashMap<String, String>();
								map.put("userID", user.getId());
								map.put("username", user.getName());
								map.put("access_token",
										session.getAccessToken());
								map.put("gcm_token", regID);

								System.err.println("The write to Preference is "+Preference.getInstance(myContext).put(map));
								
								// Intent intent=new Intent();

								try {
									Network.executeRequest(myContext,
											new LoginTask(),
											Server.Method.POST, map);
								} catch (InstantiationException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						if (response.getError() != null) {
							// Handle errors, will do so later.
						}
					}
				});
		request.executeAsync();
	}

	private String getGCM() {
		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				// mDisplay.append(getString(R.string.already_registered) +
				// "\n");
			} else {

			}
		}
		return regId;
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
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
	
	public void nextActivity(boolean isVenmoSet){
		Intent myIntent;
		if(isVenmoSet){
			myIntent = new Intent(this,FeedListActivity.class);
		}else{
			myIntent = new Intent(this,SettingsActivity.class);
		}
		startActivityForResult(myIntent, 0);
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// mDisplay.append(newMessage + "\n");
		}
	};

	public class LoginTask extends AsyncTask {
		// public LoginTask(){}
		@Override
		protected JSONObject doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			
			try {
				Server server=new Server();
				JSONObject finalResult = null;
				Map<String, String> map = (Map<String, String>) arg0[0];
				finalResult = server.post("/account", map);
				System.err.println("here comes json array");
				return finalResult;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Object result) {
			try {
				boolean flag=true;
				System.err.println("the flag is "+flag);
				if (result != null) {
					System.err.println("the flag is "+flag);
					JSONArray names = ((JSONObject)result).names();
					for (int i = 0; i < names.length(); i++) {
						if (names.getString(i).compareTo("errorMsg") == 0)
						{
							flag=false;
							break;
						}
					}
					if(flag){
						try {
							Network.executeRequest(myContext,
									new VenmoTask(),
									Server.Method.POST, Preference.getInstance(myContext).get("userID"));
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
						
				} else {
					System.err.println("result is null");
				}
			} catch (Exception e) {

			}
			//System.err.println("YES!!!");
		}
	}

	public class VenmoTask extends AsyncTask {
		// public LoginTask(){}
		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			System.err.println((String) arg0[0]);
			try {
				Server server=new Server();
				String userID = (String) arg0[0];
				Map<String, String> map =new HashMap<String, String>();
				map.put("userID", (String) arg0[0]);
				JSONObject finalResult = server.get("/venmo", map);
				System.err.println("here after get");
				return finalResult;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(Object result) {
			
			try {
				JSONObject finalResult= (JSONObject) result;
				if(finalResult.getString(finalResult.names().getString(0)).compareTo("false")==0)
					nextActivity(false);
				else
					nextActivity(true);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.err.println("YES!!!");
		}
	}

}
