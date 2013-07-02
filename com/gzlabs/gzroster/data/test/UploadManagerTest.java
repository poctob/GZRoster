package com.gzlabs.gzroster.data.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.gzroster.data.UploadManager;
import com.gzlabs.gzroster.gui.IDisplayStatus;

public class UploadManagerTest implements IDisplayStatus{

	UploadManager upman;
	Properties prop_mysql;
	private static final String CONFIG_FILE_PATH_MYSQL = "GZRoster.config";
	@Before
	public void setUp() throws Exception {
		prop_mysql = new Properties();
		prop_mysql.load(new FileInputStream(CONFIG_FILE_PATH_MYSQL));
		upman=new UploadManager(prop_mysql, this);		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetIds() {
		assertEquals("Objects should equal", this, upman.getIds());
	}

	@Test
	public void testSetIds() {
		upman.setIds(null);
		assertNull("Object should be null", upman.getIds());
	}

	@Test
	public void testGetProp() {
		assertEquals("Objects should equal", prop_mysql, upman.getProp());
	}

	@Override
	public void DisplayStatus(String status) {
		System.out.println(status);
		
	}

	@Override
	public void ShowErrorBox(String error) {
		// TODO Auto-generated method stub
		
	}

}
