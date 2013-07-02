package com.gzlabs.gzroster.sql.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DB_FactoryTest.class, DBManagerTest.class,
		QueryFactoryTest.class })
public class AllTests {

}
