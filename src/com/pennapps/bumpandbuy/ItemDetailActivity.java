package com.pennapps.bumpandbuy;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class ItemDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_detail);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_detail, menu);
		return true;
	}

	public  void onBuyButtonClick(View view){
		//TODO
		Toast.makeText(getApplicationContext(),
				"Buy button clicked!",
				Toast.LENGTH_LONG).show();

	}
	
	public void onBackButtonClick(){
		finish();
	}
}
