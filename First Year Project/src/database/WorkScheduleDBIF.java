package database;

import utility.DataAccessException;

public interface WorkScheduleDBIF {
	
	public int findWorkScheduleIDOnEmployeeCPR(String CPR) throws DataAccessException;

}
