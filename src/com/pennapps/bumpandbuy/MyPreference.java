package com.pennapps.bumpandbuy;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyPreference {
	private static SharedPreferences sharedpref;
	private static MyPreference myPref;
	
	public static MyPreference getInstance(Context context){
		if(myPref==null){
			myPref=new MyPreference(context);
		}
		return myPref;
	}
	private MyPreference(Context context){
		String name=context.getResources().getString(R.string.pref_name);
		String mode=context.getResources().getString(R.string.pref_mode);
		sharedpref=context.getSharedPreferences(name, Integer.valueOf(mode));
	}
	
	public int put(String key,String value){
		int code;
		if(sharedpref.contains(key)){
			code=1;
		}else{
			code=0;
		}
		Editor editor=sharedpref.edit();
		editor.putString(key, value);
		if(editor.commit())
			return code;
		else
			return -1;
	}
	
	public boolean put(Map<String,String> map){
		Editor editor=sharedpref.edit();
		Iterator<Entry<String, String>> iterator=map.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, String> entry=iterator.next();
			editor.putString(entry.getKey(), entry.getValue());
		}
		return editor.commit();
	}
	
	public String get(String key){
		return sharedpref.getString(key, "");
	}
}
