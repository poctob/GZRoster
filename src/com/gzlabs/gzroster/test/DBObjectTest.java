package com.gzlabs.gzroster.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.drosterheper.DBManager;
import com.gzlabs.gzroster.data.DBObject;

public class DBObjectTest {

	//Database manager.
	private DBManager dbman;
		
	//Application properties.
	private Properties prop = null;
	
	private static final String CONFIG_FILE_PATH = "GZRoster.config";
	
	
	@Before
	public void setUp() throws Exception {
		
		try {
			prop = new Properties();
			prop.load(new FileInputStream(CONFIG_FILE_PATH));
			dbman = DBManager.getInstance(prop.getProperty("db_driver"),
					prop.getProperty("db_url"), prop.getProperty("db_username"),
					prop.getProperty("db_password"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDBObjectDBManagerString() {
		DBObject dbobject=new DBObject(dbman, "test");
		assertNotNull("Object must be instantiated", dbobject);		
	}

	@Test
	public void testDBObjectDBManagerStringHashMapOfStringString() {
		DBObject dbobject=new DBObject(dbman, "test", null);
		assertNotNull("Object must be instantiated", dbobject);	
	}

	@Test
	public void testGetDbman() {
		DBObject dbobject=new DBObject(dbman, "test");
		assertNotNull("Object must be instantiated", dbobject.getDbman());	
	}

	@Test
	public void testSetDbman() {
		DBObject dbobject=new DBObject(null, "test");
		dbobject.setDbman(dbman);
		assertNotNull("Object must be instantiated", dbobject.getDbman());	
	}

	@Test(expected = NullPointerException.class)
	public void testGetLast_error() {
		DBObject dbobject=new DBObject(dbman, "test");
		dbobject.setNewPKID("test");
		assertTrue("Last error string should not be empty", !dbobject.getLast_error().isEmpty());	
	}

	@Test
	public void testGetCols() {
		DBObject dbobject=new DBObject(dbman, "PERSON");
		assertNotNull("Cols should be instantiated", dbobject.getCols());
		assertTrue("Cols should be empty", dbobject.getCols().isEmpty());
	}

	@Test
	public void testSetCols() {
		HashMap<String, String> cols=new HashMap<String, String>();
		cols.put("test","test123");
		DBObject dbobject=new DBObject(dbman, "PERSON");
		dbobject.setCols(cols);
		assertTrue("Cols should be equal", dbobject.getCols().equals(cols));
		
	}

	@Test
	public void testAddProperty() {
		DBObject dbobject=new DBObject(dbman, "PERSON");
		dbobject.addProperty(null,"test123");
		assertTrue("Cols should be empty", dbobject.getCols().isEmpty());
		dbobject.addProperty("test","test123");
		assertTrue("Cols should be empty", !dbobject.getCols().isEmpty());
	}

	@Test
	public void testGetProperty() {
		DBObject dbobject=new DBObject(dbman, "PERSON");
		dbobject.addProperty("test","test123");
		assertTrue("Cols should be empty", dbobject.getProperty("test").equals("test123"));
	}

	@Test
	public void testGetValues() {
		DBObject dbobject=new DBObject(dbman, "PERSON");
		dbobject.addProperty("test","test123");
		dbobject.addProperty("test1","test1231");
		ArrayList<String> vals=dbobject.getValues();
		assertEquals("Values size should be 2", 2, vals.size());
		assertTrue("First value should equal test1231", vals.get(0).equals("test1231"));
		assertTrue("First value should equal test123", vals.get(1).equals("test123"));	
	}

	@Test
	public void testGenerateNewKey() {
		DBObject dbobject=new DBObject(dbman, "PERSON");
		dbobject.generateNewKey("test");
		String result=dbobject.getProperty("test");
		assertEquals("UUID should be 36 charcters long", 36, result.length());
	}

	@Test
	public void testDoesRecordExist() {
		DBObject dbobject=new DBObject(dbman, "PERSON");
		dbobject.addProperty("PERSON_NAME","test123");
		assertTrue("Record should not exist", !dbobject.doesRecordExist("test"));	
		
		dbobject.addProperty("PERSON_NAME", "Farva");
		assertTrue("Record should exist", dbobject.doesRecordExist("PERSON_NAME"));	
	}

	@Test
	public void testInsertNew() {
		DBObject dbobject=new DBObject(dbman, "PLACE");
		dbobject.addProperty("PLACE_NAME","test123");
		dbobject.setNewPKID("PLACE_ID");
		assertTrue("Record should be inserted", dbobject.insertNew());	
		assertTrue("Record should not be inserted", !dbobject.insertNew());	
	}

	@Test
	public void testUpdateRecord() {
		DBObject dbobject=new DBObject(dbman, "PLACE");
		dbobject.addProperty("PLACE_NAME","test123");
		dbobject.setNewPKID("PLACE_ID");
		assertTrue("Record should be updated", dbobject.updateRecord("PLACE_NAME"));	
		dbobject.addProperty("PLACE_NAME","test123456");
		assertTrue("Record should not be updated", dbobject.updateRecord("PLACE_NAME"));	
	}

	@Test
	public void testDeleteRecord() {
		DBObject dbobject=new DBObject(dbman, "PLACE");
		dbobject.addProperty("PLACE_NAME","test123");
		dbobject.setNewPKID("PLACE_ID");
		assertTrue("Record should be deleted", dbobject.deleteRecord("PLACE_NAME"));	
		dbobject.addProperty("PLACE_NAME","test123456");
		assertTrue("Record should not be deleted", dbobject.deleteRecord("PLACE_NAME"));	
	}

	@Test
	public void testSetNewPKID() {
		DBObject dbobject=new DBObject(dbman, "PLACE");
		dbobject.addProperty("PLACE_NAME","test123");
		dbobject.setNewPKID("PLACE_ID");
		String place_id= dbobject.getProperty("PLACE_ID");
		assertTrue("Next pkid should not be empty", place_id.length()>0);
		dbobject.setNewPKID("PLACE_ID");
		assertTrue("Next pkid should not equal current one", !place_id.equals(dbobject.getProperty("PLACE_ID")));
	}

}
