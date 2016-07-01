package com.mapway.database2java.model.schema;


/**
 * @author ZhangJianshe
 *
 */
public class Column {
	String DatabaseType;
	String JavaType;
	String Name;
	String comment;
	boolean isPK;
	boolean isAuto=false;
	int length;
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
		
		
		if(this.comment!=null && !this.comment.equals(""))
		{
			int index=this.comment.indexOf("@Seq(");
			if(index>=0)
			{
				int end =this.comment.indexOf(")", index);
				if(end >0)
				{
					String str=this.comment.substring(index+5, end);
					
					this.m_seq=str.trim().toUpperCase();
					this.isAuto=true;
				}
			}
		}
		
	}
	public String getDatabaseType() {
		return DatabaseType;
	}
	String m_seq;
	public String getSequence()
	{
		return m_seq;
	}
	
	/**
	 * @param databaseType
	 */
	public void setDatabaseType(String databaseType) {
		DatabaseType = databaseType;
	}
	public String getJavaType() {
		TypeMapper tm=TypeMapper.getInstance();
		
		return tm.getOracle2JDBC().likeValue(this.DatabaseType);
	}
	public String getNetType() {
		TypeMapper tm=TypeMapper.getInstance();
		
		return tm.getOracle2Net().likeValue(this.DatabaseType);
	}
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public boolean isPK() {
		return isPK;
	}
	public void setPK(boolean isPK) {
		this.isPK = isPK;
	}
	public String getJavaType2() {
		TypeMapper tm=TypeMapper.getInstance();
		return tm.getOracle2JDBC2().likeValue(this.DatabaseType);
	}
	
	public boolean isAuto()
	{
	    return isAuto;
	}
	
	public void setAuto(boolean isAuto)
	{
	    this.isAuto = isAuto;
	}
	public String getJavaType4() {
		TypeMapper tm=TypeMapper.getInstance();
		return tm.getOracle2JDBC4().likeValue(this.DatabaseType);
	}
}
