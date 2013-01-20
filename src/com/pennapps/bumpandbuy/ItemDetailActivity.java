package com.pennapps.bumpandbuy;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ItemDetailActivity extends Activity {
	public static HashMap<String, String> details;
	TextView title, seller, price, description;
	
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
	
	public void onResume(){
		super.onResume();
		title = (TextView)findViewById(R.id.title_textview);
		title.setText(details.get("title"));
		
		description = (TextView)findViewById(R.id.description_textview);
		description.setText(details.get("description"));
		
		price = (TextView)findViewById(R.id.price_textview);
		price.setText(details.get("price"));
		
		seller = (TextView)findViewById(R.id.seller_textview);
		seller.setText(details.get("seller"));
	
	}

	public  void onBuyButtonClick(View view){
		//TODO
		Toast.makeText(getApplicationContext(),
				"Buy button clicked!",
				Toast.LENGTH_LONG).show();

	}
	
	public void onBackButtonClick(View view){
		finish();
	}
}
