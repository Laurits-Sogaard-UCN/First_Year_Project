package controller;

import java.util.ArrayList;

import database.WorkScheduleDB;
import database.WorkScheduleDBIF;
import model.WorkSchedule;
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
	
	public boolean setTotalHoursOnWorkSchedule(int hours, String employeeCPR) throws DataAccessException {
		boolean set = false;
		
		if(workScheduleDB.setTotalHoursOnWorkSchedule(hours, employeeCPR)) {
			set = true;
		}
		return set;
	}
	
	public ArrayList<WorkSchedule> getAllWorkSchedules() throws DataAccessException {
		ArrayList<WorkSchedule> workSchedules = workScheduleDB.getAllWorkSchedules();
		return workSchedules;
	}
	
	

}
