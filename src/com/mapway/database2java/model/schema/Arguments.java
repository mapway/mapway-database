package com.mapway.database2java.model.schema;

import java.util.ArrayList;

public class Arguments {
	ArrayList<Argument> m_args;
	public Arguments()
	{
		m_args=new ArrayList<Argument>(); 
	}
	public void Clear()
	{
		m_args.clear();
	}
	public void AddArguemnt(Argument a)
	{
		m_args.add(a);
	}
	public int getCount()
	{
		return m_args.size();
	}
	public Argument getAt(int index)
	{
		return m_args.get(index);
	}
}
