package com.gzlabs.gzroster.sql.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gzlabs.gzroster.sql.QueryFactory;

public class QueryFactoryTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInsert() {
		String target="INSERT INTO %FROM% (%COLS%) VALUES (%VALS%)";
		String sql=QueryFactory.getInsert(null, null, null);
		assertEquals("Strings should equal", target, sql);
		String cols="Col1, Col2";
		String vals="Val1, Val2";
		String table="Test table";
		target="INSERT INTO "+table+" ("+cols+") VALUES ("+vals+")";
		sql=QueryFactory.getInsert(cols, vals, table);
		assertEquals("Strings should equal", target, sql);
	}

	@Test
	public void testGetDeleteStringStringString() {
		String target="DELETE FROM %FROM% WHERE null='null'";
		String nullstr=null;
		String sql=QueryFactory.getDelete(nullstr, null, null);
		assertEquals("Strings should equal", target, sql);
		String cols="Col1";
		String vals="Val1";
		String table="TestTable";
		target="DELETE FROM "+table+" WHERE "+cols+"='"+vals+"'";
		sql=QueryFactory.getDelete(cols, vals, table);
		assertEquals("Strings should equal", target, sql);		
	}

	@Test
	public void testGetDeleteStringIntString() {
		String target="DELETE FROM %FROM% WHERE null='0'";		
		String sql=QueryFactory.getDelete(null, 0, null);
		assertEquals("Strings should equal", target, sql);
		String cols="Col1";
		int vals=0;
		String table="TestTable";
		target="DELETE FROM "+table+" WHERE "+cols+"='"+vals+"'";
		sql=QueryFactory.getDelete(cols, vals, table);
		assertEquals("Strings should equal", target, sql);		
	}

	@Test
	public void testGetDeleteArrayListOfStringArrayListOfStringString() {
		String target="DELETE FROM %FROM% WHERE null='null'";
		ArrayList<String> nullstr=null;
		String sql=QueryFactory.getDelete(nullstr, null, null);
		assertNull("String should be null", sql);
		ArrayList<String> cols=new ArrayList<String>();
		cols.add("Col1");
		cols.add("Col2");
		ArrayList<String> vals=new ArrayList<String>();
		vals.add("Val1");
		vals.add("val2");		
		String table="TestTable";
		target="DELETE FROM "+table+" WHERE Col1='Val1' AND Col2='val2'";
		sql=QueryFactory.getDelete(cols, vals, table);
		assertEquals("Strings should equal", target, sql);		
	}

	@Test
	public void testGetUpdateStringStringStringString() {
		String target="UPDATE %FROM% SET %WHAT% WHERE null='null'";
		String nullstr=null;
		String sql=QueryFactory.getUpdate(nullstr, null, null, null);
		assertEquals("Strings should equal", target, sql);
		String cols="Col1";
		String vals="Val1";
		String table="TestTable";
		String what="Col2=Val2";
		target="UPDATE "+table+" SET "+what+" WHERE "+cols+"='"+vals+"'";
		sql=QueryFactory.getUpdate(what, cols, vals, table);
		assertEquals("Strings should equal", target, sql);	
	}

	@Test
	public void testGetUpdateStringStringIntString() {
		String target="UPDATE %FROM% SET %WHAT% WHERE null='0'";
		String nullstr=null;
		String sql=QueryFactory.getUpdate(nullstr, null, 0, null);
		assertEquals("Strings should equal", target, sql);
		String cols="Col1";
		int vals=1;
		String table="TestTable";
		String what="Col2=Val2";
		target="UPDATE "+table+" SET "+what+" WHERE "+cols+"='"+vals+"'";
		sql=QueryFactory.getUpdate(what, cols, vals, table);
		assertEquals("Strings should equal", target, sql);	
	}

	@Test
	public void testGetSelect() {
		String target="SELECT %WHAT% FROM %FROM% WHERE null='null'";
		String nullstr=null;
		String sql=QueryFactory.getSelect(nullstr, null, null, null);
		assertEquals("Strings should equal", target, sql);
		String cols="Col1";
		String vals="Val1";
		String table="TestTable";
		String what="*";
		target="SELECT "+what+" FROM "+table+" WHERE "+cols+"='"+vals+"'";
		sql=QueryFactory.getSelect(what, cols, vals, table);
		assertEquals("Strings should equal", target, sql);	
		
	}

	@Test
	public void testGetNextPKIDFB() {
		String target="SELECT NEXT VALUE FOR %FROM%_ID_GEN FROM RDB$DATABASE";
		String sql=QueryFactory.getNextPKIDFB(null);
		assertEquals("Strings should equal", target, sql);
		String table="TestTable";
		target="SELECT NEXT VALUE FOR "+table+"_ID_GEN FROM RDB$DATABASE";
		sql=QueryFactory.getNextPKIDFB(table);
		assertEquals("Strings should equal", target, sql);
	}

}
