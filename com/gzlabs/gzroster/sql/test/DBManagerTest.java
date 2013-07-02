package com.gzlabs.gzroster.sql.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.gzroster.sql.DBManager;

public class DBManagerTest {

	DBManager dbman;
	Properties prop_mysql;
	Properties prop_firebird;
	private static final String CONFIG_FILE_PATH_MYSQL = "GZRoster.mysql.config";
	private static final String CONFIG_FILE_PATH_FIREBIRD = "GZRoster.firebird.config";
	@Before
	public void setUp() throws Exception {
		try {
			prop_mysql = new Properties();
			prop_mysql.load(new FileInputStream(CONFIG_FILE_PATH_MYSQL));
			prop_firebird = new Properties();
			prop_firebird.load(new FileInputStream(CONFIG_FILE_PATH_FIREBIRD));
		
		} catch (Exception e) {
			
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		assertNotNull("Object should be initialized", dbman);
		
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		assertNotNull("Object should be initialized", dbman);
	}

	@Test
	public void testInit() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		assertTrue("DB connection was not initialized", dbman.init());
		
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		assertTrue("DB connection was not initialized", dbman.init());
		
		
		dbman = DBManager.getInstance("fake",
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		assertFalse("DB connection should not have initialized", dbman.init());
	}

	@Test
	public void testGetMetaData() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.getMetaData();
		assertTrue("DB connection was not initialized", dbman.init());
		
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		dbman.getMetaData();
		assertTrue("DB connection was not initialized", dbman.init());

	}

	@Test
	public void testRunQuery() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.disconnect();
		dbman.init();
		String sql="SELECT * from PERSON";
		ResultSet rs=dbman.runQuery(sql, true);
		assertNotNull("Should have result", rs);
		rs=dbman.runQuery(null, true);
		assertNull("Result should be empty", rs);
		rs=dbman.runQuery(sql, false);
		assertNull("Result should be empty", rs);
		rs=dbman.runQuery("", true);
		assertNull("Result should be empty", rs);
		rs=dbman.runQuery("Mumbo jumbo", true);
		assertNull("Result should be empty", rs);
		
		dbman.disconnect();
		dbman.init();
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		sql="SELECT * from PERSON";
		rs=dbman.runQuery(sql, true);
		assertNotNull("Should have result", rs);
	}

}
