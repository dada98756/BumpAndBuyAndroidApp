package com.pennapps.bumpandbuy;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pennapps.bumpandbuy.LoginUsingLoginFragmentActivity.VenmoTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FeedListActivity extends Activity{

	public static final int PostButtonClick_ID = 1;
	public static final int InboxButtonClick_ID = 2;
	private TextView tvPath;
	private ListView itemListView;
	private Button selectButton;
	private boolean sdCardStatue;
	List<HashMap<String,String>> fakeMap;
	List<HashMap<String, HashSet<String>>> itemMap = new ArrayList<HashMap<String, HashSet<String>>>(); 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_list);
//		try {
//			Network.executeRequest(this, new ListPopulateTask(), Server.Method.GET, null);
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
			try {
				if (result == null){
				return;	
					
				}
				JSONArray thisArray = (JSONArray)result;
				for(int i =0; i<thisArray.length(); i++){
					JSONObject obj = (JSONObject) thisArray.get(i);
					JSONArray names = obj.names();
					for(int j=0; j< names.length();j++)
					{
						String string = obj.getString(names.getString(j));
						System.out.println(names.getString(i) + ": " + string);
						
						
						
						
					}
				}
				
			} catch (Exception e) {

			}
			//System.err.println("YES!!!");
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		
//		try {
//			Network.executeRequest(this, new ListPopulateTask(), Server.Method.GET, null);
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		itemListView = (ListView) findViewById(R.id.itemListView);
		fakeMap = fakeMap();
		inflateListView(fakeMap);
		itemListView.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				
				Intent myIntent = new Intent(FeedListActivity.this, ItemDetailActivity.class);
				ItemDetailActivity.details = fakeMap.get(position);
				startActivityForResult(myIntent, InboxButtonClick_ID);
				
			}
		});
	}

		
	
	
	private List<HashMap<String,String>> fakeMap(){
		List<HashMap<String,String>> falsity = new ArrayList<HashMap<String,String>>();
		String title1 = "Great Expectations";
		String desc1 = "Long and boring";
		String price1 = "$500";
		String seller1 = "Yuhan Hao";
		String title2 = "How to Login To Facebook";
		String desc2 = "Yeah that would have been useful earlier";
		String price2 = "$10";
		String seller2 = "Mark Zuckerberg";
		String title3 = "Star Wars VII";
		String desc3 = "Ewoks gone wild.  This film is a wild romp through places you thought were sacred to your childhood.";
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
				List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

				

				/*
				 * attach the updater to the list view
				 */
				SimpleAdapter adapter = new SimpleAdapter(FeedListActivity.this, fakeMap,
						R.layout.itemlist, new String[] { "title", "description" },
						new int[] { R.id.item_name, R.id.item_description });
				itemListView.setAdapter(adapter);

			

			}

			/*
			 * set back to root
			 */
//			public void onRootBtnClick(View v) {
//				currentPath = root;
//				currentFileList = root.listFiles();
//				inflateListView(currentFileList);
//			}

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


			/*
			 * if file is vaild, getting data
			 */
//			public void onSelectBtnClick(View v) {
//				if (currentFile.isDirectory()) {
//					new AlertDialog.Builder(v.getContext())
//							.setMessage("This is a directory!").create().show();
//				} else if (currentFile != null) {
//					System.out.println("Try to get data");
//
//					System.out.println("Getting data");
//					Document.loadFile(currentFile);
//					Document.setDeFile(currentFile.getPath());
//					// Document.setDescriptor(getIntent().getData());
//					// EngineeringPrinter.Microsoft =
//					// MicrosoftSink.Filter(getIntent().getType());
//					// EngineeringPrinter.type = getIntent().getType();
//					/*
//					 * data is good, ready to be sent to remote printer
//					 */
//					Intent myIntent = new Intent(v.getContext(),
//							PrinterSelectScreen.class);
//					myIntent.putExtra("eniac", false);
//					myIntent.putExtra("filePath", currentFile.toString());
//					myIntent.putExtra("isPdf",
//							currentFile.toString().trim().endsWith(".pdf"));
//					startActivityForResult(myIntent, 0);
//				}
//			}

			public void onQuitBtnClick(View v) {
				finish();
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
