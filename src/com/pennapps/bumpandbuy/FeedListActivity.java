package com.pennapps.bumpandbuy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class FeedListActivity extends Activity{

	public static final int PostButtonClick_ID = 1;
	public static final int InboxButtonClick_ID = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_list);
	}

	@Override
	public void onResume(){
		super.onResume();

	}

	public void onPostButtonClick(View view){
		Intent myIntent = new Intent(this,PostFormActivity.class);
		startActivityForResult(myIntent, PostButtonClick_ID);
	}
	
	public void onInboxButtonClick(View view){
		Intent myIntent = new Intent(this,InboxActivity.class);
		startActivityForResult(myIntent, InboxButtonClick_ID);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);

		switch(requestCode){
			case PostButtonClick_ID:
				break;
			case InboxButtonClick_ID:
				break;
		}
	}
}
