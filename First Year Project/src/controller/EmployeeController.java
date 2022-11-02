package controller;

import database.EmployeeDB;
import database.EmployeeDBIF;
import model.Employee;
import utility.DataAccessException;

public class EmployeeController {
	
	private EmployeeDBIF employeeDB;
	private Employee loggedInEmployee;
	
	public EmployeeController() throws DataAccessException {
		employeeDB = new EmployeeDB();
	}
	
	public Employee login(String username, String password) throws DataAccessException {
		Employee employee = employeeDB.login(username, password);
		loggedInEmployee = employee;
		return employee;
	}

	/**
	 * @return the loggedInEmployee
	 */
	public Employee getLoggedInEmployee() {
		return loggedInEmployee;
	}

	/**
	 * @param loggedInEmployee the loggedInEmployee to set
	 */
	public void setLoggedInEmployee(Employee loggedInEmployee) {
		this.loggedInEmployee = loggedInEmployee;
	}

}
