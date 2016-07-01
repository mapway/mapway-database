package com.mapway.database2java.model.schema;

import java.util.ArrayList;

public class Packages {
	ArrayList<Pack> Pack;
	public Packages(){
		Pack=new ArrayList<Pack>(); 
	}
	public int getCount()
	{
		return Pack.size();
	}
	public Pack getAt(int index){
		return Pack.get(index);
	}
	public void Clear(){
		Pack.clear();
	}
	public void addPack(Pack pack)
	{
		Pack.add(pack);
	}
}
