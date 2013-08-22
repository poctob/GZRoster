package com.gzlabs.gzroster.data.test;

import com.gzlabs.gzroster.data.DataManager;
import com.gzlabs.gzroster.gui.IConnectionStatus;
import com.gzlabs.gzroster.gui.IDisplayStatus;

/**
 * Initializes common objects for use with all tests.
 * @author apavlune
 *
 */
public class TestCommon implements IDisplayStatus, IConnectionStatus{

	private DataManager dman;
	private boolean isInitialized;
	private boolean isError;
	
	public TestCommon()
	{
		isInitialized=false;
		dman=new DataManager(this, this, "Test.config");
		
		Thread worker=new Thread(dman);
		worker.setName("DataManager");
		worker.start();
			
		while (!isInitialized) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		if(isError)
		{
			ShowErrorBox("Intialization error");
			System.exit(0);
		}
	}
	
	public DataManager getDman()
	{
		return dman;
	}

	@Override
	public void setError() {
		isError=true;
		
	}

	@Override
	public void setInitialized() {
		isInitialized=true;
		
	}

	@Override
	public void DisplayStatus(String status) {
		System.out.println(status);
		
	}

	@Override
	public void ShowErrorBox(String error) {
		System.out.println(error);
		
	}
}
