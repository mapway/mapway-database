package com.mapway.database2java.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

public class GeneratorPool implements IConnectionPool
{

    public GeneratorPool getInstance()
    {
	return m_pool;
	// TODO Auto-generated constructor stub
    }

    private static GeneratorPool m_pool;

  
    public GeneratorPool(String drivername, String jdbcurl, String username,
	    String password, String packagename, String path, int connectionSize)
    {
	this.driverName = drivername;
	this.jdbcURL = jdbcurl;
	this.username = username;
	this.passwd = password;
	this.pack = packagename;
	this.path = path;
	this.maxConnections = connectionSize;

	try
	{
	    Class.forName(driverName);
	} catch (ClassNotFoundException e)
	{
	    e.printStackTrace();
	}
	m_pool = this;
    }

    private int checkedOut;
    private Vector freeConnections = new Vector();
    private String driverName;
    private String passwd;
    private String username;
    private String jdbcURL;
    private String database;
    private int maxConnections;
    private String pack;
    private String path;

    public void log(String msg)
    {
	System.out.println(msg);
    }

    @Override
    public Connection getConnection()
    {
	Connection con = null;
	if (freeConnections.size() > 0)
	{
	   
	    con = (Connection) freeConnections.firstElement();
	    freeConnections.removeElementAt(0);
	    try
	    {
		if (con.isClosed())
		{
		   
		    con = getConnection();
		}
	    } catch (SQLException e)
	    {
		log("�����ӳ�ɾ��һ����Ч����");
		
		con = getConnection();
	    }
	} else if (this.maxConnections == 0
		|| this.checkedOut < this.maxConnections)
	{
	    con = createConnection();
	}
	if (con != null)
	{
	    checkedOut++;
	}
	return con;
    }

    @Override
    public synchronized void releaseConnection(Connection con)
    {
	freeConnections.addElement(con);
	checkedOut--;
	notifyAll();
    }


    public synchronized Connection getConnection(long timeout)
    {
	long startTime = new java.util.Date().getTime();
	Connection con;
	while ((con = getConnection()) == null)
	{
	    try
	    {
		wait(timeout);
	    } catch (InterruptedException e)
	    {
	    }
	    if ((new java.util.Date().getTime() - startTime) >= timeout)
	    {
		// wait()���ص�ԭ���ǳ�ʱ
		return null;
	    }
	}
	return con;
    }

    public synchronized void close()
    {
	Enumeration allConnections = freeConnections.elements();
	while (allConnections.hasMoreElements())
	{
	    Connection con = (Connection) allConnections.nextElement();
	    try
	    {
		con.close();
	    } catch (SQLException e)
	    {
		
	    }
	}
	freeConnections.removeAllElements();
    }


    private Connection createConnection()
    {
	Connection con = null;
	try
	{
	    if (this.username == null)
	    {
		con = DriverManager.getConnection(this.jdbcURL);
	    } else
	    {
		con = DriverManager.getConnection(this.jdbcURL, this.username,
			this.passwd);
	    }
	    log("" + this.jdbcURL + ":" + this.username + ":"
		    + this.passwd);
	} catch (SQLException e)
	{
	    log(" " + this.jdbcURL);
	    log("" + e.getMessage());
	    return null;
	}
	return con;
    }

    @Override
    public String getPath()
    {
	return path;
    }

    @Override
    public String getNetPath()
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getPackage()
    {
	return pack;
    }

    @Override
    public String getGwtbase()
    {
	// TODO Auto-generated method stub
	return pack;
    }

}
