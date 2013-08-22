package com.gzlabs.gzroster.data.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.gzroster.data.DataManager;
import com.gzlabs.gzroster.data.Privilege;
import com.gzlabs.gzroster.sql.DBObjectType;
import com.gzlabs.gzroster.sql.Tables;

public class PrivilegeTest {
	Privilege privilege;

	@Before
	public void setUp() throws Exception {
		privilege=new Privilege("Test Name", 100);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInsert_sql() {
		assertEquals(Tables.PROC_INSERT_PRIVILEGE, privilege.getInsert_sql());
	}

	@Test
	public void testGetUpdate_sql() {
		assertEquals(Tables.PROC_UPDATE_PRIVILEGE, privilege.getUpdate_sql());
	}

	@Test
	public void testGetDelete_sql() {
		assertEquals(Tables.PROC_DELETE_PRIVILEGE, privilege.getDelete_sql());
	}

	@Test
	public void testPopulateProperites() {
		Privilege privilege2=new Privilege("Test2", 200);
		privilege.populateProperties(privilege2);
		assertTrue(privilege.matches(privilege2, true));
	}

	/**
	 * The test requires database creation and modification.
	 * While it's not testing this method directly, it is still
	 * called in the process.  Make sure that the database
	 * contains test records and examine output.
	 */
	@Test
	public void testPopulateProperties() {
		TestCommon tc=new TestCommon();
		DataManager dman=tc.getDman();
		dman.addRecord(privilege, DBObjectType.PRIVILEGE);
		Object priv2=dman.getElementDetails("Test Name", DBObjectType.PRIVILEGE);
		assertNotNull(priv2);
		assertEquals("Test Name",((Privilege)priv2).getName());
		dman.deleteRecord(privilege, DBObjectType.PRIVILEGE);
	}

	@Test
	public void testGetName() {
		assertEquals("Test Name", privilege.getName());
	}

	@Test
	public void testGetPKID() {
		assertEquals(100, privilege.getPKID());
	}

	@Test
	public void testGetPKIDStr() {
		assertEquals("100", privilege.getPKIDStr());
	}

	@Test
	public void testToStringArray() {
		String [] array=privilege.toStringArray();
		assertTrue(array.length==2);
		assertEquals(array[0], "Test Name");
		assertEquals(array[1], "100");
	}

}
