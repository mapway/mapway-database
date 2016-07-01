package com.mapway.database2java.model.base;

public class Configure {

	String path;
	String Package;
	String database;
	String schema;
	String gwtbase;
	String netFilePath;
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getDatabase() {
	  return database;
	}
	public void setDatabase(String database) {
	  this.database = database;
	}
	public String getPackage() {
		return Package;
	}
	public void setPackage(String package1) {
		Package = package1;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFilePath()
	{
		String s=this.path+"\\";
		s+=this.Package.replace('.', '\\');
		return s;
	}
	public Configure copy()
	{
		Configure conf=new Configure();
		conf.setPath(this.getPath());
		conf.setPackage(this.getPackage());
		conf.setDatabase(this.getDatabase());
		conf.setSchema(this.getSchema());
		conf.setGwtbase(this.getGwtbase());
		
		return conf;
	}
	/**
	 * @return the gwtbase
	 */
	public String getGwtbase()
	{
	    return gwtbase;
	}
	/**
	 * @param gwtbase the gwtbase to set
	 */
	public void setGwtbase(String gwtbase)
	{
	    this.gwtbase = gwtbase;
	}
	/**
	 * @return the netFilePath
	 */
	public String getNetFilePath()
	{
	    return netFilePath;
	}
	/**
	 * @param netFilePath the netFilePath to set
	 */
	public void setNetFilePath(String netFilePath)
	{
	    this.netFilePath = netFilePath;
	}
}
