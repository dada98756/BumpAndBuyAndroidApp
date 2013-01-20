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



	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch(requestCode) {
			case TryBumpBottonClick_ID:{
			
			
				break;
			}
		}
	}
}

