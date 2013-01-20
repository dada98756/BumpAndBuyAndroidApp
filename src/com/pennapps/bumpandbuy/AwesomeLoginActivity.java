package com.pennapps.bumpandbuy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class AwesomeLoginActivity extends Activity {

	private static final int SignInButtonClick_ID = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_awesome_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_awesome_login, menu);
		return true;
	}

	public void onSignInButtonClick(View view){
		Intent myIntent = new Intent(this,FeedListActivity.class);
		startActivityForResult(myIntent, SignInButtonClick_ID);
	}
}
