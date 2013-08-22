package com.gzlabs.gzroster.sql.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.gzroster.data.DB_Object;
import com.gzlabs.gzroster.data.Duty;
import com.gzlabs.gzroster.data.Position;
import com.gzlabs.gzroster.sql.DBManager;
import com.gzlabs.gzroster.sql.DB_Factory;
import com.gzlabs.gzroster.sql.Tables;
import com.gzlabs.utils.DateUtils;

public class DB_FactoryTest {

/*	DBManager dbman;
	Properties prop_mysql;
	Properties prop_firebird;
	private static final String CONFIG_FILE_PATH_MYSQL = "GZRoster.mysql.config";
	private static final String CONFIG_FILE_PATH_FIREBIRD = "GZRoster.firebird.config";
	
	private ArrayList<DB_Object> db_persons;
	
	private ArrayList<DB_Object> db_positions;
	
	private ArrayList<DB_Object> db_duties;
	
	Calendar start;
	Calendar end;
	@Before
	public void setUp() throws Exception {
		try {
			prop_mysql = new Properties();
			prop_mysql.load(new FileInputStream(CONFIG_FILE_PATH_MYSQL));
			prop_firebird = new Properties();
			prop_firebird.load(new FileInputStream(CONFIG_FILE_PATH_FIREBIRD));
						
		
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		start=new GregorianCalendar();
		start.set(2013, 1, 1, 1, 0, 0);
		end=new GregorianCalendar();
		end.set(2013, 1, 1, 1, 0, 0);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateObject() {
		DB_Object obj=DB_Factory.createObject(null);
		assertNull("Object should be null", obj);
		obj=DB_Factory.createObject(DB_Factory.ObjectType.POSITION);
		assertTrue("Should get position", obj.getClass().getName().equals("com.gzlabs.gzroster.data.Position"));
		obj=DB_Factory.createObject(DB_Factory.ObjectType.PERSON);
		assertTrue("Should get person", obj.getClass().getName().equals("com.gzlabs.gzroster.data.Person"));
		obj=DB_Factory.createObject(DB_Factory.ObjectType.DUTIES);
		assertTrue("Should get duty", obj.getClass().getName().equals("com.gzlabs.gzroster.data.Duty"));
		obj=DB_Factory.createObject(DB_Factory.ObjectType.PERSON_TO_PLACE);
		assertNull("Object should be null", obj);
	}

	@Test
	public void testGetAllDuties() {
		db_duties=DB_Factory.getAllDuties(null, null, null, false);
		assertNull("Object should be null", db_duties);
		

		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);	
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, false);
		db_duties=DB_Factory.getAllDuties(dbman, db_persons, db_positions, false);
		assertNotNull("Object should not be null", db_duties);
		assertTrue("Object should not be emply", db_duties.size()>0);
		
		
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, true);	
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, true);
		db_duties=DB_Factory.getAllDuties(dbman, db_persons, db_positions, true);
		assertNotNull("Object should not be null", db_duties);
		assertTrue("Object should not be emply", db_duties.size()>0);		
		
	}

	@Test
	public void testGetAllRecordsObjectTypeDBManagerStringStringBoolean() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		ArrayList<DB_Object> objects=DB_Factory.getAllRecords(null, dbman, null, null, false);
		assertNull("Object should be null", objects);
		objects=DB_Factory.getAllRecords(DB_Factory.ObjectType.POSITION, null, null, null, false);
		assertNull("Object should be null", objects);
		objects=DB_Factory.getAllRecords(DB_Factory.ObjectType.POSITION, dbman, "Fake column name", "Fake value name", false);
		assertNull("Object should be null", objects);
		
		objects=DB_Factory.getAllRecords(DB_Factory.ObjectType.POSITION, dbman, "1", "1", false);
		assertNotNull("Object should not be null", objects);
		assertTrue("Object should not be emply", objects.size()>0);		
		
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		dbman.init();
		objects=DB_Factory.getAllRecords(DB_Factory.ObjectType.POSITION, dbman, "1", "1", true);
		assertNotNull("Object should not be null", objects);
		assertTrue("Object should not be emply", objects.size()>0);		
	}

	@Test
	public void testGetAllRecordsObjectTypeDBManagerBoolean() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		ArrayList<DB_Object> objects=DB_Factory.getAllRecords(null, dbman, false);
		
		assertNull("Object should be null", objects);
		objects=DB_Factory.getAllRecords(DB_Factory.ObjectType.POSITION, null, false);
		assertNull("Object should be null", objects);
		
		objects=DB_Factory.getAllRecords(DB_Factory.ObjectType.POSITION, dbman, false);
		assertNotNull("Object should not be null", objects);
		assertTrue("Object should not be emply", objects.size()>0);	
		
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		dbman.init();
		objects=DB_Factory.getAllRecords(DB_Factory.ObjectType.POSITION, dbman, true);
		assertNotNull("Object should not be null", objects);
		assertTrue("Object should not be emply", objects.size()>0);		
	}

	@Test
	public void testInsertRecord() {
		ArrayList<String>properties=new ArrayList<String>();
		properties.add("");
		properties.add("Test Position 1");
		properties.add("Test Note");
		
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		boolean result=DB_Factory.insertRecord(null, null, null, false);
		assertFalse("result should be false", result);
		result=DB_Factory.insertRecord(null, dbman, properties, false);
		assertFalse("result should be false", result);
		result=DB_Factory.insertRecord(DB_Factory.ObjectType.POSITION, null, properties, false);
		assertFalse("result should be false", result);
		result=DB_Factory.insertRecord(DB_Factory.ObjectType.POSITION, dbman, properties, false);
		assertTrue("result should be true", result);
		
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		dbman.init();
		result=DB_Factory.insertRecord(DB_Factory.ObjectType.POSITION, dbman, properties, true);
		assertTrue("result should be true", result);
	}

	@Test
	public void testDeleteRecord() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);	
		boolean result=DB_Factory.deleteRecord(null, null, null);
		assertFalse("result should be false", result);
		result=DB_Factory.deleteRecord(db_positions, null, null);
		assertFalse("result should be false", result);
		result=DB_Factory.deleteRecord(db_positions, dbman, null);
		assertFalse("result should be false", result);
		result=DB_Factory.deleteRecord(db_positions, dbman, "Test Position 1");
		assertTrue("result should be true", result);
		
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);	
		result=DB_Factory.deleteRecord(db_positions, dbman, "Test Position 1");
		assertTrue("result should be true", result);
	}
	
	@Test
	public void testInsertDuty() {
	
		
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);	
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, false);
		db_duties=DB_Factory.getAllDuties(dbman, db_persons, db_positions, false);
		
		ArrayList<String> d_properties=new ArrayList<String>();	
		end.add(Calendar.HOUR_OF_DAY,2);
		d_properties.add(DateUtils.DateToString(start.getTime()));
		d_properties.add(db_positions.get(0).getName());
		d_properties.add(db_persons.get(0).getName());
		d_properties.add(DateUtils.DateToString(end.getTime()));
		d_properties.add("");
		boolean result=DB_Factory.insertDuty(null, null, null, null);
		assertFalse("result should be false", result);
		result=DB_Factory.insertDuty(dbman, null, null, null);
		assertFalse("result should be false", result);
		result=DB_Factory.insertDuty(dbman, d_properties, null, null);
		assertFalse("result should be false", result);
		result=DB_Factory.insertDuty(dbman, d_properties, db_positions, null);
		assertFalse("result should be false", result);
		result=DB_Factory.insertDuty(dbman, d_properties, null, db_persons);
		assertFalse("result should be false", result);
		result=DB_Factory.insertDuty(dbman, d_properties, db_positions, db_persons);
		assertTrue("result should be true", result);
		
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);	
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, false);
		db_duties=DB_Factory.getAllDuties(dbman, db_persons, db_positions, false);
		
		d_properties=new ArrayList<String>();
		d_properties.add(DateUtils.DateToString(start.getTime()));
		d_properties.add(db_positions.get(0).getName());
		d_properties.add(db_persons.get(0).getName());
		d_properties.add(DateUtils.DateToString(end.getTime()));
		d_properties.add("");
		result=DB_Factory.insertDuty(dbman, d_properties, db_positions, db_persons);
		assertTrue("result should be true", result);
	}

	@Test
	public void testDeleteDuty() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);	
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, false);
		db_duties=DB_Factory.getAllDuties(dbman, db_persons, db_positions, false);
		boolean result=DB_Factory.deleteDuty(null, null, null);
		assertFalse("result should be false", result);
		result=DB_Factory.deleteDuty(db_duties, null, null);
		assertFalse("result should be false", result);
		result=DB_Factory.deleteDuty(db_duties, dbman, null);
		assertFalse("result should be false", result);
		
		ArrayList<String> d_properties=new ArrayList<String>();	
		end.add(Calendar.HOUR_OF_DAY,2);
		d_properties.add(DateUtils.DateToString(start.getTime()));
		d_properties.add(db_positions.get(0).getName());
		d_properties.add(db_persons.get(0).getName());
		d_properties.add(DateUtils.DateToString(end.getTime()));
		d_properties.add("");
		result=DB_Factory.deleteDuty(db_duties, dbman, d_properties);
		assertTrue("result should be true", result);
		
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);	
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, false);
		db_duties=DB_Factory.getAllDuties(dbman, db_persons, db_positions, false);
		
		d_properties=new ArrayList<String>();
		d_properties.add(DateUtils.DateToString(start.getTime()));
		d_properties.add(db_positions.get(0).getName());
		d_properties.add(db_persons.get(0).getName());
		d_properties.add(DateUtils.DateToString(end.getTime()));
		d_properties.add("");
		result=DB_Factory.deleteDuty(db_duties, dbman, d_properties);
		assertTrue("result should be true", result);
	}

	@Test
	public void testFindDutyByTime() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);	
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, false);
		db_duties=DB_Factory.getAllDuties(dbman, db_persons, db_positions, false);
		Duty target=(Duty) db_duties.get(0);
		Duty dest=DB_Factory.findDutyByTime(null, 0, 0, null);
		assertNull("Object should be null", dest);
		
		dest=DB_Factory.findDutyByTime(db_duties, 0, 0, null);
		assertNull("Object should be null", dest);
		dest=DB_Factory.findDutyByTime(db_duties, target.getM_person().getPKID(), 0, null);
		assertNull("Object should be null", dest);
		dest=DB_Factory.findDutyByTime(db_duties, target.getM_person().getPKID(), target.getM_position().getPKID(), null);
		assertNull("Object should be null", dest);
		dest=DB_Factory.findDutyByTime(db_duties, target.getM_person().getPKID(), target.getM_position().getPKID(), 
				DateUtils.DateToString(target.getM_start()));
		assertNotNull("Object should not be null", dest);
		assertEquals("Objects should equal", target, dest);
		
	}

	@Test
	public void testUpdateRecord() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);	
		ArrayList<String> properties=db_positions.get(0).toSortedArray();
		properties.set(Tables.PLACE_NAME_INDEX, "Updated Name");
		
		boolean result=DB_Factory.updateRecord(null, null, null, null);
		assertFalse("result should be false", result);
		result=DB_Factory.updateRecord(db_positions, null, null, null);
		assertFalse("result should be false", result);
		result=DB_Factory.updateRecord(db_positions, dbman, null, null);
		assertFalse("result should be false", result);
		result=DB_Factory.updateRecord(db_positions, dbman, db_positions.
				get(0).toSortedArray(), properties);
		assertTrue("result should be true", result);
		
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);
		properties=db_positions.get(0).toSortedArray();
		properties.set(Tables.PLACE_NAME_INDEX, "Updated Name");
		result=DB_Factory.updateRecord(db_positions, dbman, db_positions.
				get(0).toSortedArray(), properties);
		assertTrue("result should be true", result);	
	}

	@Test
	public void testGetObjectByName() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);	
		Position dest=(Position) DB_Factory.getObjectByName(null, null);
		assertNull("Object should be null", dest);
		dest=(Position) DB_Factory.getObjectByName(db_positions, null);
		assertNull("Object should be null", dest);
		dest=(Position) DB_Factory.getObjectByName(null, "Updated Name");
		assertNull("Object should be null", dest);
		dest=(Position) DB_Factory.getObjectByName(db_positions, "Updated Name");
		assertEquals("Objects should equal", db_positions.get(0), dest);
	}

	@Test
	public void testGetObjectByPKID() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);	
		int pkid=db_positions.get(0).getPKID();
		Position dest=(Position) DB_Factory.getObjectByPKID(null, 0);
		assertNull("Object should be null", dest);
		dest=(Position) DB_Factory.getObjectByPKID(db_positions, 0);
		assertNull("Object should be null", dest);
		dest=(Position) DB_Factory.getObjectByPKID(null, pkid);
		assertNull("Object should be null", dest);
		dest=(Position) DB_Factory.getObjectByPKID(db_positions, pkid);
		assertEquals("Objects should equal", db_positions.get(0), dest);
	}

	@Test
	public void testGetNames() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);
		ArrayList<String> names=DB_Factory.getNames(null);
		assertEquals("Object should be empty", 0,names.size());
		names=DB_Factory.getNames(db_positions);
		assertEquals("Object length should equal", db_positions.size(), names.size());
	}

	@Test
	public void testGetPIKDs() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);
		ArrayList<Integer> names=DB_Factory.getPIKDs(null);
		assertEquals("Object should be empty", 0,names.size());
		names=DB_Factory.getPIKDs(db_positions);
		assertEquals("Object length should equal", db_positions.size(), names.size());
	}

	@Test
	public void testGetPIKDsStr() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);
		ArrayList<String> names=DB_Factory.getPIKDsStr(null);
		assertEquals("Object should be empty", 0,names.size());
		names=DB_Factory.getPIKDsStr(db_positions);
		assertEquals("Object length should equal", db_positions.size(), names.size());
	}

	@Test
	public void testGetObjectNameByPKID() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);
		int pkid=db_positions.get(0).getPKID();
		String name=db_positions.get(0).getName();
		String dest=DB_Factory.getObjectNameByPKID(null, 0);
		assertNull("Object should be null", dest);
		dest=DB_Factory.getObjectNameByPKID(db_positions, 0);
		assertNull("Object should be null", dest);
		dest=DB_Factory.getObjectNameByPKID(null, pkid);
		assertNull("Object should be null", dest);
		dest=DB_Factory.getObjectNameByPKID(db_positions, pkid);
		assertEquals("Objects should equal", name, dest);
	}

	@Test
	public void testGetObjectPKIDByName() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);
		int pkid=db_positions.get(0).getPKID();
		String name=db_positions.get(0).getName();
		int dest=DB_Factory.getObjectPKIDByName(null, null);
		assertEquals("Object should be null", 0,dest);
		dest=DB_Factory.getObjectPKIDByName(db_positions, null);
		assertEquals("Object should be null", 0,dest);
		dest=DB_Factory.getObjectPKIDByName(null, name);
		assertEquals("Object should be null", 0,dest);
		dest=DB_Factory.getObjectPKIDByName(db_positions, name);
		assertEquals("Objects should equal", pkid, dest);
	}

	@Test
	public void testGeDetaislByName() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);
		String name=db_positions.get(0).getName();
		ArrayList<String> details=db_positions.get(0).toSortedArray();
		ArrayList<String> dest=DB_Factory.geDetaislByName(null, null);
		assertNull("Object should be null", dest);
		dest=DB_Factory.geDetaislByName(db_positions, null);
		assertNull("Object should be null", dest);
		dest=DB_Factory.geDetaislByName(null, name);
		assertNull("Object should be null", dest);
		dest=DB_Factory.geDetaislByName(db_positions, name);
		assertEquals("Objects should equal", details, dest);
	}

	@Test
	public void testAddTimeOff() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, false);
		String name=db_persons.get(0).getName();
		boolean result=DB_Factory.addTimeOff(null, null, null, null, null);
		assertFalse("Result should be false", result);
		result=DB_Factory.addTimeOff(db_persons, DateUtils.DateToString(start.getTime()),
				DateUtils.DateToString(end.getTime()), name, dbman);
		assertTrue("Result should be true", result);
		
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		dbman.init();
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, false);
		name=db_persons.get(0).getName();
		result=DB_Factory.addTimeOff(db_persons, DateUtils.DateToString(start.getTime()),
				DateUtils.DateToString(end.getTime()), name, dbman);
		assertTrue("Result should be true", result);
	}
	
	
	@Test
	public void testDeleteTimeOff() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, false);
		String name=db_persons.get(0).getName();
		boolean result=DB_Factory.deleteTimeOff(null, null, null, null, null);
		assertFalse("Result should be false", result);
		result=DB_Factory.deleteTimeOff(db_persons, DateUtils.DateToString(start.getTime()),
				DateUtils.DateToString(end.getTime()), name, dbman);
		assertTrue("Result should be true", result);
		
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		dbman.init();
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, false);
		name=db_persons.get(0).getName();
		result=DB_Factory.deleteTimeOff(db_persons, DateUtils.DateToString(start.getTime()),
				DateUtils.DateToString(end.getTime()), name, dbman);
		assertTrue("Result should be true", result);
	}
	
	@Test
	public void testUpdatePersonToPosition() {
		dbman = DBManager.getInstance(prop_mysql.getProperty("db_driver"),
				prop_mysql.getProperty("db_url"), prop_mysql.getProperty("db_username"),
				prop_mysql.getProperty("db_password"));
		dbman.init();
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, false);
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);
		ArrayList<Integer> place_ids=DB_Factory.getPIKDs(db_positions);
		String name=db_persons.get(0).getName();
		boolean result=DB_Factory.updatePersonToPosition(null, null, null, null);
		assertFalse("Result should be false", result);
		result=DB_Factory.updatePersonToPosition(db_persons, place_ids, name, dbman);
		assertTrue("Result should be true", result);
		
		dbman = DBManager.getInstance(prop_firebird.getProperty("db_driver"),
				prop_firebird.getProperty("db_url"), prop_firebird.getProperty("db_username"),
				prop_firebird.getProperty("db_password"));
		dbman.init();
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, false);
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);
		place_ids=DB_Factory.getPIKDs(db_positions);
		name=db_persons.get(0).getName();
		result=DB_Factory.updatePersonToPosition(db_persons, place_ids, name, dbman);
		assertTrue("Result should be true", result);
	}*/

}
