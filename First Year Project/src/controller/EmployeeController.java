package controller;

import model.Employee;
import model.FullTimeEmployee;
import model.Shop;
import utility.DataAccessException;

public class EmployeeController {
	
	private Employee loggedInEmployee;
	private Shop shop;
	
	/**
	 * Constructor to initialize instance variables. 
	 * @throws DataAccessException
	 */
	public EmployeeController() throws DataAccessException {
		this.shop = new Shop(1);
		loggedInEmployee = new FullTimeEmployee("9876512345", "Kallesen", "Mathias", "MathiasKS@mail.com", "Millionvej 44", "+4512344321", "331", "opetdss2", "Manager", shop, 25000);
	}
	
	/**
	 * Gets logged in employee. 
	 * @return loggedInEmployee
	 */
	public Employee getLoggedInEmployee() {
		return loggedInEmployee;
	}

}
