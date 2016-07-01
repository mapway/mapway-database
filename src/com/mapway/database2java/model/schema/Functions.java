package com.mapway.database2java.model.schema;

import java.util.ArrayList;

public class Functions {
	ArrayList<Function> Functions;
	public Functions(){
		Functions=new ArrayList<Function>(); 
	}
	public int getCount()
	{
		return Functions.size();
	}
	public Function getAt(int index){
		return Functions.get(index);
	}
	public void Clear(){
		Functions.clear();
	}
	public void addFunction(Function func)
	{
		Functions.add(func);
	}
}
