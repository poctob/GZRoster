package com.gzlabs.gzroster.data.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.gzroster.data.TimeOff;
import com.gzlabs.utils.DateUtils;

public class TimeOffTest {

	Date start;
	Date end;
	TimeOff timeoff;
	@Before
	public void setUp() throws Exception {
		start=new Date();
		end = new Date();
		end.setDate(end.getDate()+1);
		timeoff=new TimeOff(start, end, "Pending","Test Subject 1");
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetStart() {
		assertEquals("Start date should be equal", start, timeoff.getStart());
	}

	@Test
	public void testSetStart() {
		Date newdate=new Date();
		timeoff.setStart(newdate);
		assertEquals("Start date should be equal", newdate, timeoff.getStart());
	}

	@Test
	public void testGetEnd() {
		assertEquals("Start date should be equal", end, timeoff.getEnd());
	}

	@Test
	public void testSetEnd() {
		Date newdate=new Date();
		timeoff.setEnd(newdate);
		assertEquals("Start date should be equal", newdate, timeoff.getEnd());
	}

	@Test
	public void testGetStartStr() {
		assertEquals("Start date should be equal", DateUtils.DateToString(start), timeoff.getStartStr());
	}

	@Test
	public void testGetEndStr() {
		assertEquals("Start date should be equal", DateUtils.DateToString(end), timeoff.getEndStr());
	}

	@Test
	public void testIsConflicting() {
		assertTrue("Should be conflicting", timeoff.isConflicting(DateUtils.DateToString(new Date()), DateUtils.DateToString(new Date())));
		
		Date prior=new Date();
		prior.setDate(prior.getDate()-1);
		assertFalse("Should not be conflicting", timeoff.isConflicting(DateUtils.DateToString(prior), DateUtils.DateToString(prior)));
	}

}
