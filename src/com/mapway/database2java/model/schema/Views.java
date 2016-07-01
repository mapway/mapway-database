package com.mapway.database2java.model.schema;

import java.util.ArrayList;

public class Views {
	ArrayList<View> Views;
	public Views(){
		Views=new ArrayList<View>(); 
	}
	public int getCount()
	{
		return Views.size();
	}
	public View getAt(int index){
		return Views.get(index);
	}
	public void Clear(){
		Views.clear();
	}
	public void addView(View view)
	{
		Views.add(view);
	}
	
}
