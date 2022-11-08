package controller;

import database.WorkScheduleDB;
import database.WorkScheduleDBIF;
import utility.DataAccessException;

public class WorkScheduleController {
	
	private WorkScheduleDBIF workScheduleDB;
	
	public WorkScheduleController() throws DataAccessException {
		workScheduleDB = new WorkScheduleDB();
	}
	
	public int findWorkScheduleIDOnEmployeeCPR(String employeeCPR) throws DataAccessException {
		int workScheduleID = workScheduleDB.findWorkScheduleIDOnEmployeeCPR(employeeCPR);
		return workScheduleID;
	}
	
	

}
