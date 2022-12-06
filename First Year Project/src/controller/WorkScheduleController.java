package controller;

import java.util.ArrayList;

import database.WorkScheduleDB;
import database.WorkScheduleDBIF;
import model.WorkSchedule;
import utility.DataAccessException;
import utility.DatabaseType;

public class WorkScheduleController {
	
	private WorkScheduleDBIF workScheduleDB;
	
	/**
	 * Constructor to initialize instance variables. 
	 * @throws DataAccessException
	 */
	public WorkScheduleController(DatabaseType databaseType) throws DataAccessException {
		workScheduleDB = new WorkScheduleDB(databaseType);
	}
	
	/**
	 * Finds a work schedule ID belonging to an employee with given CPR number. 
	 * @param employeeCPR
	 * @return workScheduleID
	 * @throws DataAccessException
	 */
	public int findWorkScheduleIDOnEmployeeCPR(String employeeCPR) throws DataAccessException {
		int workScheduleID = workScheduleDB.findWorkScheduleIDOnEmployeeCPR(employeeCPR);
		return workScheduleID;
	}

	/**
	 * Sets a given new total hours on a work schedule belonging to an employee
	 * with given CPR number. 
	 * @param hours
	 * @param employeeCPR
	 * @return set
	 * @throws DataAccessException
	 */
	public boolean setTotalHoursOnWorkSchedule(int hours, String employeeCPR) throws DataAccessException {
		boolean set = false;
		
		if(workScheduleDB.setTotalHoursOnWorkSchedule(hours, employeeCPR)) {
			set = true;
		}
		return set;
	}
	
	/**
	 * Gets all work schedules belonging to part-time employees. 
	 * @return workSchedules
	 * @throws DataAccessException
	 */
	public ArrayList<WorkSchedule> getAllPartTimeWorkSchedules() throws DataAccessException {
		ArrayList<WorkSchedule> workSchedules = workScheduleDB.getAllPartTimeWorkSchedules();
		return workSchedules;
	}

	public String getEmployeeCPROnID(int id) throws DataAccessException {
		String employeeCPR = workScheduleDB.getEmployeeCPROnID(id);
		return employeeCPR;
	}
	
	

}
