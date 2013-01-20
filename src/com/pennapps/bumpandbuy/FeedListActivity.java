package com.pennapps.bumpandbuy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FeedListActivity extends Activity{

	public static final int PostButtonClick_ID = 1;
	public static final int InboxButtonClick_ID = 2;
	private TextView tvPath;
	private ListView itemListView;
	private Button selectButton;
	private boolean sdCardStatue;
	List<HashMap<String, String>> itemMap; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_list);
		itemListView = (ListView) findViewById(R.id.itemListView);
		
		itemListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

				Intent myIntent = new Intent(FeedListActivity.this, ItemDetailActivity.class);
				ItemDetailActivity.details = itemMap.get(position);
				startActivityForResult(myIntent, InboxButtonClick_ID);

			}
		});
	}

	public class ListPopulateTask extends AsyncTask {
		// public LoginTask(){}
		@Override
		protected JSONArray doInBackground(Object... arg0) {
			// TODO Auto-generated method stub

			try {
				Server server=new Server();
				JSONArray finalResult = null;
				Map<String, String> map = (Map<String, String>) arg0[0];
				finalResult = server.get2("/post", map);
				System.err.println("here comes json array");
				return finalResult;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Object result) {
			if (result == null){
				return;

			}
			try {
				itemMap = JSONConverter.JSONArrayToListofMap((JSONArray)result);
				inflateListView(itemMap);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		try {
			Network.executeRequest(this, new ListPopulateTask(), Server.Method.GET, new HashMap<String,String>());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<HashMap<String,String>> fakeMap(){
		List<HashMap<String,String>> falsity = new ArrayList<HashMap<String,String>>();
		String title1 = "Great Expectations";
		String desc1 = "It wasn't what I hoped for";
		String price1 = "$500";
		String seller1 = "Yuhan Hao";
		String title2 = "How to Login To Facebook";
		String desc2 = "Yeah that would have been useful earlier";
		String price2 = "$10";
		String seller2 = "Mark Zuckerberg";
		String title3 = "Star Wars VII Leaked Screenplay";
		String desc3 = "Ewoks gone wild.  This film is a boisterous romp through places you thought were sacred to your childhood.";
		String price3 = "$1000";
		String seller3 = "Tao Mo";
		String titleKey = "title";
		String descKey = "description";
		String priceKey = "price";
		String sellerKey = "seller";

		String[] titleArray = {title1, title2, title3};
		String[] descArray = {desc1, desc2, desc3};
		String[] priceArray = {price1, price2, price3};
		String[] sellArray = {seller1, seller2, seller3};

		for(int i = 0; i<3; i++){
			HashMap<String,String> tempMap = new HashMap<String,String>();
			tempMap.put(titleKey, titleArray[i]);
			tempMap.put(descKey, descArray[i]);
			tempMap.put(priceKey, priceArray[i]);
			tempMap.put(sellerKey, sellArray[i]);
			falsity.add(tempMap);
		}
		return falsity;
	}


	/*
	 * when click on files
	 */


	/*
	 * update the file list
	 */
	private void inflateListView(List<HashMap<String,String>> fakeMap) {
		/*
		 * attach the updater to the list view
		 */
		SimpleAdapter adapter = new SimpleAdapter(FeedListActivity.this, fakeMap,
				R.layout.itemlist, new String[] { ItemField.POST_TITLE, ItemField.TEXT, ItemField.PRICE },
				new int[] { R.id.item_name, R.id.item_description, R.id.item_price });
		itemListView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Quit");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 0:
			finish();
			break;
		}
		return true;
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
