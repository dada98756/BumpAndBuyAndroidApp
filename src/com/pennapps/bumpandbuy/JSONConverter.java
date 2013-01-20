package com.pennapps.bumpandbuy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONConverter {
	List<HashMap<String, String>> list;
	HashMap<String, String> map;
	
	static public List<HashMap<String, String>> JSONArrayToListofMap(JSONArray array) throws JSONException{
		List<HashMap<String, String>> list= new ArrayList<HashMap<String,String>>();
		for(int i=0;i<array.length();i++){
			JSONObject obj=array.getJSONObject(i);
			JSONArray names=array.getJSONObject(i).names();
			HashMap<String, String> map =new HashMap<String, String> ();
			for(int j=0;j<names.length();j++){
				map.put(names.getString(j), obj.getString(names.getString(j)));
			}
			list.add(map);
		}
		return list;
		
	} 
	
	static public HashMap<String, String> JSONObjectToMap(JSONObject obj) throws JSONException{
		JSONArray names=obj.names();
		HashMap<String, String> map =new HashMap<String, String> ();
		for(int i=0;i<names.length();i++){
			map.put(names.getString(i), obj.getString(names.getString(i)));
		}
		return map;
	}
}
