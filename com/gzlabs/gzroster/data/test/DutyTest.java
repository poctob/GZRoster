package com.gzlabs.gzroster.data.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.gzroster.data.DB_Object;
import com.gzlabs.gzroster.data.Duty;
import com.gzlabs.gzroster.data.Person;
import com.gzlabs.gzroster.data.Position;
import com.gzlabs.utils.DateUtils;


public class DutyTest {

	Duty duty;
	
	Position position;
	Person person;
	
	ArrayList<String> d_properties;
	ArrayList<String> pos_properties;
	ArrayList<String> per_properties;
	ArrayList<Integer> positions;
	
	Calendar start;
	Calendar end;
	
	
	@Before
	public void setUp() throws Exception {
		position=new Position();
		pos_properties=new ArrayList<String>();
		pos_properties.add("2");
		pos_properties.add("Test Position");
		pos_properties.add("Test Note");
		position.populateProperties(pos_properties);
		
		person=new Person();
		per_properties=new ArrayList<String>();
		per_properties.add("2");
		per_properties.add("Test Name");
		per_properties.add("Test Address");
		per_properties.add("Test home phone");
		per_properties.add("Test mobile phone");
		per_properties.add("Test Note");
		per_properties.add("1");
		per_properties.add("Test Email");
		person.populateProperties(per_properties);
		
		positions=new ArrayList<Integer>();
		positions.add(1);
		positions.add(2);
		positions.add(3);
		person.setM_positions(positions);
		
		duty=new Duty();
		d_properties=new ArrayList<String>();
		start=new GregorianCalendar();
		end=new GregorianCalendar();
		end.add(Calendar.HOUR_OF_DAY,2);
		d_properties.add(DateUtils.DateToString(start.getTime()));
		d_properties.add(position.getName());
		d_properties.add(person.getName());
		d_properties.add(DateUtils.DateToString(end.getTime()));
		d_properties.add("");
		
		ArrayList<DB_Object> pos=new ArrayList<DB_Object>();
		pos.add(position);
		ArrayList<DB_Object> pers=new ArrayList<DB_Object>();
		pers.add(person);
		duty.populateProperties(d_properties, pos, pers);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInsert_sql() {
		String sql=duty.getInsert_sql(1);
		String uuid=duty.getM_uuid();
		String target="INSERT INTO DUTIES" +
				" (DUTY_START_TIME,PLACE_ID,PERSON_ID,DUTY_END_TIME,DUTY_KEY) " +
				"VALUES ('"+DateUtils.DateToString(start.getTime())+
						"','2','2','"+DateUtils.DateToString(end.getTime())+
								"','"+uuid+"')";
		
		assertEquals("Strings should be the same", target, sql);		
	}

	@Test
	public void testGetNexPKID_sql() {
		assertNull("Should not be implemented", duty.getNexPKID_sql());
	}

	@Test
	public void testGetUpdate_sql() {
		String uuid=duty.getM_uuid();
		String target="UPDATE DUTIES SET " +
				"DUTY_START_TIME='"+DateUtils.DateToString(start.getTime())+
				"',PLACE_ID='2',PERSON_ID='2'," +
				"DUTY_END_TIME='"+DateUtils.DateToString(end.getTime())+"' "+
				"WHERE DUTY_KEY='"+uuid+"'";
		String sql=duty.getUpdate_sql();
		assertEquals("Strings should be the same", target, sql);
	}

	@Test
	public void testGetDelete_sql() {
		String uuid=duty.getM_uuid();
		String target="DELETE FROM DUTIES WHERE DUTY_KEY='"+uuid+"'";
		ArrayList<String> sql=duty.getDelete_sql();
		assertEquals("Strings should be the same", target, sql.get(0));
	}

	@Test
	public void testGetName() {
		assertEquals("Strings should be the same", DateUtils.DateToString(start.getTime()), duty.getName());
	}

	@Test
	public void testGetPKID() {
		assertEquals("PKID should not be initialized", 0, duty.getPKID());
	}

	@Test
	public void testMatchesArrayListOfStringBoolean() {
		ArrayList<String> p_properties=new ArrayList<String>();
		start=new GregorianCalendar();
		String uuid=UUID.randomUUID().toString();
		
		p_properties.add(DateUtils.DateToString(start.getTime()));
		p_properties.add(position.getName());
		p_properties.add(person.getName());
		p_properties.add(DateUtils.DateToString(end.getTime()));
		p_properties.add("");
		
		assertTrue("Arrays should match", duty.matches(p_properties, false));
		assertTrue("Arrays should match", duty.matches(p_properties, true));
		
		duty.setM_uuid(uuid);
		p_properties.set(4, uuid);
		assertTrue("Arrays should match", duty.matches(p_properties, true));
	}

	@Test
	public void testToSortedArray() {
		d_properties.set(1,Integer.toString(position.getPKID()));
		d_properties.set(2,Integer.toString(person.getPKID()));
		assertEquals("Strings should be the same", d_properties,duty.toSortedArray());
	}

	@Test
	public void testSetM_start() {
		duty.setM_start(start.getTime());
		assertEquals("Strings should be the same",start.getTime(),duty.getM_start());
	}

	@Test
	public void testSetM_end() {
		duty.setM_end(end.getTime());
		assertEquals("Strings should be the same",end.getTime(),duty.getM_end());
	}

	@Test
	public void testSetM_position() {
		duty.setM_position(position);
		assertEquals("Objects should be the same",position,duty.getM_position());
	}

	@Test
	public void testSetM_person() {
		duty.setM_person(person);
		assertEquals("Objects should be the same",person,duty.getM_person());
	}

	@Test
	public void testSetM_uuid() {
		String uuid=UUID.randomUUID().toString();
		duty.setM_uuid(uuid);
		assertEquals("Objects should be the same",uuid,duty.getM_uuid());
	}

	@Test
	public void testIsPersonOn() {
		int person_id=duty.isPersonOn(2, DateUtils.DateToString(start.getTime()));
		assertEquals("Objects should be the same",person.getPKID(),person_id);
		
	}

	@Test
	public void testPersonConflict() {
		Calendar td=new GregorianCalendar();
		td.add(Calendar.HOUR_OF_DAY,1);
		String str_cal=DateUtils.DateToString(td.getTime());
		boolean conflict=duty.personConflict(2, str_cal, str_cal);
		assertTrue("Should be conlicting", conflict);
		
		td.add(Calendar.HOUR_OF_DAY,4);
		str_cal=DateUtils.DateToString(td.getTime());
		conflict=duty.personConflict(2, str_cal, str_cal);
		assertFalse("Should be conlicting", conflict);
	}

	@Test
	public void testGetTotalEmpoloyeeHours() {
		Calendar td=new GregorianCalendar();
		td.add(Calendar.HOUR_OF_DAY,-1);
		Calendar td2=new GregorianCalendar();
		td2.add(Calendar.HOUR_OF_DAY,3);
		int hours=(int)duty.getTotalEmpoloyeeHours(2, DateUtils.DateToString(td.getTime()), DateUtils.DateToString(td2.getTime()));
		assertEquals("Hours should be 2", 2, hours);
	}

	@Test
	public void testMatchesIntIntString() {
		Calendar td=new GregorianCalendar();
		td.add(Calendar.HOUR_OF_DAY,-1);
		boolean matched=duty.matches(2, 2, DateUtils.DateToString(td.getTime()));
		assertFalse("Should not match", matched);
		td.add(Calendar.HOUR_OF_DAY,2);
		matched=duty.matches(2, 2, DateUtils.DateToString(td.getTime()));
		assertTrue("Should match", matched);
	}

}
