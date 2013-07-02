package com.gzlabs.gzroster.data.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.gzroster.data.DataManager;
import com.gzlabs.gzroster.gui.IDisplayStatus;
import com.gzlabs.utils.DateUtils;


public class DataManagerTest implements IDisplayStatus {

	DataManager dman;
	private static final String CONFIG_FILE_PATH = "GZRoster.config";
	@Before
	public void setUp() throws Exception {
		dman=new DataManager(this);
	}

	@After
	public void tearDown() throws Exception {
		deleteTimeOffRequest();
		deleteDuty();
		deleteEmployee();
		deletePosition();
	}

	@Test
	public void testDataManager() {
		dman=new DataManager(this);
		assertNotNull("object should be created", dman);
	}

	@Test
	public void testGetEmployees() {
		ArrayList<String> results=dman.getEmployees();
		assertTrue("There should be data", results.size()>0);
	}

	@Test
	public void testGetEmployeeDetails() {
		ArrayList<String> results=dman.getEmployees();
		ArrayList<String> details=dman.getEmployeeDetails(results.get(0));
		assertTrue("There should be data", details.size()>0);
		details=dman.getEmployeeDetails(null);
		assertNull("There should be no data", details);
	}

	@Test
	public void testGetPositions() {
		ArrayList<String> results=dman.getPositions();
		assertTrue("There should be data", results.size()>0);
	}

	@Test
	public void testGetPersonToPosMapping() {
		ArrayList<String> results=dman.getEmployees();
		ArrayList<String> details=dman.getPersonToPosMapping(results.get(0));
		assertTrue("There should be data", details.size()>0);
		details=dman.getPersonToPosMapping(null);
		assertNull("There should be no data", details);
	}

	@Test
	public void testGetPositionDetailsByName() {
		ArrayList<String> results=dman.getPositions();
		ArrayList<String> details=dman.getPositionDetailsByName(results.get(0));
		assertTrue("There should be data", details.size()>0);
		details=dman.getPositionDetailsByName(null);
		assertNull("There should be no data", details);
	}

	@Test
	public void testGetDuties() {
		ArrayList<String> results=dman.getDuties();
		assertTrue("There should be data", results.size()>0);
	}
	
	@Test
	public void testAddPosition() {			
		ArrayList<String> positions=dman.getPositions();
		dman.addPosition(null);
		ArrayList<String> newpositions=dman.getPositions();
		assertEquals("Objects should be the same", positions, newpositions);
		
		addPosition();
		newpositions=dman.getPositions();
		assertEquals("There should be one more person", positions.size()+1, newpositions.size());
		deletePosition();
	}
	
	
	@Test
	public void testAddEmployee() {
		
		
		ArrayList<String> persons=dman.getEmployees();
		
		dman.addEmployee(null, null);
		ArrayList<String> newpersons=dman.getEmployees();
		
		assertEquals("Objects should be the same", persons, newpersons);
		
		addEmployee();
		newpersons=dman.getEmployees();
		assertEquals("There should be one more person", persons.size()+1, newpersons.size());
		deleteEmployee();
		
	}
	
	
	@Test
	public void testAddDuty() {
		addPosition();
		addEmployee();
		
		ArrayList<String> duties=dman.getDuties();
	
		
		dman.addDuty(null);
		ArrayList<String> newresults=dman.getDuties();
		
		assertEquals("Objects should be the same", duties, newresults);
		
		addDuty();
		newresults=dman.getDuties();
		assertEquals("There should be one more object", duties.size()+1, newresults.size());
		deleteDuty();
		deleteEmployee();
		deletePosition();
	}
	

	
	@Test
	public void testIsDutyOn() {

		addPosition();
		addEmployee();
		addDuty();
		ArrayList<String> result=dman.isDutyOn(null, null);
		assertNull("There should be no data", result);
		
		Calendar start=new GregorianCalendar();
		start.set(2013, 1, 1, 1, 0, 0);
		result=dman.isDutyOn(DateUtils.DateToString(start.getTime()), "Test Position 123");
		assertEquals("The person should be on", "Test Name 123", result.get(0));
		deleteDuty();
		deleteEmployee();
		deletePosition();
	
	}
	
	
	@Test
	public void testGetDutyStart() {
		addPosition();
		addEmployee();
		addDuty();
		Calendar start=new GregorianCalendar();
		start.set(2013, 1, 1, 1, 0, 0);
		String datestring=DateUtils.DateToString(start.getTime());
		String result=dman.getDutyStart(null, null, null);
		assertNull("Result should be null", result);
		result=dman.getDutyStart("Test Name 123", "Test Position 123", datestring);
		assertEquals("Start date should equal", datestring, result);
		deleteDuty();
		deleteEmployee();
		deletePosition();
	}

	
	@Test
	public void testGetDutyEnd() {
		addPosition();
		addEmployee();
		addDuty();
		Calendar start=new GregorianCalendar();
		start.set(2013, 1, 1, 3, 0, 0);
		String datestring=DateUtils.DateToString(start.getTime());
		String result=dman.getDutyEnd(null, null, null);
		assertNull("Result should be null", result);
		result=dman.getDutyEnd("Test Name 123", "Test Position 123", datestring);
		assertEquals("Start date should equal", datestring, result);
		deleteDuty();
		deleteEmployee();
		deletePosition();
	}

	@Test
	public void testGetCellLabelString() {
		addPosition();
		addEmployee();
		addDuty();
		String result=dman.getCellLabelString("02:00", "Test Position 123", "2013-02-01");
		assertEquals("Person name should equal", "[Test Name 123]", result);
		result=dman.getCellLabelString(null, null, null);
		assertNull("Result should be null", result);
		deleteDuty();
		deleteEmployee();
		deletePosition();
	}
	
	

	@Test
	public void testGetTimeSpan() {
		ArrayList<String> results=dman.getTimeSpan();
		assertTrue("There should be data", results.size()>0);
		
	}

	@Test
	public void testGetProp() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(CONFIG_FILE_PATH));
			Properties result=dman.getProp();
			assertEquals("Objects should equal", prop, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCheckDutyConflict() {
		addPosition();
		addEmployee();
		addDuty();
		Calendar start=new GregorianCalendar();
		start.set(2013, 1, 1, 1, 0, 0);
		Calendar end=new GregorianCalendar();
		end.set(2013, 1, 1, 3, 0, 0);
		
		boolean result=dman.checkDutyConflict(null, null, null);
		assertFalse("Result should be false", result);
		result=dman.checkDutyConflict("Test Name 123", 
				DateUtils.DateToString(start.getTime()), 
				DateUtils.DateToString(end.getTime()));
		assertTrue("Result should be true", result);
		deleteDuty();
		deleteEmployee();
		deletePosition();
	}

	@Test
	public void testGetTotalEmpoloyeeHours() {
		addPosition();
		addEmployee();
		addDuty();
		String result=dman.getTotalEmpoloyeeHours(null, null, null);
		assertEquals("Objects should be empty", "0.00",result);
		Calendar start=new GregorianCalendar();
		start.set(2013, 1, 1, 1, 0, 0);
		Calendar end=new GregorianCalendar();
		end.set(2013, 1, 1, 3, 0, 0);
		
		result=dman.getTotalEmpoloyeeHours("Test Name 123", 
				DateUtils.DateToString(start.getTime()), 
				DateUtils.DateToString(end.getTime()));
		assertEquals("Objects should equal", "2.00", result);
		deleteDuty();
		deleteEmployee();
		deletePosition();
	}

	@Test
	public void testNewTimeOffRequest() {
		addPosition();
		addEmployee();
		addDuty();
		
		boolean result=dman.newTimeOffRequest(null, null, null);
		assertFalse("Result should be false", result);
		
		result=addTimeOffRequest();
		
		assertTrue("Result should be true", result);
		deleteTimeOffRequest();
		deleteDuty();
		deleteEmployee();
		deletePosition();
		
	}
	
	@Test
	public void testGetAllowedEmployees() {

		addPosition();
		addEmployee();
		addDuty();
		addTimeOffRequest();
		Calendar start=new GregorianCalendar();
		start.set(2013, 1, 1, 4, 0, 0);
		Calendar end=new GregorianCalendar();
		end.set(2013, 1, 1, 5, 0, 0);
		ArrayList<String> result=dman.getAllowedEmployees(null, null, null);
		assertEquals("Objects should be empty", 0, result.size());
		
		result=dman.getAllowedEmployees("Test Position 123", 
				DateUtils.DateToString(start.getTime()), 
				DateUtils.DateToString(end.getTime()));
		
		assertEquals("Objects should be empty", 0, result.size());
		
		start.set(2013, 1, 1, 6, 0, 0);
		end.set(2013, 1, 1, 7, 0, 0);
		
		result=dman.getAllowedEmployees("Test Position 123", 
				DateUtils.DateToString(start.getTime()), 
				DateUtils.DateToString(end.getTime()));
		assertEquals("Objects should equal", "Test Name 123", result.get(0));
		deleteTimeOffRequest();
		deleteDuty();
		deleteEmployee();
		deletePosition();
	}
	

	@Test
	public void testGetTimeOff() {
		addPosition();
		addEmployee();
		addDuty();
		addTimeOffRequest();
		ArrayList<String> result=dman.getTimeOff(null);
		assertNull("result should be null", result);
		result=dman.getTimeOff("Test Name 123");
		assertTrue("There should be data",result.size()>0);
		deleteTimeOffRequest();
		deleteDuty();
		deleteEmployee();
		deletePosition();
	}

	@Test
	public void testDeleteTimeOffRequest() {
		addPosition();
		addEmployee();
		addDuty();
		addTimeOffRequest();
	
		boolean result=dman.deleteTimeOffRequest(null, null, null);
		assertFalse("Result should be false", result);
		
		result=deleteTimeOffRequest();
		
		assertTrue("Result should be true", result);
		deleteDuty();
		deleteEmployee();
		deletePosition();
	}
	@Test
	public void testDeleteDuty() {
		addPosition();
		addEmployee();
		addDuty();
		ArrayList<String> duties=dman.getDuties();
		
		dman.deleteDuty(null);
		ArrayList<String> newresults=dman.getDuties();
		
		assertEquals("Objects should be the same", duties, newresults);
		
		deleteDuty();
		newresults=dman.getDuties();
		assertEquals("There should be one less object", duties.size()-1, newresults.size());
		deleteEmployee();
		deletePosition();
	}
	
	@Test
	public void testUpdateEmployee() {
		addEmployee();
		ArrayList<String> properties=new ArrayList<String>();
		properties.add("");
		properties.add("Test Name 123");
		properties.add("Test Address");
		properties.add("Test phone");
		properties.add("Test mphone");
		properties.add("Test Note");
		properties.add("1");
		properties.add("Test Email");
		
		ArrayList<String> properties2=new ArrayList<String>();
		properties2.add("");
		properties2.add("Test Name 12356");
		properties2.add("Test Address");
		properties2.add("Test phone");
		properties2.add("Test mphone");
		properties2.add("Test Note");
		properties2.add("1");
		properties2.add("Test Email");
		
		
		ArrayList<String> positions=dman.getPositions();
		ArrayList<String> persons=dman.getEmployees();
		
		dman.updateEmployee(null, null,null);
		ArrayList<String> newpersons=dman.getEmployees();
		
		assertEquals("Objects should be the same", persons, newpersons);
		
		dman.updateEmployee(properties, properties2, positions);
		newpersons=dman.getEmployees();
		assertEquals("Person name should have been changed", "Test Name 12356", newpersons.get(newpersons.size()-1));
		dman.updateEmployee(properties2, properties, positions);
		deleteEmployee();
	}

	@Test
	public void testUpdatePosition() {
		addPosition();
		ArrayList<String> properties=new ArrayList<String>();
		properties.add("");
		properties.add("Test Position 123");
		properties.add("Test Note");
		
		ArrayList<String> properties2=new ArrayList<String>();
		properties2.add("");
		properties2.add("Test Position 12345");
		properties2.add("Test Note");
		
		
		ArrayList<String> positions=dman.getPositions();
		dman.updatePosition(null, null);
		ArrayList<String> newpositions=dman.getPositions();
		assertEquals("Objects should be the same", positions, newpositions);
		
		dman.updatePosition(properties, properties2);
		newpositions=dman.getPositions();
		assertEquals("Person name should have been changed", "Test Position 12345", newpositions.get(newpositions.size()-1));
		dman.updatePosition(properties2, properties);
		deletePosition();
	}
	
	@Test
	public void testDeleteEmployee() {
		addEmployee();
		ArrayList<String> persons=dman.getEmployees();
		dman.deleteEmployee(null);
		ArrayList<String> newpersons=dman.getEmployees();		
		assertEquals("Objects should be the same", persons, newpersons);
		
		deleteEmployee();
		newpersons=dman.getEmployees();
		assertEquals("There should be one less object", persons.size()-1, newpersons.size());
	}

	@Test
	public void testDeletePosition() {
		addPosition();
		ArrayList<String> positions=dman.getPositions();
		dman.deletePosition(null);
		ArrayList<String> newpositions=dman.getPositions();	
		assertEquals("Objects should be the same", positions, newpositions);
				
		deletePosition();
		newpositions=dman.getPositions();	
		assertEquals("There should be one less object", positions.size()-1, newpositions.size());
	}
	

	@Override
	public void DisplayStatus(String status) {
		System.out.println(status);
		
	}

	@Override
	public void ShowErrorBox(String error) {
		System.out.println(error);
		
	}
	
	public void addPosition() {	
		ArrayList<String> properties=new ArrayList<String>();
		properties.add("");
		properties.add("Test Position 123");
		properties.add("Test Note");
		dman.addPosition(properties);
	}
	
	public void deletePosition() {
		String [] pers={"Test Position 123"};
		dman.deletePosition(pers);	
	}
	
	public void addEmployee() {
		ArrayList<String> properties=new ArrayList<String>();
		properties.add("");
		properties.add("Test Name 123");
		properties.add("Test Address");
		properties.add("Test phone");
		properties.add("Test mphone");
		properties.add("Test Note");
		properties.add("1");
		properties.add("Test Email");
		
		ArrayList<String> positions=dman.getPositions();
		dman.addEmployee(properties, positions);		
	}
	
	public void deleteEmployee() {
		String [] pers={"Test Name 123"};
		dman.deleteEmployee(pers);
		
	}
	
	public void deleteDuty() {
		ArrayList<String> d_properties=new ArrayList<String>();
		Calendar start=new GregorianCalendar();
		start.set(2013, 1, 1, 1, 0, 0);
		Calendar end=new GregorianCalendar();
		end.set(2013, 1, 1, 3, 0, 0);
		d_properties.add(DateUtils.DateToString(start.getTime()));
		d_properties.add("Test Position 123");
		d_properties.add("Test Name 123");
		d_properties.add(DateUtils.DateToString(end.getTime()));
		d_properties.add("");

		dman.deleteDuty(d_properties);

	}
	
	public void addDuty() {
		ArrayList<String> d_properties=new ArrayList<String>();
		Calendar start=new GregorianCalendar();
		start.set(2013, 1, 1, 1, 0, 0);
		Calendar end=new GregorianCalendar();
		end.set(2013, 1, 1, 3, 0, 0);
		d_properties.add(DateUtils.DateToString(start.getTime()));
		d_properties.add("Test Position 123");
		d_properties.add("Test Name 123");
		d_properties.add(DateUtils.DateToString(end.getTime()));
		d_properties.add("");

		dman.addDuty(d_properties);
	}
	
	public boolean addTimeOffRequest() {

		Calendar start=new GregorianCalendar();
		start.set(2013, 1, 1, 4, 0, 0);
		Calendar end=new GregorianCalendar();
		end.set(2013, 1, 1, 5, 0, 0);

		return dman.newTimeOffRequest( 
				DateUtils.DateToString(start.getTime()), 
				DateUtils.DateToString(end.getTime()),
				"Test Name 123");
	}
	
	public boolean deleteTimeOffRequest() {
		Calendar start=new GregorianCalendar();
		start.set(2013, 1, 1, 4, 0, 0);
		Calendar end=new GregorianCalendar();
		end.set(2013, 1, 1, 5, 0, 0);
	
		return dman.deleteTimeOffRequest( 
				DateUtils.DateToString(start.getTime()), 
				DateUtils.DateToString(end.getTime()),
				"Test Name 123");
	}
	

}
