package com.mapway.database2java.model.schema;

import java.util.ArrayList;

public class Columns {
	ArrayList<Column> Columns;
	public Columns(){
		Columns=new ArrayList<Column>(); 
	}
	public int getCount()
	{
		return Columns.size();
	}
	public Column getAt(int index){
		return Columns.get(index);
	}
	public void Clear(){
		Columns.clear();
	}
	public void addColumn(Column column)
	{
		Columns.add(column);
	}
}
