package com.gzlabs.gzroster.sql;

import java.sql.*;

/**
 * Database management functionality
 * 
 * @author apavlune
 * 
 */
public class DBManager {

	// Database connection data
	private String driver;
	private String dbURL;
	private String user;
	private String password;

	// Database objects
	private Driver d = null;
	private Connection c = null;
	private Statement s = null;
	private ResultSet rs = null;
	
	//Singleton DBManager
	private static DBManager dbman;

	/**
	 * Default private constructor. Initializes member variables
	 * 
	 * @param driver
	 *            Driver to use.
	 * @param dbURL
	 *            DB connection URL.
	 * @param user
	 *            User name
	 * @param password
	 *            Password.
	 */
	private DBManager(String driver, String dbURL, String user, String password) {
		this.driver = driver;
		this.dbURL = dbURL;
		this.user = user;
		this.password = password;
	}
	
	public static DBManager getInstance(String driver, String dbURL, String user, String password)
	{
		if(dbman==null)
			dbman=new DBManager(driver, dbURL, user, password);
		else
		{
			dbman.driver = driver;
			dbman.dbURL = dbURL;
			dbman.user = user;
			dbman.password = password;
		}
			
		return dbman;
	}

	/**
	 * Initializes database connection
	 * 
	 * @return true if all operations were successful
	 */
	public boolean init() {
		boolean success=registerDriver();
		if(success)
			success=initializeDriver();
		if(success)
			success=connect();
		return success;
	}

	/**
	 * Registers database driver.
	 * 
	 * @return true if all operations were successful
	 */
	private boolean registerDriver() {
		try {
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * Initializes DB driver.
	 * 
	 * @return true if all operations were successful.
	 */
	private boolean initializeDriver() {
		try {
			
			d = DriverManager.getDriver(dbURL);
			System.out.println("Using the driver version"
					+ +d.getMajorVersion() + "." + d.getMinorVersion());

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Connects to the database.
	 * 
	 * @return true if all operations were successful.
	 */
	private boolean connect() {
		try {
			c = DriverManager.getConnection(dbURL, user, password);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Prints out database metadata.
	 */
	public void getMetaData() {
		try {
			DatabaseMetaData dbmeta = c.getMetaData();
			if (dbmeta.supportsTransactions()) {
				System.out.println("Transactions are supported.");
			} else {
				System.out.println("Transactions are not supported.");
			}

			ResultSet tables = dbmeta.getTables(null, null, "%",
					new String[] { "VIEW" });
			while (tables.next()) {
				System.out.println(tables.getString("TABLE_NAME")
						+ " is a view.");
			}
			tables.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes a query.
	 * 
	 * @param query
	 *            Query string to execute.
	 * @param wantresults
	 *            Indicated if this query expected to return results
	 * @return query results.
	 */
	public ResultSet runQuery(String query, boolean wantresults) {
		if(query==null || query.length()==0)
		{
			return null;
		}
		if(c==null)
		{
			init();
		}
		try {
			s = c.createStatement();
			if (wantresults)
				rs = s.executeQuery(query);
			else
				s.executeUpdate(query);
			return rs;
		} catch (SQLException e) {			
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Terminates database connections.
	 */
	public void disconnect() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (s != null) {
			try {
				s.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (c != null) {
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
