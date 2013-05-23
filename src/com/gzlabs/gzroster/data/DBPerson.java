/*package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.gzlabs.drosterheper.DBManager;

/**
 * Database person representation.
 * @author apavlune
 *
 *//*
public class DBPerson extends DBObject{	
	
	public DBPerson(String table_name, DBManager dbman)
	{
		this.table_name=table_name;
		this.setDbman(dbman);
		this.cols=new HashMap<String, Object>();
	}
		
	@Override
	void populateData(ResultSet rs) {
		if(rs!=null)
		{
			try {
				if(rs.next())
				{
					cols.put("PERSON_ID", rs.getString("PERSON_ID"));
					cols.put("PERSON_NAME", rs.getString("PERSON_NAME"));
					cols.put("ADDRESS", rs.getString("ADDRESS"));
					cols.put("PHONE_HOME", rs.getString("PHONE_HOME"));
					cols.put("PHONE_MOBILE", rs.getString("PHONE_MOBILE"));
					cols.put("NOTE", rs.getString("NOTE"));
					cols.put("ACTIVE_PERSON", rs.getString("ACTIVE_PERSON"));
					cols.put("EMAIL_ADDRESS", rs.getString("EMAIL_ADDRESS"));
					cols.put("WEEKLY_TIME", rs.getString("WEEKLY_TIME"));
					cols.put("DAILY_TIME", rs.getString("DAILY_TIME"));
					cols.put("MOMTHLY_TIME", rs.getString("MOMTHLY_TIME"));
					cols.put("WEB_NAME", rs.getString("WEB_NAME"));
					cols.put("WEB_PASSWORD", rs.getString("WEB_PASSWORD"));
					cols.put("EMPLOYEE_KEY", rs.getString("EMPLOYEE_KEY"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}

	@Override
	void updateKey() {
		cols.put("EMPLOYEE_KEY", this.generateNewKey());
		
	}
	
	@Override
	boolean doesRecordExist() {
		ResultSet rs=runSQL(getSelect_sql("EMPLOYEE_KEY",(String) cols.get("EMPLOYEE_KEY")),true);
		try {
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}

	@Override
	boolean doesNameExist() {
		ResultSet rs=runSQL(getSelect_sql("PERSON_NAME",(String) cols.get("PERSON_NAME")),true);
		try {
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}

}*/
