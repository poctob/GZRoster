package com.gzlabs.gzroster.data.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.gzroster.data.Position;

public class PositionTest {

	Position position;
	ArrayList<String> properties;
	@Before
	public void setUp() throws Exception {
		position=new Position();
		properties=new ArrayList<String>();
		properties.add("2");
		properties.add("Test Position");
		properties.add("Test Note");
		position.populateProperties(properties);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInsert_sql() {
		String target="INSERT INTO PLACE (PLACE_NAME,NOTE) VALUES ('Test Position','Test Note')";
		String sql=position.getInsert_sql(1);
		assertEquals("Strings should be the same", target, sql);
		
		position.setUsingFB(true);
		target="INSERT INTO PLACE (PLACE_NAME,NOTE,PLACE_ID) VALUES ('Test Position','Test Note','1')";
		sql=position.getInsert_sql(1);
		assertEquals("Strings should be the same", target, sql);
	}

	@Test
	public void testGetNexPKID_sql() {
		String target="SELECT NEXT VALUE FOR PLACE_ID_GEN FROM RDB$DATABASE";
		String sql=position.getNexPKID_sql();
		assertEquals("Strings should be the same", target, sql);
	}

	@Test
	public void testGetUpdate_sql() {
		String target="UPDATE PLACE SET PLACE_NAME='Test Position',NOTE='Test Note' WHERE PLACE_ID='2'";
		String sql=position.getUpdate_sql();
		assertEquals("Strings should be the same", target, sql);
		
	}

	@Test
	public void testGetDelete_sql() {
		String target="DELETE FROM PLACE WHERE PLACE_ID='2'";
		ArrayList<String> sql=position.getDelete_sql();
		assertEquals("Strings should be the same", target, sql.get(0));
	}

	@Test
	public void testPopulateProperites() {
		assertEquals("Strings should be the same", properties,position.toSortedArray());
		properties.add("2");
		position.populateProperties(properties);
		assertFalse("Strings should not be the same", position.matches(properties, false));
	}

	@Test
	public void testGetName() {
		assertEquals("Strings should be the same", "Test Position",position.getName());
	}

	@Test
	public void testGetPKID() {
		assertEquals("Strings should be the same", 2,position.getPKID());
	}

	@Test
	public void testMatches() {
		assertTrue("Strings should be the same", position.matches(properties, false));
		properties.set(0, "3");
		assertFalse("Strings should not be the same", position.matches(properties, true));
		properties.set(0, "2");
		properties.set(1, "2");
		assertFalse("Strings should not be the same", position.matches(properties, false));
		
		properties.add("2");
		assertFalse("Strings should not be the same", position.matches(properties, false));
	}

	@Test
	public void testToSortedArray() {
		assertEquals("Strings should be the same", properties,position.toSortedArray());
	}

}
