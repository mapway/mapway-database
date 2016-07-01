package com.mapway.database2java.model.schema;

import java.util.ArrayList;

public class Procedures {
	ArrayList<Procedure> Procedures;
	public Procedures(){
		Procedures=new ArrayList<Procedure>(); 
	}
	public int getCount()
	{
		return Procedures.size();
	}
	public Procedure getAt(int index){
		return Procedures.get(index);
	}
	public void Clear(){
		Procedures.clear();
	}
	public void addProcedure(Procedure proc)
	{
		Procedures.add(proc);
	}
}
