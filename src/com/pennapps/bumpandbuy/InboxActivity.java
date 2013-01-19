package com.pennapps.bumpandbuy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class InboxActivity extends Activity {

	public static int TryBumpTottonClick_ID = 1;
	
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
		Intent myIntent = new Intent(this,BumpTest.class);
		startActivityForResult(myIntent, TryBumpTottonClick_ID);
	}

}
