package com.pennapps.bumpandbuy;

import com.pennapps.bumpandbuy.VenmoLibrary.VenmoResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class InboxActivity extends Activity {

	public static final int TryBumpBottonClick_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inbox, menu);
		return true;
	}

	public void onBackButtonClick(View view){
		finish();
	}

	public void onTryBumpBottonClick(View view){
		Intent myIntent = new Intent(this,BumpActivity.class);
		startActivityForResult(myIntent, TryBumpBottonClick_ID);
	}



	private final String venmo_app_id = "1222";
	private final String venmo_app_name = "BumpAndBuy";
	private String recipient;
	private String amount;
	private String txn;
	private String note;
	public  static final int VenmoActivityResult = 2;
	private final String venmo_app_secret = "K7qUAMtRe59jCrTgrmTfLrfXxt43nj7q";
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch(requestCode) {
		case TryBumpBottonClick_ID:{
			//TODO venom logic, let buyer initialize payment
			boolean isBuyer = true;
			if (isBuyer){//buyer
				Toast.makeText(getApplicationContext(),
						"Bumped successfully!",
						Toast.LENGTH_LONG).show();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				new Thread(){
					@Override
					public void run(){
						//hard coded
						recipient = "gsmith104@gmail.com";
						amount = "1.00";
						note = "Transaction made possible through Bump!";
						txn = "pay";

						try {
							Intent venmoIntent = VenmoLibrary.openVenmoPayment(venmo_app_id, venmo_app_name, recipient, amount, note, txn);
							startActivityForResult(venmoIntent, VenmoActivityResult); //1 is the requestCode we are using for Venmo. Feel free to change this to another number. 
						}
						catch (android.content.ActivityNotFoundException e) //Venmo native app not install on device, so let's instead open a mobile web version of Venmo in a WebView
						{
							Toast.makeText(getApplicationContext(),
									"local activity not available!",
									Toast.LENGTH_LONG).show();
							finish();//go back
							Intent venmoIntent = new Intent(InboxActivity.this, VenmoWebViewActivity.class);
							String venmo_uri = VenmoLibrary.openVenmoPaymentInWebView(venmo_app_id, venmo_app_name, recipient, amount, note, txn);
							venmoIntent.putExtra("url", venmo_uri);
							startActivityForResult(venmoIntent, VenmoActivityResult);
						}
					}
				}.start();
			}else{//seller
				Toast.makeText(getApplicationContext(),
						"Bumped successfully!",
						Toast.LENGTH_LONG).show();
			}
			break;
		}
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
	}
}

