package database;

import java.util.ArrayList;

import model.WorkSchedule;
import utility.DataAccessException;

public interface WorkScheduleDBIF {
	
	public int findWorkScheduleIDOnEmployeeCPR(String CPR) throws DataAccessException;
	
	public boolean setTotalHoursOnWorkSchedule(int hours, String employeeCPR) throws DataAccessException;
	
	public ArrayList<WorkSchedule> getAllPartTimeWorkSchedules() throws DataAccessException;
	
	public String getEmployeeCPROnID(int id) throws DataAccessException;

}
