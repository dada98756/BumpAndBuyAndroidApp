package com.pennapps.bumpandbuy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends Activity {
	
	private EditText user;
	private EditText pwd;
	private Context myContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		user=(EditText) findViewById(R.id.editText1);
		pwd= (EditText) findViewById(R.id.editText2);
		myContext=this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
	
	public void onSubmitClicked(View view){
		
		try {
			String email=user.getText().toString().trim();
			String password=pwd.getText().toString();
			String fakeToken="123";
			Map<String, String> map=new HashMap<String, String>();
			map.put("email", email);
			map.put("pwd", password);
			map.put("access_token",fakeToken);
			Network.executeRequest(this, new LoginTask(), Server.Method.POST, map);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class LoginTask extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
			try {
				Server server = new Server();
				JSONObject obj = server.post("/user", (Map)params[0]);
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
		
		protected void onPostExecution(Object result){
			Map<String, String> map = (Map<String, String>) result;
			MyPreference.getInstance(myContext).put("user", map.get("email"));
			Intent myIntent = new Intent(myContext,FeedListActivity.class);
			startActivityForResult(myIntent, 0);
			
		}
		
	}

}
