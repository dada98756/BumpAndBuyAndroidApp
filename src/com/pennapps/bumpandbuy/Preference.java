package com.pennapps.bumpandbuy;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
	private static SharedPreferences sharedpref;
	private static Preference myPref;
	
	public Preference getInstance(Context context){
		if(myPref==null){
			myPref=new Preference(context);
		}
		return myPref;
	}
	private Preference(Context context){
		String name=context.getResources().getString(R.string.pref_name);
		String mode=context.getResources().getString(R.string.pref_mode);
		sharedpref=context.getSharedPreferences(name, Integer.valueOf(mode));
	}
}
