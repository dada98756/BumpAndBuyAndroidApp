package com.pennapps.bumpandbuy;

import com.pennapps.bumpandbuy.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doGCMStuff();
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	Intent myIntent = new Intent(this, FeedListActivity.class);
        startActivityForResult(myIntent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void doGCMStuff(){
    	
    }
}
