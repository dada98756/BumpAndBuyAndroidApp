package com.pennapps.bumpandbuy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class PostFormActivity extends Activity {

	public static final int SellItButtonClick_ID = 1;
	public static final int WantItButtonClick_ID = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_form);
	}
	
	@Override
	public void onResume(){
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_form, menu);
		return true;
	}
	
	
	public void onSellButtonClick(View view){
		//TODO
		Toast.makeText(getApplicationContext(),
				"Post button clicked!",
				Toast.LENGTH_LONG).show();
	}
	
	public void onWantButtonClick(View view){
		//TODO
		Toast.makeText(getApplicationContext(),
				"Want button clicked!",
				Toast.LENGTH_LONG).show();
	}
	

}
