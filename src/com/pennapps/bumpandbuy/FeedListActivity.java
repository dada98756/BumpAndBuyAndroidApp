package com.pennapps.bumpandbuy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class FeedListActivity extends Activity{

	public static final int PostButtonClick_ID = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_list);
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

	public void onPostButtonClick(View view){
		Intent myIntent = new Intent(this,PostFormActivity.class);
		startActivityForResult(myIntent, PostButtonClick_ID);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);

		switch(requestCode){
			case PostButtonClick_ID:
				break;
		}
	}
}
