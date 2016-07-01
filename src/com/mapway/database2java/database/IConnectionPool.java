
package com.mapway.database2java.database;


import java.sql.Connection;


public interface IConnectionPool {
	public Connection getConnection();
	public void releaseConnection(Connection con);
	public String getPath();
	public String getNetPath();
	public String getPackage();
	public String getGwtbase();
}
