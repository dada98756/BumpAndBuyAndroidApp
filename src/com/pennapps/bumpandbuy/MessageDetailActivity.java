package com.pennapps.bumpandbuy;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MessageDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_detail);	
	}
	
	@Override
	public void onResume(){
		super.onResume();
		TextView title = new TextView(this);
		title = (TextView)findViewById(R.id.messageTitle);
		title.setText("This is a new message");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message_detail, menu);
		return true;
	}

}
