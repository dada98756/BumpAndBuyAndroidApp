package com.pennapps.bumpandbuy;

import io.filepicker.FilePicker;
import io.filepicker.FilePickerAPI;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class PostFormActivity extends Activity {

	public static final int SellItButtonClick_ID = 1;
	public static final int WantItButtonClick_ID = 1;
	private static final String MY_API_KEY = "AUgWdxAXMQoSUahJ3y4D5z";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_form);
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
		Toast.makeText(getApplicationContext(),
				"Post button clicked!",
				Toast.LENGTH_LONG).show();
	}
	
	public void onWantButtonClick(View view){
		//TODO
		Toast.makeText(getApplicationContext(),
				"Want button clicked!",
				Toast.LENGTH_LONG).show();
		getPhotoFromFilePickerIO();
	}
	
	public void onBackButtonClick(){
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
	    }
	}
}
