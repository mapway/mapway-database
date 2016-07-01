package com.mapway.database2java.model.schema;

public class Pack {
	String name;
	Procedures procedures;
	public Pack()
	{
		procedures=new Procedures();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Procedures getProcedures() {
		return procedures;
	}
	public void setProcedures(Procedures procedures) {
		this.procedures = procedures;
	}
}
