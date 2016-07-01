package com.mapway.database2java.model.schema;

import java.util.ArrayList;

public class Tables {
	ArrayList<Table> Tables;
	public Tables(){
		Tables=new ArrayList<Table>(); 
	}
	public int getCount()
	{
		return Tables.size();
	}
	public ITable getAt(int index){
		return Tables.get(index);
	}
	public void Clear(){
		Tables.clear();
	}
	public void addTable(Table table)
	{
		Tables.add(table);
	}
}
