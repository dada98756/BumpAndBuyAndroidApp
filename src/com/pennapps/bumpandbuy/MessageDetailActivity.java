package com.pennapps.bumpandbuy;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MessageDetailActivity extends Activity {

	public static HashMap<String,String> msg;
	public static final int BumpBottonClick_ID = 1;
	
	private TextView titleTextView;
	private TextView detailsTextView;
	private TextView itemIdTextView;
	private TextView sellerTextView;
	private TextView buyerTextView;
	private TextView itemPriceTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_detail);	
		titleTextView = (TextView) findViewById(R.id.messageTitle);
		detailsTextView =(TextView) findViewById(R.id.messageDetail);
		itemIdTextView = (TextView) findViewById(R.id.itemIdTextView);
		sellerTextView = (TextView)findViewById(R.id.sellerTextView);
		buyerTextView = (TextView)findViewById(R.id.buyerTextView);
		itemPriceTextView = (TextView)findViewById(R.id.priceIdTextView);
	}

	@Override
	public void onResume(){
		super.onResume();
		if (msg==null){
			finish();
		}
		titleTextView.setText(msg.get(MessageField.MSG_TITLE));
		detailsTextView.setText(msg.get(MessageField.MSG_BODY));
		itemIdTextView.setText(msg.get(MessageField.ITEM_ID));
		sellerTextView.setText(msg.get(MessageField.SELL_PENN_MAIL));
		buyerTextView.setText(msg.get(MessageField.BUYER_PENN_MAIL));
		itemPriceTextView.setText(msg.get(MessageField.ITEM_PRICE));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message_detail, menu);
		return true;
	}

	public void onDestroy() {
		msg = null;
		super.onDestroy();
	}

	public void onBumpBottonClick(View view){
		Intent myIntent = new Intent(this,BumpActivity.class);
		BumpActivity.msg = msg;
		startActivityForResult(myIntent, BumpBottonClick_ID);
	}

}
