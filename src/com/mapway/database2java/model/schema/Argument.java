package com.mapway.database2java.model.schema;


public class Argument {

	private String Name;
	private String type;
	private String Property;
	private int    position;
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getProperty() {
		return Property;
	}
	public void setProperty(String property) {
		Property = property;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getJavaType()
	{
		TypeMapper tm=TypeMapper.getInstance();
		ModleTypes mts=tm.getOracle2JDBC();
		return mts.likeValue(this.type);
	}
	public String getJavaType2()
	{
		TypeMapper tm=TypeMapper.getInstance();
		ModleTypes mts=tm.getOracle2JDBC2();
		return mts.likeValue(this.type);
	}
	public String getJavaType3()
	{
		TypeMapper tm=TypeMapper.getInstance();
		ModleTypes mts=tm.getOracle2JDBC3();
		return mts.likeValue(this.type);
	}
	public String getNetType()
	{
		TypeMapper tm=TypeMapper.getInstance();
		ModleTypes mts=tm.getOracle2Net();
		return mts.likeValue(this.type);
	}
	public String getJavaType4() {
		TypeMapper tm=TypeMapper.getInstance();
		ModleTypes mts=tm.getOracle2JDBC4();
		return mts.likeValue(this.type);
	}
	
}
