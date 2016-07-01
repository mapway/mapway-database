package com.mapway.database2java.model.schema;

import java.util.ArrayList;

public class ModleTypes {
	ArrayList<ModleType> m_dic;
	public ModleTypes()
	{
		m_dic=new ArrayList<ModleType>(); 
	}
	public void Add(String key,String value)
	{
		ModleType mt=findModleType(key);
		if(mt==null)
			{
			mt=new ModleType();
			mt.setKey(key);
			m_dic.add(mt);
			}
		mt.setValue(value);
	}
	public ModleType findModleType(String key)
	{
		for(int i=0;i<m_dic.size();i++)
		{
			ModleType mt=m_dic.get(i);
			if(mt.key.equals(key))
			{
				return mt;
			}
		}
		return null;
	}
	public String findValue(String key)
	{
		for(int i=0;i<m_dic.size();i++)
		{
			ModleType mt=m_dic.get(i);
			if(mt.key.equals(key))
			{
				return mt.getValue();
			}
		}
		return "";
	}
	public String likeValue(String key)
	{
		for(int i=0;i<m_dic.size();i++)
		{
			ModleType mt=m_dic.get(i);
			if(mt.getKey().indexOf(key)>=0)
			{
				return mt.getValue();
			}
		}
		return "";
	}
}
