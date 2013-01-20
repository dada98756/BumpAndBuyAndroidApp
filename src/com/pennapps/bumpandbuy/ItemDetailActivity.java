package com.pennapps.bumpandbuy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ItemDetailActivity extends Activity {
	public static HashMap<String, String> details;
	public static HashMap<String,String> cache;
	TextView title, seller, price, description;
	
	static{
		cache = new HashMap<String,String>();
		cache.put("yuhanhao@seas.upenn.edu", "yuhanhao@gmail.com");
		cache.put("nishiwei@seas.upenn.edu","250806040@qq.com");
		cache.put("gresmith@seas.upenn.edu","gsmith104@gmail.com");
		cache.put("motao@seas.upenn.edu","taomo117@gmail.com");
	}
	
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
		title.setText(details.get(ItemField.POST_TITLE));
		
		description = (TextView)findViewById(R.id.description_textview);
		description.setText(details.get(ItemField.TEXT));
		
		price = (TextView)findViewById(R.id.price_textview);
		price.setText(details.get(ItemField.PRICE));
		
		seller = (TextView)findViewById(R.id.seller_textview);
		seller.setText(details.get(ItemField.AUTHOR_EMAIL));
	
	}

	public  void onBuyButtonClick(View view){
		//TODO
		HashMap<String,String> newMsg = new HashMap<String,String>();
		newMsg.put(MessageField.SELL_PENN_MAIL, details.get(ItemField.AUTHOR_EMAIL));
		newMsg.put(MessageField.SELL_VENMO_MAIL, cache.get(details.get(ItemField.AUTHOR_EMAIL)));
		newMsg.put(MessageField.BUYER_PENN_MAIL,SettingsActivity.userAccount.get(UserField.PENN_EMAIL));
		newMsg.put(MessageField.MSG_TITLE,"Transaction Request");
		newMsg.put(MessageField.MSG_BODY, "I want to buy"+details.get(ItemField.POST_TITLE));
		newMsg.put(MessageField.ITEM_PRICE,details.get(ItemField.PRICE));
		newMsg.put(MessageField.ITEM_ID, details.get(ItemField.ITEM_ID));
		
		try {
			Network.executeRequest(this, new ItemDetailTask(), Server.Method.POST, newMsg);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void onBackButtonClick(View view){
		finish();
	}
	
	public class ItemDetailTask extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
			
			try {
				Server server =new Server();
				JSONConverter.JSONObjectToMap(server.post("/sendMSG", (Map<String,String>) params[0]));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		protected void onPostExecute(Object result){
			Intent myIntent = new Intent(ItemDetailActivity.this,InboxActivity.class);
			startActivityForResult(myIntent, 0);
		}
	} 
}
