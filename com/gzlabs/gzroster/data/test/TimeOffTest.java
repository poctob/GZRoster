package com.gzlabs.gzroster.data.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.gzroster.data.TimeOff;
import com.gzlabs.utils.DateUtils;

public class TimeOffTest {

	TimeOff timeoff;
	Calendar start;
	Calendar end;
	@Before
	public void setUp() throws Exception {
		start=new GregorianCalendar();
		end=new GregorianCalendar();
		end.set(Calendar.DATE, end.get(Calendar.DATE)+1);
		timeoff=new TimeOff(start, end, "Pending", "Test Person");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetName() {
		assertEquals("Test Person",timeoff.getName());
	}

	@Test
	public void testSetName() {
		timeoff.setName("Test Name 2");
		assertEquals("Test Name 2",timeoff.getName());
		timeoff.setName(null);
		assertNull(timeoff.getName());
	}

	@Test
	public void testGetStart() {
		assertTrue(start.equals(timeoff.getStart()));
	}

	@Test
	public void testSetStartDate() {
		Calendar newstart=new GregorianCalendar();
		newstart.set(Calendar.YEAR, newstart.get(Calendar.YEAR+1));
		timeoff.setStart(newstart);
		assertTrue(newstart.equals(timeoff.getStart()));
	}

	@Test
	public void testGetEnd() {
		assertTrue(end.equals(timeoff.getEnd()));
	}

	@Test
	public void testSetEndDate() {
		Calendar newend=new GregorianCalendar();
		newend.set(Calendar.YEAR, newend.get(Calendar.YEAR+1));
		timeoff.setEnd(newend);
		assertTrue(newend.equals(timeoff.getEnd()));
	}

	@Test
	public void testSetStatus() {
		timeoff.setStatus("Approved");
		assertEquals("Approved", timeoff.getStatus());
	}

	@Test
	public void testGetStartStr() {
		assertEquals(DateUtils.CalendarToString(start), timeoff.getStartStr());
	}

	@Test
	public void testGetEndStr() {
		assertEquals(DateUtils.CalendarToString(end), timeoff.getEndStr());
	}

	@Test
	public void testSetStartString() {
		Calendar newstart=new GregorianCalendar();
		newstart.set(Calendar.YEAR, newstart.get(Calendar.YEAR+1));
		timeoff.setStart(DateUtils.CalendarToString(newstart));
		newstart.set(Calendar.MILLISECOND, 0);
		assertTrue(newstart.equals(timeoff.getStart()));
	}

	@Test
	public void testSetEndString() {
		Calendar newend=new GregorianCalendar();
		newend.set(Calendar.YEAR, newend.get(Calendar.YEAR+1));
		timeoff.setEnd(DateUtils.CalendarToString(newend));
		newend.set(Calendar.MILLISECOND, 0);
		assertTrue(newend.equals(timeoff.getEnd()));
	}
	
	@Test
	public void testIsConflicting() {
		Calendar newstart=new GregorianCalendar();
		newstart.set(Calendar.HOUR, newstart.get(Calendar.HOUR)+1);
		Calendar newend=new GregorianCalendar();
		newend.set(Calendar.HOUR, newend.get(Calendar.HOUR)+2);
		timeoff.setStatus("Denied");
		assertFalse(timeoff.isConflicting(DateUtils.CalendarToString(newstart), DateUtils.CalendarToString(newend)));
		timeoff.setStatus("Approved");
		assertTrue(timeoff.isConflicting(DateUtils.CalendarToString(newstart), DateUtils.CalendarToString(newend)));
		assertTrue(timeoff.isConflicting(DateUtils.CalendarToString(newstart), null));
		assertTrue(timeoff.isConflicting(null, DateUtils.CalendarToString(newstart)));
		assertFalse(timeoff.isConflicting(null, null));
		assertTrue(timeoff.isConflicting(DateUtils.CalendarToString(newend), DateUtils.CalendarToString(newstart)));
		newstart.set(Calendar.HOUR, newstart.get(Calendar.HOUR)-5);
		newend.set(Calendar.HOUR, newend.get(Calendar.HOUR)-3);
		assertFalse(timeoff.isConflicting(DateUtils.CalendarToString(newstart), DateUtils.CalendarToString(newend)));
		assertFalse(timeoff.isConflicting(DateUtils.CalendarToString(newstart), null));
		assertFalse(timeoff.isConflicting(null, DateUtils.CalendarToString(newstart)));
	}

}
