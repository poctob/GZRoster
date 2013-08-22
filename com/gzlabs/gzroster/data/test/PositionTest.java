package com.gzlabs.gzroster.data.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.gzroster.data.DataManager;
import com.gzlabs.gzroster.data.Position;
import com.gzlabs.gzroster.sql.DBObjectType;
import com.gzlabs.gzroster.sql.Tables;

public class PositionTest {

	Position position;
	@Before
	public void setUp() throws Exception {
		position=new Position( "Test Position",  "Test Note", 100);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInsert_sql() {
		assertEquals(Tables.PROC_INSERT_POSITION, position.getInsert_sql());
	}

	@Test
	public void testGetUpdate_sql() {
		assertEquals(Tables.PROC_UPDATE_POSITION, position.getUpdate_sql());
	}

	@Test
	public void testGetDelete_sql() {
		assertEquals(Tables.PROC_DELETE_POSITION, position.getDelete_sql());
	}

	@Test
	public void testPopulateProperites() {
		Position position2=new Position("Test2", "Test Note2", 200);
		position.populateProperties(position2);
		assertTrue(position.matches(position2, true));
	}

	@Test
	public void testPopulateProperties() {
		TestCommon tc=new TestCommon();
		DataManager dman=tc.getDman();
		dman.addRecord(position, DBObjectType.POSITION);
		Object priv2=dman.getElementDetails("Test Position", DBObjectType.POSITION);
		assertNotNull(priv2);
		assertEquals("Test Position",((Position)priv2).getName());
		assertEquals("Test Note",((Position)priv2).getNote());
		dman.deleteRecord(position, DBObjectType.POSITION);
	}

	@Test
	public void testGetName() {
		assertEquals("Test Position", position.getName());
	}

	@Test
	public void testGetPKID() {
		assertEquals(100, position.getPKID());
	}

	@Test
	public void testGetPKIDStr() {
		assertEquals("100", position.getPKIDStr());
	}

	@Test
	public void testToStringArray() {
		String [] array=position.toStringArray();
		assertTrue(array.length==3);
		assertEquals(array[0], "Test Position");
		assertEquals(array[1], "Test Note");
		assertEquals(array[2], "100");
	}

	@Test
	public void testGetNote() {
		assertEquals("Test Note", position.getNote());
	}

}
