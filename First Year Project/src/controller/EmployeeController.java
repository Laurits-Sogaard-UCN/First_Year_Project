package controller;

import database.EmployeeDB;
import database.EmployeeDBIF;
import model.Employee;
import model.FullTimeEmployee;
import model.Shop;
import utility.DataAccessException;

public class EmployeeController {
	
	private EmployeeDBIF employeeDB;
	private Employee loggedInEmployee;
	private ShopController shopController;
	private Shop shop;
	
	public EmployeeController() throws DataAccessException {
		employeeDB = new EmployeeDB();
		this.shop = new Shop(1);
	}
	
	public Employee login() throws DataAccessException {
		Employee employee = new FullTimeEmployee("9876512345", "Kallesen", "Mathias", "MathiasKS@mail.com", "Millionvej 44", "+4512344321", "331", "opetdss2", "Manager", shop, 25000);
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
