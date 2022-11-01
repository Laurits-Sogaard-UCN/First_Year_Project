package database;

import model.Employee;
import utility.DataAccessException;

public interface EmployeeDBIF {
	
	public Employee login(String username, String password) throws DataAccessException;

}
