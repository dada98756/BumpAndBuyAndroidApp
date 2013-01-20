package com.pennapps.bumpandbuy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class InboxActivity extends Activity {

	public static final int ItemSelected_ID = 2;
	
	//some tokens in order to get messages
	private String userID;
	private String userName;
	private ListView inboxListView;
	private ArrayList<HashMap<String,String>> dataOfMsgList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox);
		inboxListView = (ListView) findViewById(R.id.inboxListView);
		inboxListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				HashMap<String,String> msgSelected = dataOfMsgList.get(position);
				Intent myIntent = new Intent(InboxActivity.this,MessageDetailActivity.class);
				//myIntent.putExtra("item_id", msgSelected.get(MessageField.ITEM_ID));
				MessageDetailActivity.msg = msgSelected;
				startActivityForResult(myIntent, ItemSelected_ID);
			}
		});
	}
	
	@Override
	public void onResume(){
		super.onResume();
		dataOfMsgList = getFakedData();
		SimpleAdapter adapter = new SimpleAdapter(InboxActivity.this, dataOfMsgList,
				R.layout.msglist, new String[] { MessageField.MSG_TITLE, MessageField.MSG_BODY },
				new int[] { R.id.msg_title, R.id.msg_body });
		inboxListView.setAdapter(adapter);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch(requestCode) {
			case ItemSelected_ID:{
				break;
			}
		}
	}
	
	
	
	ArrayList<HashMap<String,String>> getFakedData(){
		ArrayList<HashMap<String,String>> dataOfMsgList = new ArrayList<HashMap<String,String>>();
		for (int i = 0;i < 10;i++){
			HashMap<String,String> newMsg = new HashMap<String,String>();
			newMsg.put(MessageField.MSG_TITLE, "msg_title"+i);
			newMsg.put(MessageField.MSG_BODY, "msg_body"+i);
			newMsg.put(MessageField.ITEM_ID, "item_id"+i);
			newMsg.put(MessageField.MSG_ID,"msg_id"+i);
			newMsg.put(MessageField.SELLER_ID, "gsmith104@gmail.com");
			newMsg.put(MessageField.BUYER_ID,"taomo117@gmail.com");
			newMsg.put(MessageField.PRICE, "1.00");
			dataOfMsgList.add((HashMap<String,String>)newMsg.clone());
		}
		for (int i = 10;i<20;i++){
			HashMap<String,String> newMsg = new HashMap<String,String>();
			newMsg.put(MessageField.MSG_TITLE, "msg_title"+i);
			newMsg.put(MessageField.MSG_BODY, "msg_body"+i);
			newMsg.put(MessageField.ITEM_ID, "item_id"+i);
			newMsg.put(MessageField.MSG_ID,"msg_id"+i);
			newMsg.put(MessageField.SELLER_ID, "taomo117@gmail.com");
			newMsg.put(MessageField.BUYER_ID,"gsmith104@gmail.com");
			newMsg.put(MessageField.PRICE, "1.00");
			dataOfMsgList.add((HashMap<String,String>)newMsg.clone());
		}
		return dataOfMsgList;
	}
}

