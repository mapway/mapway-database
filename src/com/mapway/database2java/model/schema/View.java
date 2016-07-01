package com.mapway.database2java.model.schema;


public class View implements ITable{
	Columns columns;
	public View()
	{
		columns=new Columns();
	}
	public Columns getColumns() {
		return columns;
	}
	String name;
	String owner;
	String comment;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getJavaName()
	{
		return this.name+"Obj";
	}
	public String getJavaNames()
	{
		return this.name+"Objs";
	}
	public String getJavaAccessName()
	{
		return this.name+"DAO";
	}
	public boolean hasPK() {
		
		return false;
	}
	public boolean hasAuto() {
		
		return false;
	}
}
