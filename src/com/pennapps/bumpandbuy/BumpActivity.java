package com.pennapps.bumpandbuy;

import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.bump.api.BumpAPIIntents;
import com.bump.api.IBumpAPI;
import com.pennapps.bumpandbuy.VenmoLibrary.VenmoResponse;

public class BumpActivity extends Activity {
	
	public static HashMap<String,String> msg;
	private IBumpAPI api;
	private TextView logTextView;
	private String bumpUser;

	private final String venmo_app_id = "1222";
	private final String venmo_app_name = "BumpAndBuy";
	private String recipient;
	private String amount;
	private String txn;
	private String note;
	public  static final int VenmoActivityResult = 2;
	private final String venmo_app_secret = "K7qUAMtRe59jCrTgrmTfLrfXxt43nj7q";
	private final String noteOfBump = "Transaction made possible through Bump!";
	private final String pay = "pay";


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
					String yourItemId = new String(intent.getByteArrayExtra("data"));
					//TODO here we should see if itemid consistent
					boolean isConsistent = yourItemId.equals(msg.get(MessageField.ITEM_ID))?true:false;
					if (isConsistent){	//itemids consistent
						//TODO venom logic, let buyer initialize payment
						String userEmail = SettingsActivity.userAccount.get(UserField.PENN_EMAIL);
						String buyerPennEmail = msg.get(MessageField.BUYER_PENN_MAIL);
						boolean isBuyer = buyerPennEmail.equals(userEmail);
						if (isBuyer){//buyer
							Toast.makeText(getApplicationContext(),
									"Bumped successfully!",
									Toast.LENGTH_LONG).show();
							//hard coded
							recipient = msg.get(MessageField.SELL_VENMO_MAIL);
							amount = msg.get(MessageField.ITEM_PRICE);
							note = noteOfBump;
							txn = pay;

							try {
								Intent venmoIntent = VenmoLibrary.openVenmoPayment(venmo_app_id, venmo_app_name, recipient, amount, note, txn);
								startActivityForResult(venmoIntent, VenmoActivityResult); //1 is the requestCode we are using for Venmo. Feel free to change this to another number. 
							}
							catch (android.content.ActivityNotFoundException e) //Venmo native app not install on device, so let's instead open a mobile web version of Venmo in a WebView
							{
								Intent venmoIntent = new Intent(BumpActivity.this, VenmoWebViewActivity.class);
								String venmo_uri = VenmoLibrary.openVenmoPaymentInWebView(venmo_app_id, venmo_app_name, recipient, amount, note, txn);
								venmoIntent.putExtra("url", venmo_uri);
								startActivityForResult(venmoIntent, VenmoActivityResult);
							}catch(Exception e){
								Intent venmoIntent = new Intent(BumpActivity.this, VenmoWebViewActivity.class);
								String venmo_uri = VenmoLibrary.openVenmoPaymentInWebView(venmo_app_id, venmo_app_name, recipient, amount, note, txn);
								venmoIntent.putExtra("url", venmo_uri);
								startActivityForResult(venmoIntent, VenmoActivityResult);
							}
						}else{//seller
							Toast.makeText(getApplicationContext(),
									"Bumped successfully!",
									Toast.LENGTH_LONG).show();
							finish();
						}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch(requestCode) {
		case VenmoActivityResult: { //1 is the requestCode we picked for Venmo earlier when we called startActivityForResult
			if(resultCode == RESULT_OK) {
				String signedrequest = data.getStringExtra("signedrequest");
				if(signedrequest != null) {
					VenmoResponse response = (new VenmoLibrary()).validateVenmoPaymentResponse(signedrequest, venmo_app_secret);
					if(response.getSuccess().equals("1")) {
						//Payment successful.  Use data from response object to display a success message
						String note = response.getNote();
						String amount = response.getAmount();
						Toast.makeText(getApplicationContext(),
								"note:"+note+";amount:"+amount,
								Toast.LENGTH_LONG).show();
					}
				}
				else {
					String error_message = data.getStringExtra("error_message");
					//An error ocurred.  Make sure to display the error_message to the user
					Toast.makeText(getApplicationContext(),
							error_message,
							Toast.LENGTH_LONG).show();
				}                               
			}
			else if(resultCode == RESULT_CANCELED) {
				//The user cancelled the payment
			}
			break;

		}
		}
		finish();
	}

}
