package com.mapway.database2java.model.schema;

import java.util.ArrayList;

public class Sequences {
	ArrayList<Sequence> Sequences;
	public Sequences(){
		Sequences=new ArrayList<Sequence>(); 
	}
	public int getCount()
	{
		return Sequences.size();
	}
	public Sequence getAt(int index){
		return Sequences.get(index);
	}
	public void Clear(){
		Sequences.clear();
	}
	public void addSequence(Sequence sequence)
	{
		Sequences.add(sequence);
	}
}
