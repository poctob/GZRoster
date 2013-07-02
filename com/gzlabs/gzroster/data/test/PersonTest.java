package com.gzlabs.gzroster.data.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.gzroster.data.Person;
import com.gzlabs.utils.DateUtils;

public class PersonTest {

	Person person;
	ArrayList<String> properties;
	ArrayList<Integer> positions;
	@Before
	public void setUp() throws Exception {
		person=new Person();
		properties=new ArrayList<String>();
		properties.add("2");
		properties.add("Test Name");
		properties.add("Test Address");
		properties.add("Test home phone");
		properties.add("Test mobile phone");
		properties.add("Test Note");
		properties.add("1");
		properties.add("Test Email");
		person.populateProperties(properties);
		
		positions=new ArrayList<Integer>();
		positions.add(1);
		positions.add(2);
		positions.add(3);
		person.setM_positions(positions);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInsert_sql() {
		 
		String target="INSERT INTO PERSON" +
				" (PERSON_NAME,ADDRESS,PHONE_HOME,PHONE_MOBILE,NOTE,ACTIVE_PERSON,EMAIL_ADDRESS) " +
				"VALUES ('Test Name','Test Address','Test home phone','Test mobile phone','Test Note','1','Test Email')";
		String sql=person.getInsert_sql(1);
		assertEquals("Strings should be the same", target, sql);
		
		person.setUsingFB(true);
		target="INSERT INTO PERSON" +
				" (PERSON_NAME,ADDRESS,PHONE_HOME,PHONE_MOBILE,NOTE,ACTIVE_PERSON,EMAIL_ADDRESS,PERSON_ID) " +
				"VALUES ('Test Name','Test Address','Test home phone','Test mobile phone','Test Note','1','Test Email','1')";
		sql=person.getInsert_sql(1);
		assertEquals("Strings should be the same", target, sql);
	}

	@Test
	public void testGetNexPKID_sql() {
		String target="SELECT NEXT VALUE FOR PERSON_ID_GEN FROM RDB$DATABASE";
		String sql=person.getNexPKID_sql();
		assertEquals("Strings should be the same", target, sql);
	}

	@Test
	public void testGetUpdate_sql() {
		String target="UPDATE PERSON SET " +
				"PERSON_NAME='Test Name',ADDRESS='Test Address',PHONE_HOME='Test home phone'," +
				"PHONE_MOBILE='Test mobile phone',NOTE='Test Note',ACTIVE_PERSON='1',EMAIL_ADDRESS='Test Email' " +
				"WHERE PERSON_ID='2'";
		String sql=person.getUpdate_sql();
		assertEquals("Strings should be the same", target, sql);
	}

	@Test
	public void testGetDelete_sql() {
		String target="DELETE FROM PERSON WHERE PERSON_ID='2'";
		ArrayList<String> sql=person.getDelete_sql();
		assertEquals("Strings should be the same", target, sql.get(0));
	}

	@Test
	public void testPopulateProperites() {
		assertEquals("Strings should be the same", properties,person.toSortedArray());
		properties.add("2");
		person.populateProperties(properties);
		assertFalse("Strings should not be the same", person.matches(properties, false));
	}

	@Test
	public void testGetName() {
		assertEquals("Strings should be the same", "Test Name",person.getName());
	}

	@Test
	public void testGetPKID() {
		assertEquals("Strings should be the same", 2,person.getPKID());
	}

	@Test
	public void testMatches() {
		assertTrue("Strings should be the same", person.matches(properties, false));
		properties.set(0, "3");
		assertFalse("Strings should not be the same", person.matches(properties, true));
		properties.set(0, "2");
		properties.set(1, "2");
		assertFalse("Strings should not be the same", person.matches(properties, false));
		
		properties.add("2");
		assertFalse("Strings should not be the same", person.matches(properties, false));
	}

	@Test
	public void testToSortedArray() {
		assertEquals("Strings should be the same", properties,person.toSortedArray());
	}

	@Test
	public void testIsActive() {
		assertTrue("Person should be active.", person.isActive());
	}

	@Test
	public void testGetM_positions() {
		assertEquals("Positions should be the same", positions,person.getM_positions());
	}

	@Test
	public void testSetM_positions() {
		positions.add(5);
		person.setM_positions(positions);
		assertEquals("Positions should be the same", positions,person.getM_positions());
	}

	@Test
	public void testIsPositionAllowed() {
		assertTrue("Position should be allowed", person.isPositionAllowed(3));
		assertFalse("Position should not be allowed", person.isPositionAllowed(30));
		assertFalse("Position should not be allowed", person.isPositionAllowed(-1));
		person.setM_positions(null);
		assertFalse("Position should not be allowed", person.isPositionAllowed(3));
	}

	@Test
	public void testGetTimesOff() {
		assertEquals("Times Off should not be initialized", 0,person.getTimesOff().size());
	}

	@Test
	public void testIsTimeAllowed() {
	//	fail("Not yet implemented"); 
		// TODO Implement
	}

	@Test
	public void testGetTimeOffInsertSql() {
		String start=DateUtils.DateToString(new Date());
		String end=DateUtils.DateToString(new Date());
		String result = "INSERT INTO PERSON_NA_AVAIL_HOURS " +
				"(PERSON_ID,PERSON_NA_START_DATE_HOUR,PERSON_NA_END_DATE_HOUR) " +
				"VALUES ('2','"+start+"','"+end+"')";
		assertEquals("Positions should be the same", result,person.getTimeOffInsertSql(start, end));
	}

	@Test
	public void testGetDeleteTimeOffSql() {
		String start=DateUtils.DateToString(new Date());
		String end=DateUtils.DateToString(new Date());
		String result = "DELETE FROM PERSON_NA_AVAIL_HOURS " +
				"WHERE PERSON_ID='2' AND PERSON_NA_START_DATE_HOUR='"+start+"' AND PERSON_NA_END_DATE_HOUR='" +
				end+"'";
		assertEquals("Positions should be the same", result,person.getDeleteTimeOffSql(start, end));
	}

	@Test
	public void testGetPersonToPositionsInsertSql() {
		String target="INSERT INTO PERSON_TO_PLACE (PERSON_ID,PLACE_ID) VALUES ('2','1')";
		String target2="INSERT INTO PERSON_TO_PLACE (PERSON_ID,PLACE_ID) VALUES ('2','2')";
		String target3="INSERT INTO PERSON_TO_PLACE (PERSON_ID,PLACE_ID) VALUES ('2','3')";
		ArrayList<String> sql=person.getPersonToPositionsInsertSql();
		assertEquals("Strings should be the same", target, sql.get(0));
		assertEquals("Strings should be the same", target2, sql.get(1));
		assertEquals("Strings should be the same", target3, sql.get(2));
	}

	@Test
	public void testGetPersonToPositionsDeleteSql() {
		String target="DELETE FROM PERSON_TO_PLACE WHERE PERSON_ID='2'";
		assertEquals("Strings should be the same", target,person.getPersonToPositionsDeleteSql());
	}

	@Test
	public void testIsUsingFB() {
		person.setUsingFB(true);
		assertTrue("Should be true", person.isUsingFB());
		person.setUsingFB(false);
		assertFalse("Should be true", person.isUsingFB());
	}

}
