package com.gzlabs.gzroster.data.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.gzroster.data.DataManager;
import com.gzlabs.gzroster.data.Person;
import com.gzlabs.gzroster.data.Position;
import com.gzlabs.gzroster.data.Privilege;
import com.gzlabs.gzroster.data.TimeOff;
import com.gzlabs.gzroster.sql.DBObjectType;
import com.gzlabs.gzroster.sql.Tables;
import com.gzlabs.utils.DateUtils;

public class PersonTest {

	Person person;
	@Before
	public void setUp() throws Exception {
		Position position=new Position( "Test Position",  "Test Note", 100);
		Privilege privilege=new Privilege("Test Name", 100);
		ArrayList<String> positions=new ArrayList<String>();
		ArrayList<String> privileges=new ArrayList<String>();
		positions.add(position.getName());
		privileges.add(privilege.getName());
		
		Calendar start=new GregorianCalendar();
		Calendar end=new GregorianCalendar();
		end.set(Calendar.DATE, end.get(Calendar.DATE)+1);
		TimeOff timeoff=new TimeOff(start, end, "Pending", "Test Person");
		
		ArrayList<TimeOff> timesoff=new ArrayList<TimeOff>();
		timesoff.add(timeoff);
		
		person=new Person("Test Person","Test Address","23204230420","4732752399743",true,"TEst email@",100,timesoff, positions, privileges);
	} 

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInsert_sql() {
		assertEquals(Tables.PROC_INSERT_PERSON, person.getInsert_sql());
	}

	@Test
	public void testGetUpdate_sql() {
		assertEquals(Tables.PROC_UPDATE_PERSON, person.getUpdate_sql());
	}

	@Test
	public void testGetDelete_sql() {
		assertEquals(Tables.PROC_DELETE_PERSON, person.getDelete_sql());
	}

	@Test
	public void testPopulateProperites() {
		Position position2=new Position( "Test Position2",  "Test Note", 100);
		Privilege privilege2=new Privilege("Test Name2", 200);
		ArrayList<String> positions2=new ArrayList<String>();
		ArrayList<String> privileges2=new ArrayList<String>();
		positions2.add(position2.getName());
		privileges2.add(privilege2.getName());
		
		Calendar start=new GregorianCalendar();
		Calendar end=new GregorianCalendar();
		end.set(Calendar.DATE, end.get(Calendar.DATE)+1);
		TimeOff timeoff2=new TimeOff(start, end, "Pending", "Test Person2");
		
		ArrayList<TimeOff> timesoff2=new ArrayList<TimeOff>();
		timesoff2.add(timeoff2);
		Person person2=new Person("Test Person2","Test Address2","232042304202","47327523997432",true,"TEst email@2",200,timesoff2, positions2, privileges2);
		person.populateProperties(person2);
		assertTrue(person.matches(person2, true));
	}

	@Test
	public void testPopulateProperties() {
		TestCommon tc=new TestCommon();
		DataManager dman=tc.getDman();
		dman.addRecord(person, DBObjectType.PERSON);
		Object priv2=dman.getElementDetails("Test Person", DBObjectType.PERSON);
		assertNotNull(priv2);
		assertEquals("Test Person",((Person)priv2).getName());
		dman.deleteRecord(person, DBObjectType.PERSON);
	}

	@Test
	public void testGetName() {
		assertEquals("Test Person", person.getName());
	}

	@Test
	public void testGetPKID() {
		assertEquals(100, person.getPKID());
	}

	@Test
	public void testGetPKIDStr() {
		assertEquals("100", person.getPKIDStr());
	}

	@Test
	public void testToStringArray() {
		String [] array=person.toStringArray();
		assertTrue(array.length==7);
		assertEquals(array[0], "Test Person");
		assertEquals(array[1], "Test Address");
		assertEquals(array[2], "23204230420");
		assertEquals(array[3], "4732752399743");
		assertEquals(array[4], "1");
		assertEquals(array[5], "TEst email@");
		assertEquals(array[6], "100");
	}

	@Test
	public void testIsActive() {
		assertTrue(person.isActive());
	}

	@Test
	public void testGetM_positions() {
		ArrayList<String> positions=person.getM_positions();
		assertTrue(positions.size()==1);
		assertEquals("Test Position", positions.get(0));
	}

	@Test
	public void testSetM_name() {
		person.setM_name("Test Person2");
		assertEquals("Test Person2", person.getName());
	}

	@Test
	public void testGetM_address() {
		assertEquals("Test Address", person.getM_address());
	}

	@Test
	public void testGetM_home_phone() {
		assertEquals("23204230420", person.getM_home_phone());
	}

	@Test
	public void testGetM_mobile_phone() {
		assertEquals("4732752399743", person.getM_mobile_phone());
	}

	@Test
	public void testGetM_email() {
		assertEquals("TEst email@", person.getM_email());
	}

	@Test
	public void testGetM_privileges() {
		ArrayList<String> privileges=person.getM_privileges();
		assertTrue(privileges.size()==1);
		assertEquals("Test Name", privileges.get(0));
	}

	@Test
	public void testSetM_positions() {
		Position position2=new Position( "Test Position2",  "Test Note", 100);		
		ArrayList<String> positions2=new ArrayList<String>();
		positions2.add(position2.getName());
		person.setM_positions(positions2);
		ArrayList<String> positions=person.getM_positions();
		assertTrue(positions.size()==1);
		assertEquals("Test Position2", positions.get(0));
	}

	@Test
	public void testSetM_privileges() {
		Privilege position2=new Privilege( "Test Position2", 100);		
		ArrayList<String> positions2=new ArrayList<String>();
		positions2.add(position2.getName());
		person.setM_privileges(positions2);
		ArrayList<String> positions=person.getM_privileges();
		assertTrue(positions.size()==1);
		assertEquals("Test Position2", positions.get(0));
	}

	@Test
	public void testIsPositionAllowed() {
		assertTrue(person.isPositionAllowed("Test Position"));
	}

	@Test
	public void testPopulateTimeOff() {
		TestCommon tc=new TestCommon();
		DataManager dman=tc.getDman();
		
		Calendar start=new GregorianCalendar();
		Calendar end=new GregorianCalendar();
		start.set(Calendar.DATE, start.get(Calendar.DATE)+1);
		end.set(Calendar.DATE, end.get(Calendar.DATE)+2);
		TimeOff timeoff=new TimeOff(start, end, "Pending", person.getName());
		
		ArrayList<Object> timesoff=new ArrayList<Object>();
		dman.addRecord(person, DBObjectType.PERSON);
		timesoff.add(timeoff);
		dman.updateTimesOff(timesoff);
		
		Object priv2=dman.getElementDetails("Test Person", DBObjectType.PERSON);
		ArrayList<TimeOff> toffs=((Person)priv2).getTimeOffs();
		assertEquals(1,toffs.size());
		TimeOff toff=toffs.get(0);
		assertEquals(timeoff.getEndStr(), toff.getEndStr());
		assertEquals(timeoff.getStartStr(), toff.getStartStr());
		assertEquals(timeoff.getName(), toff.getName());
		assertEquals(timeoff.getStatus(), toff.getStatus());
		
		timesoff.clear();
		dman.updateTimesOff(timesoff);
		dman.deleteRecord(person, DBObjectType.PERSON);
		
	}

	@Test
	public void testGetTimesOff() {
		TestCommon tc=new TestCommon();
		DataManager dman=tc.getDman();
		
		Calendar start=new GregorianCalendar();
		Calendar end=new GregorianCalendar();
		start.set(Calendar.DATE, start.get(Calendar.DATE)+1);
		end.set(Calendar.DATE, end.get(Calendar.DATE)+2);
		TimeOff timeoff=new TimeOff(start, end, "Pending", person.getName());
		ArrayList<Object> timesoff=new ArrayList<Object>();
		dman.addRecord(person, DBObjectType.PERSON);
		timesoff.add(timeoff);
		dman.updateTimesOff(timesoff);
		
		String str_to = "From:";
		str_to += timeoff.getStartStr();
		str_to += " To:";
		str_to += timeoff.getEndStr();
		
		Object priv2=dman.getElementDetails("Test Person", DBObjectType.PERSON);
		ArrayList<String> toffs=((Person)priv2).getTimesOff();
		assertEquals(1,toffs.size());
		assertEquals(toffs.get(0),str_to);
		
		timesoff.clear();
		dman.updateTimesOff(timesoff);
		dman.deleteRecord(person, DBObjectType.PERSON);
	}

	@Test
	public void testIsTimeAllowed() {
		TestCommon tc=new TestCommon();
		DataManager dman=tc.getDman();
		
		Calendar start=new GregorianCalendar();
		Calendar end=new GregorianCalendar();
		start.set(Calendar.DATE, start.get(Calendar.DATE)+1);
		end.set(Calendar.DATE, end.get(Calendar.DATE)+2);
		TimeOff timeoff=new TimeOff(start, end, "Approved", person.getName());
		ArrayList<Object> timesoff=new ArrayList<Object>();
		dman.addRecord(person, DBObjectType.PERSON);
		timesoff.add(timeoff);
		dman.updateTimesOff(timesoff);
		
		Object priv2=dman.getElementDetails("Test Person", DBObjectType.PERSON);
		
		Calendar start1=new GregorianCalendar();
		Calendar end1=new GregorianCalendar();
		
		assertTrue(((Person)priv2).isTimeAllowed(DateUtils.CalendarToString(start1), DateUtils.CalendarToString(end1)));
		start1.set(Calendar.DATE, start.get(Calendar.DATE));
		start1.set(Calendar.HOUR, start.get(Calendar.HOUR)+1);
		end1=new GregorianCalendar();
		end1.set(Calendar.DATE, start.get(Calendar.DATE));
		end1.set(Calendar.HOUR, start.get(Calendar.HOUR)+2);
		assertFalse(((Person)priv2).isTimeAllowed(DateUtils.CalendarToString(start1), DateUtils.CalendarToString(end1)));
		timesoff.clear();
		dman.updateTimesOff(timesoff);
		dman.deleteRecord(person, DBObjectType.PERSON);
	}

}
