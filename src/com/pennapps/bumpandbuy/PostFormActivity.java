package com.pennapps.bumpandbuy;

import io.filepicker.FilePicker;
import io.filepicker.FilePickerAPI;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.pennapps.bumpandbuy.ItemDetailActivity.ItemDetailTask;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PostFormActivity extends Activity {

	public static final int SellItButtonClick_ID = 1;
	public static final int WantItButtonClick_ID = 1;
	private static final String MY_API_KEY = "AUgWdxAXMQoSUahJ3y4D5z";
	private EditText descriptionEditText;
	private EditText attributeEditText;
	private EditText titleEditText;
	private EditText priceEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_form);
		descriptionEditText = (EditText) findViewById(R.id.description_edittext);
		attributeEditText = (EditText) findViewById(R.id.attribute_edittext);
		titleEditText = (EditText) findViewById(R.id.title_edittext);
		priceEditText = (EditText) findViewById(R.id.price_edittext);
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
	
	
	public void onSellButtonClick(View view){
		//TODO
		HashMap<String,String> newItem = new HashMap<String,String>();
		newItem.put(ItemField.SELLOR_BUY, "sell");
		newItem.put(ItemField.AUTHOR_EMAIL, SettingsActivity.userAccount.get(UserField.PENN_EMAIL));
		newItem.put(ItemField.PRICE, priceEditText.getText().toString());
		newItem.put(ItemField.POST_TITLE,titleEditText.getText().toString());
		newItem.put(ItemField.TEXT, descriptionEditText.getText().toString());
		newItem.put(ItemField.ATTRIBUTES, attributeEditText.getText().toString());
	
		try {
			Network.executeRequest(this, new PostFormTask(), Server.Method.POST, newItem);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class PostFormTask extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
			
			try {
				Server server =new Server();
				JSONConverter.JSONObjectToMap(server.post("/post", (Map<String,String>) params[0]));
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
			Toast.makeText(getApplicationContext(),
					"Posted",
					Toast.LENGTH_LONG).show();
			finish();
		}
		
	}
	
	public void onWantButtonClick(View view){
		//TODO
		Toast.makeText(getApplicationContext(),
				"Wanted",
				Toast.LENGTH_LONG).show();
		
	}
	
	public void onLoadImageButtonClick(View view){
		getPhotoFromFilePickerIO();
	}
	
	public void onBackButtonClick(View view){
		finish();
	}
	
	public void getPhotoFromFilePickerIO(){
		FilePickerAPI.setKey(MY_API_KEY);
		Intent intent = new Intent(this, FilePicker.class);
		startActivityForResult(intent, FilePickerAPI.REQUEST_CODE_GETFILE);
	}
	
	@Override
	protected void onActivityResult(int requestCode,
	                                int resultCode, Intent data) {
	    if (requestCode == FilePickerAPI.REQUEST_CODE_GETFILE) {
	        if (resultCode != RESULT_OK)
			//Result was cancelled by the user or there was an error
	                return;
	        Uri uri = data.getData();
	        System.out.println("File path is " + uri.toString());
	        System.out.println("FPUrl: " + data.getExtras().getString("fpurl"));
	        descriptionEditText.setText("File path is " + uri.toString());
	        attributeEditText.setText("FPUrl: " + data.getExtras().getString("fpurl"));
	        Toast.makeText(getApplicationContext(),
	        		"File path is " + uri.toString()+";"+"FPUrl: " + data.getExtras().getString("fpurl"),
					Toast.LENGTH_LONG).show();
	    }
	}
}
