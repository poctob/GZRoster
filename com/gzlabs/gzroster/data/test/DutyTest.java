package com.gzlabs.gzroster.data.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.gzroster.data.DataManager;
import com.gzlabs.gzroster.data.Duty;
import com.gzlabs.gzroster.data.Person;
import com.gzlabs.gzroster.data.Position;
import com.gzlabs.gzroster.data.Privilege;
import com.gzlabs.gzroster.data.TimeOff;
import com.gzlabs.gzroster.sql.DBObjectType;
import com.gzlabs.gzroster.sql.Tables;
import com.gzlabs.utils.DateUtils;

public class DutyTest {
	Position position;
	Privilege privilege;
	Person person;
	
	Calendar start;
	Calendar end;
	Duty duty;
	@Before
	public void setUp() throws Exception {
		TestCommon tc=new TestCommon();
		DataManager dman=tc.getDman();
		
		position=new Position( "Test Position",  "Test Note", 100);
		dman.addRecord(position, DBObjectType.POSITION);
		
		privilege=new Privilege("Test Name", 100);
		dman.addRecord(privilege, DBObjectType.PRIVILEGE);
		
		ArrayList<String> positions=new ArrayList<String>();
		ArrayList<String> privileges=new ArrayList<String>();
		positions.add(position.getName());
		privileges.add(privilege.getName());
		
		Calendar start1=new GregorianCalendar();
		Calendar end1=new GregorianCalendar();
		end1.set(Calendar.DATE, end1.get(Calendar.DATE)+1);
		TimeOff timeoff=new TimeOff(start1, end1, "Pending", "Test Person");
		
		ArrayList<TimeOff> timesoff=new ArrayList<TimeOff>();
		timesoff.add(timeoff);
		
		person=new Person("Test Person","Test Address","23204230420","4732752399743",true,"TEst email@",100,timesoff, positions, privileges);
		dman.addRecord(person, DBObjectType.PERSON);
		
		
		start=new GregorianCalendar();
		end=new GregorianCalendar();
		end.set(Calendar.HOUR, end.get(Calendar.HOUR)+1);
		duty=new Duty(start, end, position.getName(), person.getName(), "1234");
	}

	@After
	public void tearDown() throws Exception {
		TestCommon tc=new TestCommon();
		DataManager dman=tc.getDman();
		
		dman.deleteRecord(position, DBObjectType.POSITION);
		dman.deleteRecord(privilege, DBObjectType.PRIVILEGE);
		dman.deleteRecord(person, DBObjectType.PERSON);
	}

/*	@Test
	public void testGetInsert_sql() {
		assertEquals(Tables.PROC_INSERT_DUTY, duty.getInsert_sql());
	}

	@Test
	public void testGetUpdate_sql() {
		assertEquals(Tables.PROC_UPDATE_DUTY, duty.getInsert_sql());
	}

	@Test
	public void testGetDelete_sql() {
		assertEquals(Tables.PROC_DELETE_DUTY, duty.getInsert_sql());
	}

	@Test
	public void testPopulateProperitesResultSet() {
		TestCommon tc=new TestCommon();
		DataManager dman=tc.getDman();
		
		dman.addRecord(duty, DBObjectType.DUTY);
		ArrayList<String> persons=dman.isDutyOn(DateUtils.CalendarToString(start), position.getName());
		assertNotNull(persons);
		assertEquals(1,persons.size());
		assertEquals(person.getName(),persons.get(0));
		dman.deleteDuty(start, end, position.getName(), person.getName());
	}*/

	@Test
	public void testPopulateProperties() {
		fail("Not yet implemented"); // TODO
	}

	/*@Test
	public void testGetName() {
		assertEquals(DateUtils.CalendarToString(start), duty.getName());
	}

	@Test
	public void testGetPKID() {
		assertEquals(0, duty.getPKID());
	}

	@Test
	public void testGetPKIDStr() {
		assertEquals("1234", duty.getPKIDStr());
	}

	@Test
	public void testMatchesDB_ObjectBoolean() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testToStringArray() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetM_start() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSetM_start() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetM_end() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSetM_end() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetM_position() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSetM_position() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetM_person() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSetM_person() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetM_uuid() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSetM_uuid() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testPopulateProperitesResultSetDB_Factory() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testIsPersonOn() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testPersonConflict() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetTotalEmpoloyeeHours() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testMatchesStringStringString() {
		fail("Not yet implemented"); // TODO
	}*/

}
