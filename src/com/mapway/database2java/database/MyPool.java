package com.mapway.database2java.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * 数据库线程池
 * @author zhangjianshe@gmail.com
 *
 */
public class MyPool implements IConnectionPool
{
    private int checkedOut;
    private Vector freeConnections = new Vector();
    private String driverName;
    private String passwd;
    private String username;
    private String jdbcURL;
    private String database;
    private int maxConnections;

    public String getGwt_base()
    {
	return gwt_base;
    }

    public void setGwt_base(String gwt_base)
    {
	this.gwt_base = gwt_base;
    }

   
    private static MyPool m_poolOracle;
    private static MyPool m_poolMySQL;
    private String path;
    private String pack;
    private String gwt_modle;
    private String gwt_dao;
    private String gwt_base;

    public static synchronized MyPool getInstance(String database)
    {
	return getInstance(database, "database");
    }

    public static synchronized MyPool getInstance()
    {
	return m_poolMySQL;
    }

    public static synchronized MyPool getInstance(String database,
	    String configurefile)
    {
	if (database == null || database.equals(""))
	{
	    database = "Oracle";
	}
	if (database.equals("Oracle"))
	{
	    if (m_poolOracle == null)
	    {
		m_poolOracle = new MyPool("Oracle", configurefile);
	    }
	    return m_poolOracle;
	} else if (database.equals("MySQL"))
	{
	    if (m_poolMySQL == null)
	    {
		m_poolMySQL = new MyPool("MySQL", configurefile);
	    }
	} else if (database.equals("MySQL2"))
	{
	    if (m_poolMySQL == null)
	    {
		m_poolMySQL = new MyPool("MySQL2", configurefile);
	    }
	}
	return m_poolMySQL;

    }

    /**
     * �����̳߳�
     * 
     * @param drivername
     *            ��ݿ������
     * @param jdbcurl
     *            ��ݿ�����URL
     * @param username
     *            ��ݿ��û���
     * @param password
     *            ��ݿ�����
     * @param packagename
     *            package���
     * @param path
     *            ��ɴ���·��
     * @param connectionSize
     *            ����ʱ��С
     */
    public MyPool(String drivername, String jdbcurl, String username,
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
	m_poolMySQL = this;
    }

    public final String DT_MYSQL = "MySQL";
    public final String DT_ORACLE = "Oracle";

    /**
     * 根据配置文件
     * 
     * @param databaseType
     *            数据库类型，可用的参数类型有 MySQL ，Oracle
     * @param rb
     *            数据库配置参数属性文件
     */
    public MyPool(String databaseType, ResourceBundle rb)
    {
	init(databaseType, rb);
    }

    public MyPool(String database, String configurefile)
    {
	if (database == null)
	{
	    database = "Oracle";
	}
	Locale locale = Locale.getDefault();
	ResourceBundle rb = ResourceBundle.getBundle(configurefile, locale);
	init(database, rb);
    }

    private void init(String databaseType, ResourceBundle rb)
    {
	this.driverName = rb.getString(database + "_driver");
	this.jdbcURL = rb.getString(database + "_dataurl");
	this.username = rb.getString(database + "_user");
	this.passwd = rb.getString(database + "_password");
	this.pack = rb.getString(database + "_package");
	this.path = rb.getString(database + "_path");
	this.database = rb.getString(database + "_database");
	this.netPath = rb.getString(database + "_netpath");

	this.gwt_modle = rb.getString("gwt_modle");
	this.gwt_dao = rb.getString("gwt_dao");
	this.gwt_base = rb.getString("gwt_base");

	this.maxConnections = Integer.valueOf(rb.getString(database
		+ "_maxconnections"));
	try
	{
	    Class.forName(driverName);
	} catch (ClassNotFoundException e)
	{
	    e.printStackTrace();
	}
    }

    public void log(String msg)
    {
	System.out.println(msg);
    }

    /**
     * ������ʹ�õ����ӷ��ظ����ӳ�
     * 
     * @param con
     *            �ͻ������ͷŵ�����
     */
    @SuppressWarnings("unchecked")
    public synchronized void releaseConnection(Connection con)
    {
	// ��ָ�����Ӽ��뵽����ĩβ
	freeConnections.addElement(con);
	checkedOut--;
	notifyAll();
    }

    /**
     * �����ӳػ��һ����������.��û�п��е������ҵ�ǰ������С���������
     * ������,�򴴽�������.��ԭ���Ǽ�Ϊ���õ����Ӳ�����Ч,�������ɾ��֮,
     * Ȼ��ݹ�����Լ��Գ����µĿ�������.
     */
    public synchronized Connection getConnection()
    {
	Connection con = null;
	if (freeConnections.size() > 0)
	{
	    // ��ȡ�����е�һ����������
	    con = (Connection) freeConnections.firstElement();
	    freeConnections.removeElementAt(0);
	    try
	    {
		if (con.isClosed())
		{
		    log("�����ӳ�ɾ��һ����Ч����");
		    // �ݹ�����Լ�,�����ٴλ�ȡ��������
		    con = getConnection();
		}
	    } catch (SQLException e)
	    {
		log("�����ӳ�ɾ��һ����Ч����");
		// �ݹ�����Լ�,�����ٴλ�ȡ��������
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

    /**
     * �����ӳػ�ȡ��������.����ָ���ͻ������ܹ��ȴ���ʱ�� �μ�ǰһ��getConnection()����.
     * 
     * @param timeout
     *            �Ժ���Ƶĵȴ�ʱ������
     */
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

    /**
     * �ر���������
     */
    public synchronized void close()
    {
	Enumeration allConnections = freeConnections.elements();
	while (allConnections.hasMoreElements())
	{
	    Connection con = (Connection) allConnections.nextElement();
	    try
	    {
		con.close();
		log("�ر����ӳ��е�һ������");
	    } catch (SQLException e)
	    {
		log("�޷��ر����ӳ��е�����");
	    }
	}
	freeConnections.removeAllElements();
    }

    /**
     * �����µ�����
     */
    private Connection createConnection()
    {
	Connection con = null;
	try
	{
	    if (this.username == null)
	    {
		con = DriverManager.getConnection(this.jdbcURL.replace(
			"$database", this.database));
	    } else
	    {
		con = DriverManager.getConnection(
			this.jdbcURL.replace("$database", this.database),
			this.username, this.passwd);
	    }
	    log("���ӳش���һ���µ�����@"
		    + this.jdbcURL.replace("$database", this.database) + ":"
		    + this.username + ":" + this.passwd);
	} catch (SQLException e)
	{
	    log("�޷���������URL������: "
		    + this.jdbcURL.replace("$database", this.database));
	    log("ԭ�� " + e.getMessage());
	    return null;
	}
	return con;
    }

    /**
     * @return the driverName
     */
    public String getDriverName()
    {
	return driverName;
    }

    /**
     * @param driverName
     *            the driverName to set
     */
    public void setDriverName(String driverName)
    {
	this.driverName = driverName;
    }

    /**
     * @return the jdbcURL
     */
    public String getJdbcURL()
    {
	return jdbcURL;
    }

    /**
     * @param jdbcURL
     *            the jdbcURL to set
     */
    public void setJdbcURL(String jdbcURL)
    {
	this.jdbcURL = jdbcURL;
    }

    /**
     * @return the passwd
     */
    public String getPasswd()
    {
	return passwd;
    }

    /**
     * @param passwd
     *            the passwd to set
     */
    public void setPasswd(String passwd)
    {
	this.passwd = passwd;
    }

    /**
     * @return the username
     */
    public String getUsername()
    {
	return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username)
    {
	this.username = username;
    }

    /**
     * @param driverName
     * @param passwd
     * @param username
     * @param jdbcURL
     */
    public MyPool(String driverName, String passwd, String username,
	    String jdbcURL)
    {
	super();
	this.driverName = driverName;
	this.passwd = passwd;
	this.username = username;
	this.jdbcURL = jdbcURL;
    }

    /**
     * @return the maxConnections
     */
    public int getMaxConnections()
    {
	return maxConnections;
    }

    /**
     * @param maxConnections
     *            the maxConnections to set
     */
    public void setMaxConnections(int maxConnections)
    {
	this.maxConnections = maxConnections;
    }

    public void setPackage(String pack)
    {
	this.pack = pack;
    }

    String netPath;

    public String getNetPath()
    {
	return netPath;
    }

    public void setNetPath(String netPath)
    {
	this.netPath = netPath;
    }

    public String getPath()
    {
	return path;
    }

    public void setPath(String path)
    {
	this.path = path;
    }

    public String getPackage()
    {
	return this.pack;
    }

    public String getDatabase()
    {
	return database;
    }

    public void setDatabase(String database)
    {
	this.database = database;
    }

    public String getGwtbase()
    {
	return this.gwt_base;
    }
}
